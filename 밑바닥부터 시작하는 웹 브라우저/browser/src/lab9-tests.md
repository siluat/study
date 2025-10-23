Tests for WBE Chapter 9
=======================

Chapter 9 (Running Interactive Scripts) introduces JavaScript and the DOM API,
The focus of the chapter is browser-JS
interaction.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab9

Note that we aren't mocking `dukpy`. It should just run JavaScript normally!

9.2 Running JavaScript Code
---------------------------

The browser should download JavaScript code mentioned in a `<script>` tag:

    >>> url2 = lab9.URL(test.socket.serve(""))
    >>> url = lab9.URL(test.socket.serve("<script src=" + str(url2) + "></script>"))
    >>> lab9.Browser().new_tab(url)
    >>> test.socket.last_request(str(url2))
    b'GET /page0 HTTP/1.0\r\nHost: test\r\n\r\n'

If the script succeeds, the browser prints nothing:

    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\nvar x = 2; x + x")
    >>> lab9.Browser().new_tab(url)

If instead the script crashes, the browser prints an error message:

    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\nthrow Error('Oops');")
    >>> lab9.Browser().new_tab(url) #doctest: +ELLIPSIS
    Script http://test/page0 crashed Error: Oops
    ...

Note that in the last test I set the `ELLIPSIS` flag to elide the duktape stack
trace.

9.3 Exporting Functions
-----------------------

For the rest of these tests we're going to use `console.log` for most testing:

    >>> script = "console.log('Hello, world!')"
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\n" + script.encode("utf8"))
    >>> lab9.Browser().new_tab(url)
    Hello, world!

Note that you can print other data structures as well:

    >>> script = "console.log([2, 3, 4])"
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\n" + script.encode("utf8"))
    >>> lab9.Browser().new_tab(url)
    [2, 3, 4]

Let's test that variables work:

    >>> script = "var x = 'Hello!'; console.log(x)"
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\n" + script.encode("utf8"))
    >>> lab9.Browser().new_tab(url)
    Hello!

Next let's try to do two scripts:

    >>> url2 = 'http://test.test/js1'
    >>> url3 = 'http://test.test/js2'
    >>> html_page = "<script src=" + url2 + "></script>" + "<script src=" + url3 + "></script>"
    >>> test.socket.respond(str(url), b"HTTP/1.0 200 OK\r\n\r\n" + html_page.encode("utf8"))
    >>> test.socket.respond(str(url2), b"HTTP/1.0 200 OK\r\n\r\nvar x = 'Testing, testing';")
    >>> test.socket.respond(str(url3), b"HTTP/1.0 200 OK\r\n\r\nconsole.log(x);")
    >>> lab9.Browser().new_tab(url)
    Testing, testing

9.5 Returning Handles
---------------------

The `querySelectorAll` method is easiest to test by looking at the number of
matching nodes:

    >>> page = """<!doctype html>
    ... <div>
    ...   <p id=lorem>Lorem</p>
    ...   <p class=ipsum>Ipsum</p>
    ... </div>"""
    >>> test.socket.respond(str(url), b"HTTP/1.0 200 OK\r\n\r\n" + page.encode("utf8"))
    >>> b = lab9.Browser()
    >>> b.new_tab(url)
    >>> js = b.tabs[0].js
    >>> js.run("test", "document.querySelectorAll('div').length")
    1
    >>> js.run("test", "document.querySelectorAll('p').length")
    2
    >>> js.run("test", "document.querySelectorAll('html').length")
    1

That last query is finding an implicit tag. Complex queries are also supported

    >>> js.run("test", "document.querySelectorAll('html p').length")
    2
    >>> js.run("test", "document.querySelectorAll('html body div p').length")
    2
    >>> js.run("test", "document.querySelectorAll('body html div p').length")
    0

`querySelectorAll` should return `Node` objects:

    >>> js.run("test", "document.querySelectorAll('html')[0] instanceof Node")
    True

9.6 Wrapping Handles
--------------------

Once we have a `Node` object we can call `getAttribute`:

    >>> js.run("test", "document.querySelectorAll('p')[0].getAttribute('id')")
    'lorem'

Note that this is "live": as the page changes `querySelectorAll` gives new results:

    >>> b.tabs[0].nodes.children[0].children[0].children[0].attributes['id'] = 'blah'
    >>> js.run("test", "document.querySelectorAll('p')[0].getAttribute('id')")
    'blah'

9.7 Event Handling
------------------

Events are the trickiest thing to test here. There are two steps:
establish a listener and then trigger it. This helper function quashes
the return value for the first step; otherwise it differs by DukPy version:

    >>> def void(s): return

Let's do a basic test of adding an event listener and then triggering
it. I'll use the `div` element to test things:

    >>> div = b.tabs[0].nodes.children[0].children[0]
    >>> void(js.run("test", "var div = document.querySelectorAll('div')[0]"))
    >>> void(js.run("test", "div.addEventListener('test', function(e) { console.log('Listener ran!')})"))
    >>> js.dispatch_event("test", div) #doctest: +ELLIPSIS
    Listener ran!...

The `...` ignores `preventDefault` handling once you implement that.
    
Let's test each of our automatic event types. We'll need a new web page with a
link, a button, and an input area:

    >>> post = test.socket.respond_ok("http://test/post", "Submitted", method="POST", body="input=t")
    >>> url = test.socket.serve("""<!doctype html>
    ... <a href=page2>Click me!</a>
    ... <form action=/post>
    ...   <input name=input value=hi>
    ...   <button>Submit</button>
    ... </form>""")
    >>> b.new_tab(lab9.URL(url))
    >>> js = b.tabs[1].js

Now we're going test five event handlers: clicking on the link, clicking on the
input, typing into the input, clicking on the button, and submitting the form.

    >>> void(js.run("test", "var form = document.querySelectorAll('form')[0]"))
    >>> void(js.run("test", "var input = document.querySelectorAll('input')[0]"))
    >>> void(js.run("test", "var button = document.querySelectorAll('button')[0]"))

Note that the `input` element has a value of `hi`:

    >>> js.run("test", "input.getAttribute('value')")
    'hi'

We'll log on every event:

    >>> void(js.run("test", "input.addEventListener('click', " +
    ...     "function(e) { console.log('input clicked')})"))
    >>> void(js.run("test", "input.addEventListener('keydown', " +
    ...     "function(e) { console.log('input typed');})"))
    >>> void(js.run("test", "button.addEventListener('click', " +
    ...     "function(e) { console.log('button clicked')})"))
    >>> void(js.run("test", "form.addEventListener('submit', " +
    ...     "function(e) { console.log('form submitted');})"))

With these all set up, we need to do some clicking and typing to trigger these
events. The display list gives us coordinates for clicking.

    >>> lab9.print_tree(b.tabs[1].document)
     DocumentLayout()
       BlockLayout[block](x=13, y=18, width=774, height=30.0, node=<html>)
         BlockLayout[block](x=13, y=18, width=774, height=30.0, node=<body>)
           BlockLayout[inline](x=13, y=18, width=774, height=15.0, node=<a href="page2">)
             LineLayout(x=13, y=18, width=774, height=15.0)
               TextLayout(x=13, y=20.25, width=60, height=12, word=Click)
               TextLayout(x=85, y=20.25, width=36, height=12, word=me!)
           BlockLayout[inline](x=13, y=33.0, width=774, height=15.0, node=<form action="/post">)
             LineLayout(x=13, y=33.0, width=774, height=15.0)
               InputLayout(x=13, y=35.25, width=200, height=12, type=input)
               InputLayout(x=225, y=35.25, width=200, height=12, type=button text=Submit)

    >>> b.tabs[1].click(14, 40)
    input clicked
    >>> b.tabs[1].keypress('t')
    input typed
    >>> b.tabs[1].click(230, 40)
    button clicked
    form submitted
    >>> b.tabs[1].history #doctest: +NORMALIZE_WHITESPACE
    [URL(scheme=http, host=test, port=80, path='/page2'),
     URL(scheme=http, host=test, port=80, path='/post')]

This submits the form because we allowed the default action from
clicking a button---form submission.

We need to re-set-up the listeners since we reloaded the page, but
let's also test clicking the link:

    >>> b.tabs[1].go_back()
    >>> js = b.tabs[1].js
    >>> b.tabs[1].history #doctest: +NORMALIZE_WHITESPACE
    [URL(scheme=http, host=test, port=80, path='/page2')]
    >>> void(js.run("test", "var a = document.querySelectorAll('a')[0]"))
    >>> void(js.run("test", "a.addEventListener('click', " +
    ...     "function(e) { console.log('a clicked');})"))
    >>> b.tabs[1].click(14, 22)
    a clicked
    >>> b.tabs[1].history #doctest: +NORMALIZE_WHITESPACE
    [URL(scheme=http, host=test, port=80, path='/page2'),
     URL(scheme=http, host=test, port=80, path='/page2')]

Note that we navigated to a new page---that's because we allowed the
default action to occur.
