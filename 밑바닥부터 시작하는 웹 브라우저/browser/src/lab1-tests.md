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
