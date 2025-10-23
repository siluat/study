import wbetools
import tkinter
import urllib.parse
import dukpy
from lab4 import print_tree, HTMLParser
from lab6 import CSSParser
from lab6 import tree_to_list
from lab8 import URL, Element, Text, Browser, Tab
from lab8 import DEFAULT_STYLE_SHEET

EVENT_DISPATCH_JS = \
    "new Node(dukpy.handle).dispatchEvent(dukpy.type)"

RUNTIME_JS = open("runtime9.js").read()

class JSContext:
    def __init__(self, tab):
        self.tab = tab

        self.interp = dukpy.JSInterpreter()
        self.interp.export_function("log", print)
        self.interp.export_function("querySelectorAll", self.querySelectorAll)
        self.interp.export_function("getAttribute", self.getAttribute)
        self.interp.evaljs(RUNTIME_JS)

        self.node_to_handle = {}
        self.handle_to_node = {}

    def run(self, script, code):
        try:
            return self.interp.evaljs(code)
        except dukpy.JSRuntimeError as e:
            print("Script", script, "crashed", e)
    
    def dispatch_event(self, type, elt):
        handle = self.node_to_handle.get(elt, -1)
        self.interp.evaljs(
            EVENT_DISPATCH_JS, type=type, handle=handle)

    def get_handle(self, elt):
        if elt not in self.node_to_handle:
            handle = len(self.node_to_handle)
            self.node_to_handle[elt] = handle
            self.handle_to_node[handle] = elt
        else:
            handle = self.node_to_handle[elt]
        return handle

    def querySelectorAll(self, selector_text):
        selector = CSSParser(selector_text).selector()
        nodes = [node for node
                    in tree_to_list(self.tab.nodes, [])
                    if selector.matches(node)]
        return [self.get_handle(node) for node in nodes]

    def getAttribute(self, handle, attr):
        elt = self.handle_to_node[handle]
        attr = elt.attributes.get(attr, None)
        return attr if attr else ""

@wbetools.patch(Tab)
class Tab:
    def load(self, url, payload=None):
        body = url.request(payload)
        self.scroll = 0
        self.url = url
        self.history.append(url)
        self.nodes = HTMLParser(body).parse()

        self.js = JSContext(self)
        scripts = [node.attributes["src"] for node
                    in tree_to_list(self.nodes, [])
                    if isinstance(node, Element)
                    and node.tag == "script"
                    and "src" in node.attributes]
        for script in scripts:
            script_url = url.resolve(script)
            try:
                body = script_url.request()
            except:
                continue
            self.js.run(script, body)

        self.rules = DEFAULT_STYLE_SHEET.copy()
        links = [node.attributes["href"]
                    for node in tree_to_list(self.nodes, [])
                    if isinstance(node, Element)
                    and node.tag == "link"
                    and node.attributes.get("rel") == "stylesheet"
                    and "href" in node.attributes]
        for link in links:
            style_url = url.resolve(link)
            try:
                body = style_url.request()
            except:
                continue
            self.rules.extend(CSSParser(body).parse())
        self.render()

    def click(self, x, y):
        if self.focus:
            self.focus.is_focused = False
        self.focus = None
        y += self.scroll
        objs = [obj for obj in tree_to_list(self.document, [])
                if obj.x <= x < obj.x + obj.width
                and obj.y <= y < obj.y + obj.height]
        if not objs: return self.render()
        elt = objs[-1].node
        while elt:
            if isinstance(elt, Text):
                pass
            elif elt.tag == "a" and "href" in elt.attributes:
                self.js.dispatch_event("click", elt)
                url = self.url.resolve(elt.attributes["href"])
                return self.load(url)
            elif elt.tag == "input":
                self.js.dispatch_event("click", elt)
                elt.attributes["value"] = ""
                self.focus = elt
                elt.is_focused = True
                return self.render()
            elif elt.tag == "button":
                self.js.dispatch_event("click", elt)
                while elt:
                    if elt.tag == "form" and "action" in elt.attributes:
                        return self.submit_form(elt)
                    elt = elt.parent
            elt = elt.parent
        self.render()

    def submit_form(self, elt):
        self.js.dispatch_event("submit", elt)
        inputs = [node for node in tree_to_list(elt, [])
                    if isinstance(node, Element)
                    and node.tag == "input"
                    and "name" in node.attributes]

        body = ""
        for input in inputs:
            name = input.attributes["name"]
            value = input.attributes.get("value", "")
            name = urllib.parse.quote(name)
            value = urllib.parse.quote(value)
            body += "&" + name + "=" + value
        body = body[1:]

        url = self.url.resolve(elt.attributes["action"])
        self.load(url, body)

    def keypress(self, char):
        if self.focus:
            self.js.dispatch_event("keydown", self.focus)
            self.focus.attributes["value"] += char
            self.render()

if __name__ == "__main__":
    import sys
    Browser().new_tab(URL(sys.argv[1]))
    tkinter.mainloop()
