Tests for WBE Chapter 11
========================

Chapter 11 (Adding Visual Effects) is a highly visual chapter. We won't
test the bitmap outputs directly, but instead the display lists generated.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab11

    >>> styles = 'http://test.test/styles.css'
    >>> test.socket.respond(styles, b"HTTP/1.0 200 OK\r\n" +
    ... b"content-type: text/css\r\n\r\n" +
    ... b"div { background-color:blue}")
