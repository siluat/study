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

2.3 Laying Out Text
-------------------

The lex function returns all text not contained in an HTML tag.

    >>> lab2.lex('<body>hello</body>')
    'hello'
    >>> lab2.lex('he<body>llo</body>')
    'hello'
    >>> lab2.lex('he<body>l</body>lo')
    'hello'
    >>> lab2.lex('he<body>l<div>l</div>o</body>')
    'hello'

Note that the tags do not have to match:

    >>> lab2.lex('he<body>l</div>lo')
    'hello'
    >>> lab2.lex('he<body>l<div>l</body>o</div>')
    'hello'

Breakpoints can be set after each character:

    >>> test.patch_breakpoint()
    >>> lab2.lex('abc')
    breakpoint(name='lex', 'a')
    breakpoint(name='lex', 'ab')
    breakpoint(name='lex', 'abc')
    'abc'
    >>> test.unpatch_breakpoint()

The `load` function should then lay these characters out line by line.
It uses WIDTH to determine the maximum length of a line, HSTEP for the
horizontal distance between letters, and VSTEP for the vertical
distance between lines.

Let's override those values to convenient ones that make it easy to do
math when testing:

    >>> lab2.WIDTH = 11
    >>> lab2.HSTEP = 1
    >>> lab2.VSTEP = 1

Let's install a mock canvas that prints out the x and y coordinates,
plus the text drawn:

    >>> test.patch_canvas()
    >>> browser = lab2.Browser()
    >>> browser.load(lab2.URL(test.socket.serve("Hello, World!")))
    create_text: x=1 y=1 text=H
    create_text: x=2 y=1 text=e
    create_text: x=3 y=1 text=l
    create_text: x=4 y=1 text=l
    create_text: x=5 y=1 text=o
    create_text: x=6 y=1 text=,
    create_text: x=7 y=1 text= 
    create_text: x=8 y=1 text=W
    create_text: x=9 y=1 text=o
    create_text: x=1 y=2 text=r
    create_text: x=2 y=2 text=l
    create_text: x=3 y=2 text=d
    create_text: x=4 y=2 text=!

Note that each character steps to the right until it reaches the end
of the line, at which point it wraps.
