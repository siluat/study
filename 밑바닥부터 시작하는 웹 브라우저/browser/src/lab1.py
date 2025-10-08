class URL:
    def __init__(self, url):
        try:
            self.scheme, url = url.split("://", 1)
            assert self.scheme == "http"
            if "/" not in url:
                url = url + "/"
            self.host, url = url.split("/", 1)
            self.path = "/" + url
        except:
            print("Malformed URL found, falling back to the WBE home page.")
            print("  URL was: " + url)
            self.__init__("http://browser.engineering/")
    
    def __repr__(self):
        return "URL(scheme={}, host={}, path={!r})".format(self.scheme, self.host, self.path)
