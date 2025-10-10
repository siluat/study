Tests for WBE Chapter 2
=======================

Chapter 2 (Drawing to the Screen) is about how to get text parsed, laid out
and drawn on the screen, plus a very simple implementation of scrolling. This
file contains tests for this functionality.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab2

2.2 Drawing to the Window
-------------------------

Instantiating `Browser` should create a `window` object and a `canvas`
inside of the appropriate width and height:

    >>> browser = lab2.Browser()
    >>> browser.canvas.winfo_reqwidth()
    800
    >>> browser.canvas.winfo_reqheight()
    600
