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
