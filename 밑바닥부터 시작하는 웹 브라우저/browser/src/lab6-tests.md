Tests for WBE Chapter 6
=======================

Chapter 6 (Applying User Styles) introduces a CSS parser for the style attribute
and style sheets, and adds support for inherited properties, tag selectors, and
descendant selectors.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab6

6.1 Parsing with Functions
--------------------------

Let's test the `body` function, making sure it can parse CSS
property-value pairs.

    >>> lab6.CSSParser("background-color:lightblue;").body()
    {'background-color': 'lightblue'}

Whitespace should be allowed:

    >>> lab6.CSSParser("background-color : lightblue ;").body()
    {'background-color': 'lightblue'}

Multiple property-value pairs, with semicolons, should also work:

    >>> lab6.CSSParser("background-color: lightblue; margin: 1px;").body()
    {'background-color': 'lightblue', 'margin': '1px'}

The final semicolon should be optional:

    >>> lab6.CSSParser("background-color: lightblue").body()
    {'background-color': 'lightblue'}

Oddly, the book's parser doesn't allow the `style` value to start with
a space, probably because its HTML parser doesn't make that possible.

If there's junk or other garbage, the parser shouldn't crash


    >>> lab6.CSSParser("this isn't a CSS property : value pair ; ; ; lol").body()
    {}

6.2 The `style` Attribute
-------------------------

We need to make sure we didn't break layout with all of these changes:

    >>> sample_html = "<div></div><div>text</div><div><div></div>text</div><span></span><span>text</span>"
    >>> url = lab6.URL(test.socket.serve(sample_html))
    >>> browser = lab6.Browser()
    >>> browser.load(url)
    >>> lab6.print_tree(browser.document)
     DocumentLayout()
       BlockLayout[block](x=13, y=18, width=774, height=45.0, node=<html>)
         BlockLayout[block](x=13, y=18, width=774, height=45.0, node=<body>)
           BlockLayout[block](x=13, y=18, width=774, height=0, node=<div>)
           BlockLayout[inline](x=13, y=18, width=774, height=15.0, node=<div>)
           BlockLayout[block](x=13, y=33.0, width=774, height=15.0, node=<div>)
             BlockLayout[block](x=13, y=33.0, width=774, height=0, node=<div>)
             BlockLayout[inline](x=13, y=33.0, width=774, height=15.0, node='text')
           BlockLayout[block](x=13, y=48.0, width=774, height=0, node=<span>)
           BlockLayout[inline](x=13, y=48.0, width=774, height=15.0, node=<span>)

    >>> browser.display_list #doctest: +NORMALIZE_WHITESPACE
    [DrawText(top=20.25 left=13 bottom=32.25 text=text
              font=Font size=12 weight=normal slant=roman style=None),
     DrawText(top=35.25 left=13 bottom=47.25 text=text
              font=Font size=12 weight=normal slant=roman style=None),
     DrawText(top=50.25 left=13 bottom=62.25 text=text
              font=Font size=12 weight=normal slant=roman style=None)]

Here's a case with a paragraph split over multiple lines:

    >>> url = lab6.URL(test.socket.serve("""
    ... <p>Hello<br>World!</p>
    ... """))
    >>> browser = lab6.Browser()
    >>> browser.load(url)
    >>> browser.display_list #doctest: +NORMALIZE_WHITESPACE
    [DrawText(top=20.25 left=13 bottom=32.25 text=Hello
              font=Font size=12 weight=normal slant=roman style=None),
     DrawText(top=35.25 left=13 bottom=47.25 text=World!
              font=Font size=12 weight=normal slant=roman style=None)]

Let's test an element with a `style` attribute:

    >>> url = test.socket.serve("<div style='background-color:lightblue'></div>")
    >>> browser = lab6.Browser()
    >>> browser.load(lab6.URL(url))
    >>> browser.nodes.children[0].children[0].style['background-color']
    'lightblue'

This should in fact cause a background rectangle to be generated:

    >>> browser.display_list
    [DrawRect(top=18 left=13 bottom=18 right=787 color=lightblue)]
