Tests for WBE Chapter 7
=======================

Chapter 7 (Handling Buttons and Links) introduces hit testing, navigation
through link clicks, and browser chrome for the URL bar and tabs.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab7

7.2 Line Layout, Redux
----------------------

Let's load a page with multiple lines (using `<br>`):

    >>> url = test.socket.serve(
    ...   "<div>This is a test<br>Also a test<br>"
    ...   "And this too</div>")
    >>> browser = lab7.Browser()
    >>> browser.load(lab7.URL(url))
    >>> lab7.print_tree(browser.document.node)
     <html>
       <body>
         <div>
           'This is a test'
           <br>
           'Also a test'
           <br>
           'And this too'

Here is how the lines are represented in chapter 7:

    >>> lab7.print_tree(browser.document)
     DocumentLayout()
       BlockLayout[block](x=13, y=18, width=774, height=45.0)
         BlockLayout[block](x=13, y=18, width=774, height=45.0)
           BlockLayout[inline](x=13, y=18, width=774, height=45.0)
             LineLayout(x=13, y=18, width=774, height=15.0)
               TextLayout(x=13, y=20.25, width=48, height=12, word=This)
               TextLayout(x=73, y=20.25, width=24, height=12, word=is)
               TextLayout(x=109, y=20.25, width=12, height=12, word=a)
               TextLayout(x=133, y=20.25, width=48, height=12, word=test)
             LineLayout(x=13, y=33.0, width=774, height=15.0)
               TextLayout(x=13, y=35.25, width=48, height=12, word=Also)
               TextLayout(x=73, y=35.25, width=12, height=12, word=a)
               TextLayout(x=97, y=35.25, width=48, height=12, word=test)
             LineLayout(x=13, y=48.0, width=774, height=15.0)
               TextLayout(x=13, y=50.25, width=36, height=12, word=And)
               TextLayout(x=61, y=50.25, width=48, height=12, word=this)
               TextLayout(x=121, y=50.25, width=36, height=12, word=too)

Whereas in chapter 6 there is no direct layout tree representation of
text.
