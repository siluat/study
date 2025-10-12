Tests for WBE Chapter 4
=======================

Chapter 4 (Constructing a Document Tree) adds support for the document tree
(i.e. the DOM).  This file contains tests for the additional functionality.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab4

Testing Tweaks
--------------

Once you get to adding attributes in Section 4.4, please make sure to
modify `Element.__repr__` to print those attributes.

4.2 Constructing the Tree
-------------------------

HTMLParser is a class whose constructor takes HTML body text as an
argument, and can parse it:

	>>> parser = lab4.HTMLParser("<html><body>test</body></html>")
    >>> root = parser.parse()
    >>> root.tag
    'html'
    >>> len(root.children)
    1
    >>> root.children[0].tag
    'body'
    >>> len(root.children[0].children)
    1
    >>> root.children[0].children[0].text
    'test'

Various mixes of open and close tags work:

	>>> parser = lab4.HTMLParser("<html><head></head><body>test <b>Test</b> test</body></html>")
    >>> root = parser.parse()
    >>> root.tag
    'html'
    >>> len(root.children)
    2
    >>> root.children[0].tag
    'head'
    >>> len(root.children[0].children)
    0
    >>> root.children[1].tag
    'body'
    >>> len(root.children[1].children)
    3
    >>> root.children[1].children[0].text
    'test '
    >>> root.children[1].children[1].tag
    'b'
    >>> root.children[1].children[2].text
    ' test'

4.3 Debugging a Parser
----------------------

We can now print the HTML tree:

	>>> parser = lab4.HTMLParser("<html><head></head><body>test <b>Test</b> test</body></html>")
    >>> lab4.print_tree(parser.parse())
     <html>
       <head>
       <body>
         'test '
         <b>
           'Test'
         ' test'

Whitespace nodes are also skipped:

	>>> parser = lab4.HTMLParser("<!doctype html> <html> <body> <b>test</b> </body> </html>")
    >>> lab4.print_tree(parser.parse())
     <html>
       <body>
         <b>
           'test'
