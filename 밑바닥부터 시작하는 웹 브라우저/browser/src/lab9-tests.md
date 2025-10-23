Tests for WBE Chapter 9
=======================

Chapter 9 (Running Interactive Scripts) introduces JavaScript and the DOM API,
The focus of the chapter is browser-JS
interaction.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab9

Note that we aren't mocking `dukpy`. It should just run JavaScript normally!

9.2 Running JavaScript Code
---------------------------

The browser should download JavaScript code mentioned in a `<script>` tag:

    >>> url2 = lab9.URL(test.socket.serve(""))
    >>> url = lab9.URL(test.socket.serve("<script src=" + str(url2) + "></script>"))
    >>> lab9.Browser().new_tab(url)
    >>> test.socket.last_request(str(url2))
    b'GET /page0 HTTP/1.0\r\nHost: test\r\n\r\n'

If the script succeeds, the browser prints nothing:

    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\nvar x = 2; x + x")
    >>> lab9.Browser().new_tab(url)

If instead the script crashes, the browser prints an error message:

    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\nthrow Error('Oops');")
    >>> lab9.Browser().new_tab(url) #doctest: +ELLIPSIS
    Script http://test/page0 crashed Error: Oops
    ...

Note that in the last test I set the `ELLIPSIS` flag to elide the duktape stack
trace.

9.3 Exporting Functions
-----------------------

For the rest of these tests we're going to use `console.log` for most testing:

    >>> script = "console.log('Hello, world!')"
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\n" + script.encode("utf8"))
    >>> lab9.Browser().new_tab(url)
    Hello, world!

Note that you can print other data structures as well:

    >>> script = "console.log([2, 3, 4])"
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\n" + script.encode("utf8"))
    >>> lab9.Browser().new_tab(url)
    [2, 3, 4]

Let's test that variables work:

    >>> script = "var x = 'Hello!'; console.log(x)"
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\n" + script.encode("utf8"))
    >>> lab9.Browser().new_tab(url)
    Hello!

Next let's try to do two scripts:

    >>> url2 = 'http://test.test/js1'
    >>> url3 = 'http://test.test/js2'
    >>> html_page = "<script src=" + url2 + "></script>" + "<script src=" + url3 + "></script>"
    >>> test.socket.respond(str(url), b"HTTP/1.0 200 OK\r\n\r\n" + html_page.encode("utf8"))
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\nvar x = 'Testing, testing';")
    >>> test.socket.respond(str(url3), b"HTTP/1.0 200 OK\r\n\r\nconsole.log(x);")
    >>> lab9.Browser().new_tab(url)
    Testing, testing

9.5 Returning Handles
---------------------

The `querySelectorAll` method is easiest to test by looking at the number of
matching nodes:

    >>> page = """<!doctype html>
    ... <div>
    ...   <p id=lorem>Lorem</p>
    ...   <p class=ipsum>Ipsum</p>
    ... </div>"""
    >>> test.socket.respond(str(url), b"HTTP/1.0 200 OK\r\n\r\n" + page.encode("utf8"))
    >>> b = lab9.Browser()
    >>> b.new_tab(url)
    >>> js = b.tabs[0].js
    >>> js.run("test", "document.querySelectorAll('div').length")
    1
    >>> js.run("test", "document.querySelectorAll('p').length")
    2
    >>> js.run("test", "document.querySelectorAll('html').length")
    1

That last query is finding an implicit tag. Complex queries are also supported

    >>> js.run("test", "document.querySelectorAll('html p').length")
    2
    >>> js.run("test", "document.querySelectorAll('html body div p').length")
    2
    >>> js.run("test", "document.querySelectorAll('body html div p').length")
    0

`querySelectorAll` should return `Node` objects:

    >>> js.run("test", "document.querySelectorAll('html')[0] instanceof Node")
    True

9.6 Wrapping Handles
--------------------

Once we have a `Node` object we can call `getAttribute`:

    >>> js.run("test", "document.querySelectorAll('p')[0].getAttribute('id')")
    'lorem'

Note that this is "live": as the page changes `querySelectorAll` gives new results:

    >>> b.tabs[0].nodes.children[0].children[0].children[0].attributes['id'] = 'blah'
    >>> js.run("test", "document.querySelectorAll('p')[0].getAttribute('id')")
    'blah'
