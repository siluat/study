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
