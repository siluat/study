import wbetools
import tkinter
import dukpy
from lab4 import HTMLParser
from lab6 import CSSParser
from lab6 import tree_to_list
from lab8 import URL, Element, Browser, Tab
from lab8 import DEFAULT_STYLE_SHEET

@wbetools.patch(Tab)
class Tab:
    def load(self, url, payload=None):
        body = url.request(payload)
        self.scroll = 0
        self.url = url
        self.history.append(url)
        self.nodes = HTMLParser(body).parse()

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
            try:
                dukpy.evaljs(body)
            except dukpy.JSRuntimeError as e:
                print("Script", script, "crashed", e)

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

if __name__ == "__main__":
    import sys
    Browser().new_tab(URL(sys.argv[1]))
    tkinter.mainloop()
