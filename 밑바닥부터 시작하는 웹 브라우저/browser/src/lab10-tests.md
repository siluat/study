Tests for WBE Chapter 10
========================

Chapter 10 (Keeping Data Private) introduces cookies.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab10

Testing basic cookies
=====================

When a server sends a `Set-Cookie` header, the browser should save it
in the cookie jar:

    >>> browser = lab10.Browser()
    >>> url = 'http://test.test/login'
    >>> test.socket.respond(url, b"HTTP/1.0 200 OK\r\nSet-Cookie: foo=bar\r\n\r\n")
    >>> browser.new_tab(lab10.URL(url))
    >>> lab10.COOKIE_JAR["test.test"]
    'foo=bar'

Moreover, the browser should now send a `Cookie` header with future
requests:

    >>> url2 = 'http://test.test/'
    >>> test.socket.respond(url2, b"HTTP/1.0 200 OK\r\n\r\n\r\n")
    >>> browser.new_tab(lab10.URL(url2))
    >>> test.socket.last_request(url2)
    b'GET / HTTP/1.0\r\nHost: test.test\r\nCookie: foo=bar\r\n\r\n'

Unrelated sites should not be sent the cookie:

    >>> url3 = 'http://other.site/'
    >>> test.socket.respond(url3, b"HTTP/1.0 200 OK\r\n\r\n\r\n")
    >>> browser.new_tab(lab10.URL(url3))
    >>> test.socket.last_request(url3)
    b'GET / HTTP/1.0\r\nHost: other.site\r\n\r\n'

Note that these three requests were across three different tabs. All
tabs should use the same cookie jar.

Cookie values can be updated:

    >>> lab10.COOKIE_JAR["test.test"]
    'foo=bar'
    >>> test.socket.respond(url, b"HTTP/1.0 200 OK\r\nSet-Cookie: foo=baz\r\n\r\n")
    >>> browser.new_tab(lab10.URL(url))
    >>> lab10.COOKIE_JAR["test.test"]
    'foo=baz'

The trailing slash is also optional:

    >>> url_no_slash = 'http://test.test'
    >>> test.socket.respond(url_no_slash + '/', b"HTTP/1.0 200 OK\r\n\r\n\r\n")
    >>> browser.new_tab(lab10.URL(url_no_slash))
    >>> test.socket.last_request(url_no_slash + '/')
    b'GET / HTTP/1.0\r\nHost: test.test\r\nCookie: foo=baz\r\n\r\n'

Cookie parameters are parsed correctly:

    >>> test.socket.respond(url, b"HTTP/1.0 200 OK\r\n" + \
    ...   b"Set-Cookie: foo=baz; HttpOnly; SameSite=Lax; Secure\r\n\r\n")
    >>> browser.new_tab(lab10.URL(url))
    >>> lab10.COOKIE_JAR["test.test"]
    'foo=baz; HttpOnly; SameSite=Lax; Secure'

Testing XMLHttpRequest
======================

First, let's test the basic `XMLHttpRequest` functionality. We'll be
making a lot of `XMLHttpRequest` calls so let's add a little helper
for that:

    >>> def void(s): return
    >>> def xhrjs(url):
    ...     return """x = new XMLHttpRequest();
    ... x.open("GET", """ + repr(url) + """, false);
    ... x.send();
    ... console.log(x.responseText);"""

Now let's test a simple same-site request:

    >>> url = "http://about.blank/"
    >>> test.socket.respond(url, b"HTTP/1.0 200 OK\r\n\r\n")
    >>> url2 = "http://about.blank/hello"
    >>> test.socket.respond(url2, b"HTTP/1.0 200 OK\r\n\r\nHello!")
    >>> browser = lab10.Browser()
    >>> browser.new_tab(lab10.URL(url))
    >>> tab = browser.tabs[0]
    >>> tab.js.run("test", xhrjs(url2))
    Hello!

Relative URLs also work:

    >>> tab.js.run("test", xhrjs("/hello"))
    Hello!

Non-synchronous XHRs should fail:

    >>> tab.js.run("test", "new XMLHttpRequest().open('GET', '/', true)") #doctest: +ELLIPSIS
    Script test crashed Error: Asynchronous XHR is not supported
    ...

If cookies are present, they should be sent:

    >>> lab10.COOKIE_JAR["about.blank"] = 'foo=bar'
    >>> tab.js.run("test", xhrjs(url2))
    Hello!
    >>> test.socket.last_request(url2)
    b'GET /hello HTTP/1.0\r\nHost: about.blank\r\nCookie: foo=bar\r\n\r\n'

Note that the cookie value is sent.

Now let's see that cross-domain requests fail:

    >>> url3 = "http://other.site/"
    >>> test.socket.respond(url3, b"HTTP/1.0 200 OK\r\n\r\nPrivate")
    >>> tab.js.run("test", xhrjs(url3)) #doctest: +ELLIPSIS
    Script test crashed EvalError: Error while calling Python Function...: Exception('Cross-origin XHR request not allowed')
        ...

It's not important whether the request is _ever_ sent; the CORS
exercise requires sending it but the standard implementation does not
send it.
