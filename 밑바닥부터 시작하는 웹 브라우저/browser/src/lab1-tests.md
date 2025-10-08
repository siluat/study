Tests for WBE Chapter 1
=======================

Chapter 1 (Downloading Web Pages) covers parsing URLs, HTTP requests
and responses, and a very very simplistic print function that writes
to the screen. This file contains tests for those components.

Here's the testing boilerplate.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab1
    
You can run this file with `doctest` to run the tests.

1.4 Telnet in Python
--------------------

Constructing a `URL` object parses a URL:

    >>> lab1.URL('http://test.test/example1') #doctest: +ELLIPSIS
    URL(scheme=http, host=test.test,... path='/example1')

This works even if there is no path:

    >>> lab1.URL('http://test.test') #doctest: +ELLIPSIS
    URL(scheme=http, host=test.test,... path='/')

The `...` allow you to print a port once you implement that in Section
1.7.

If you fail to provide a valid URL, it'll load the book's home page:

    >>> lab1.URL('not a url') #doctest: +ELLIPSIS
    Malformed URL found, falling back to the WBE home page.
      URL was: not a url
    URL(scheme=http..., host=browser.engineering,... path='/')

1.5 Request and Response
------------------------

The `request` function makes HTTP requests. To test it, we use the
`test.socket` object, which mocks the HTTP server:

    >>> url = test.socket.serve("Body text")

Then we request the URL and test both request and response:

    >>> body = lab1.URL(url).request()
    >>> test.socket.last_request(url)
    b'GET /page0 HTTP/1.0\r\nHost: test\r\n\r\n'
    >>> body
    'Body text'

With an unusual `Transfer-Encoding` the request should fail:
    
    >>> url = test.socket.serve("", headers={
    ...     "Transfer-Encoding": "chunked"
    ... })
    >>> lab1.URL(url).request()
    Traceback (most recent call last):
      ...
    AssertionError

Likewise with `Content-Encoding`:
    
    >>> url = test.socket.serve("", headers={
    ...     "Content-Encoding": "gzip"
    ... })
    >>> lab1.URL(url).request()
    Traceback (most recent call last):
      ...
    AssertionError
