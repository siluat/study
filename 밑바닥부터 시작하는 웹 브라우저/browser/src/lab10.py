import socket
import ssl
import tkinter
import dukpy
import wbetools
from lab4 import HTMLParser
from lab6 import CSSParser
from lab6 import tree_to_list
from lab8 import URL, Browser, Element
from lab8 import DEFAULT_STYLE_SHEET
from lab9 import JSContext, Tab

@wbetools.patch(URL)
class URL:
    def request(self, referrer, payload=None):
        s = socket.socket(
            family=socket.AF_INET,
            type=socket.SOCK_STREAM,
            proto=socket.IPPROTO_TCP,
        )
        s.connect((self.host, self.port))

        if self.scheme == "https":
            ctx = ssl.create_default_context()
            s = ctx.wrap_socket(s, server_hostname=self.host)

        method = "POST" if payload else "GET"
        request = "{} {} HTTP/1.0\r\n".format(method, self.path)
        request += "Host: {}\r\n".format(self.host)
        if self.host in COOKIE_JAR:
            cookie, params = COOKIE_JAR[self.host]
            allow_cookie = True
            if referrer and params.get("samesite", "none") == "lax":
                if method != "GET":
                    allow_cookie = self.host == referrer.host
            if allow_cookie:
                request += "Cookie: {}\r\n".format(cookie)
        if payload:
            length = len(payload.encode("utf8"))
            request += "Content-Length: {}\r\n".format(length)
        request += "\r\n"
        if payload: request += payload
        s.send(request.encode("utf8"))
        response = s.makefile("r", encoding="utf8", newline="\r\n")

        statusline = response.readline()
        version, status, explanation = statusline.split(" ", 2)

        response_headers = {}
        while True:
            line = response.readline()
            if line == "\r\n": break
            header, value = line.split(":", 1)
            response_headers[header.casefold()] = value.strip()

        if "set-cookie" in response_headers:
            cookie = response_headers["set-cookie"]
            params = {}
            if ";" in cookie:
                cookie, rest = cookie.split(";", 1)
                for param in rest.split(";"):
                    if "=" in param:
                        param, value = param.split("=", 1)
                    else:
                        value = "true"
                    params[param.strip().casefold()] = value.casefold()
            COOKIE_JAR[self.host] = (cookie, params)

        assert "transfer-encoding" not in response_headers
        assert "content-encoding" not in response_headers

        content = response.read()
        s.close()

        return response_headers, content

    def origin(self):
        return self.scheme + "://" + self.host + ":" + str(self.port)

COOKIE_JAR = {}

@wbetools.patch(JSContext)
class JSContext:
    def __init__(self, tab):
        self.tab = tab

        self.interp = dukpy.JSInterpreter()
        self.interp.export_function("log", print)
        self.interp.export_function("querySelectorAll", self.querySelectorAll)
        self.interp.export_function("getAttribute", self.getAttribute)
        self.interp.export_function("innerHTML_set", self.innerHTML_set)
        self.interp.export_function("XMLHttpRequest_send", self.XMLHttpRequest_send)
        with open("runtime10.js") as f:
            self.interp.evaljs(f.read())

        self.node_to_handle = {}
        self.handle_to_node = {}

    def XMLHttpRequest_send(self, method, url, body):
        full_url = self.tab.url.resolve(url)
        headers, out = full_url.request(self.tab.url,body)
        if full_url.origin() != self.tab.url.origin():
            raise Exception("Cross-origin XHR request not allowed")
        return out;

    def run(self, script, code):
        try:
            result = self.interp.evaljs(code)
            # Normalize JavaScript undefined/null to Python None
            # dukpy may return {} or other values for undefined
            if result == {} or result is None:
                return None
            return result
        except dukpy.JSRuntimeError as e:
            print("Script", script, "crashed", e)

@wbetools.patch(Tab)
class Tab:
    def load(self, url, payload=None):
        headers, body = url.request(self.url, payload)
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
                header, body = script_url.request(url)
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
                header, body = style_url.request(url)
            except:
                continue
            self.rules.extend(CSSParser(body).parse())
        self.render()

if __name__ == "__main__":
    import sys
    Browser().new_tab(URL(sys.argv[1]))
    tkinter.mainloop()
