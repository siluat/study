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

6.3 Selectors
-------------

A tag selector stores its tag, the key-value pair.

    >>> lab6.CSSParser("div { foo: bar }").parse()
    [(TagSelector(tag=div), {'foo': 'bar'})]

A descendant selector stores its ancestor and descendant as TagSelectors

    >>> lab6.CSSParser("div span { foo: bar }").parse()
    [(DescendantSelector(ancestor=TagSelector(tag=div), descendant=TagSelector(tag=span)), {'foo': 'bar'})]

    >>> lab6.CSSParser("div span h1 { foo: bar }").parse()
    [(DescendantSelector(ancestor=DescendantSelector(ancestor=TagSelector(tag=div), descendant=TagSelector(tag=span)), descendant=TagSelector(tag=h1)), {'foo': 'bar'})]

Multiple rules can be present.

    >>> lab6.CSSParser("div { foo: bar } span { baz : baz2 }").parse()
    [(TagSelector(tag=div), {'foo': 'bar'}), (TagSelector(tag=span), {'baz': 'baz2'})]

Unknown syntaxes are ignored.

    >>> lab6.CSSParser("a;").parse()
    []
    >>> lab6.CSSParser("a {;}").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("{} a;").parse()
    []
    >>> lab6.CSSParser("a { p }").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("a { p: v }").parse()
    [(TagSelector(tag=a), {'p': 'v'})]
    >>> lab6.CSSParser("a { p: ^ }").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("a { p: ; }").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("a { p: v; q }").parse()
    [(TagSelector(tag=a), {'p': 'v'})]
    >>> lab6.CSSParser("a { p: v; ; q: u }").parse()
    [(TagSelector(tag=a), {'p': 'v', 'q': 'u'})]
    >>> lab6.CSSParser("a { p: v; q:: u }").parse()
    [(TagSelector(tag=a), {'p': 'v'})]

Whitespace can be present anywhere. This is an easy mistake to make
with a scannerless parser like used here:

    >>> lab6.CSSParser("a {}").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("a{}").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("a{ }").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("a {} ").parse()
    [(TagSelector(tag=a), {})]
    >>> lab6.CSSParser("a {p:v} ").parse()
    [(TagSelector(tag=a), {'p': 'v'})]
    >>> lab6.CSSParser("a {p :v} ").parse()
    [(TagSelector(tag=a), {'p': 'v'})]
    >>> lab6.CSSParser("a { p:v} ").parse()
    [(TagSelector(tag=a), {'p': 'v'})]
    >>> lab6.CSSParser("a {p: v} ").parse()
    [(TagSelector(tag=a), {'p': 'v'})]
    >>> lab6.CSSParser("a {p:v } ").parse()
    [(TagSelector(tag=a), {'p': 'v'})]

6.4 Applying Style Sheets
-------------------------

Let's also test the `tree_to_list` helper function:

    >>> url = lab6.URL(test.socket.serve("<div>Test</div>"))
    >>> browser = lab6.Browser()
    >>> browser.load(url)
    >>> lab6.print_tree(browser.document)
     DocumentLayout()
       BlockLayout[block](x=13, y=18, width=774, height=15.0, node=<html>)
         BlockLayout[block](x=13, y=18, width=774, height=15.0, node=<body>)
           BlockLayout[inline](x=13, y=18, width=774, height=15.0, node=<div>)
    >>> list = []
    >>> retval = lab6.tree_to_list(browser.document, list)
    >>> retval #doctest: +NORMALIZE_WHITESPACE
    [DocumentLayout(),
     BlockLayout[block](x=13, y=18, width=774, height=15.0, node=<html>),
     BlockLayout[block](x=13, y=18, width=774, height=15.0, node=<body>),
     BlockLayout[inline](x=13, y=18, width=774, height=15.0, node=<div>)]
    >>> retval == list
    True

To test that our browser actually loads style sheets, we create a CSS
file and load a page linking to it:

    >>> cssurl = test.socket.serve("div { background-color: blue; }")
    >>> htmlurl = test.socket.serve("""
    ...    <link rel=stylesheet href='""" + cssurl + """'>
    ...    <div>test</div>
    ... """)
    >>> browser = lab6.Browser()
    >>> browser.load(lab6.URL(htmlurl))

Now we make sure that the `div` is blue:

    >>> browser.nodes.children[1].children[0].style["background-color"]
    'blue'

If the page doesn't exist, the browser doesn't crash:

    >>> htmlurl = test.socket.serve("""
    ...    <link rel=stylesheet href='/does/not/exist'>
    ... """)
    >>> browser.load(lab6.URL(htmlurl))

This first test used an absolute URL, but let's also test relative URLs.

    >>> lab6.URL("http://bar.com/").resolve("http://foo.com/")
    URL(scheme=http, host=foo.com, port=80, path='/')

    >>> lab6.URL("http://bar.com/").resolve("/url")
    URL(scheme=http, host=bar.com, port=80, path='/url')

    >>> lab6.URL("http://bar.com/url1").resolve("url2")
    URL(scheme=http, host=bar.com, port=80, path='/url2')

    >>> lab6.URL("http://bar.com/url1/").resolve("url2")
    URL(scheme=http, host=bar.com, port=80, path='/url1/url2')

    >>> lab6.URL("http://bar.com/url1/").resolve("//baz.com/url2")
    URL(scheme=http, host=baz.com, port=80, path='/url2')

A trailing slash is automatically added if omitted:

    >>> lab6.URL("http://bar.com").resolve("url2")
    URL(scheme=http, host=bar.com, port=80, path='/url2')

You can use `..` to go up:

    >>> lab6.URL("http://bar.com/a/b/c").resolve("d")
    URL(scheme=http, host=bar.com, port=80, path='/a/b/d')
    >>> lab6.URL("http://bar.com/a/b/c").resolve("../d")
    URL(scheme=http, host=bar.com, port=80, path='/a/d')
    >>> lab6.URL("http://bar.com/a/b/c").resolve("../../d")
    URL(scheme=http, host=bar.com, port=80, path='/d')
    >>> lab6.URL("http://bar.com/a/b/c").resolve("../../../d")
    URL(scheme=http, host=bar.com, port=80, path='/d')
