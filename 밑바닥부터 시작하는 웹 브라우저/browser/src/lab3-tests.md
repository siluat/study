Tests for WBE Chapter 3
=======================

Chapter 3 (Formatting Text) adds on font metrics and simple font styling via
HTML tags. This file contains tests for the additional functionality.

    >>> import test
    >>> _ = test.socket.patch().start()
    >>> _ = test.ssl.patch().start()
    >>> import lab3

Testing Mocks
-------------

This section handles a refactor that's introduced later in in Section
3.5; you can ignore it. Basically, if you've already created a
`Layout` class it creates a stub `layout` function that calls it and
undoes a lot of later changes like font changes, leading, and so on,
for testing previous sections.

    >>> lab3.WIDTH
    800
    >>> if not hasattr(lab3, "layout"):
    ...     def layout(text):
    ...         if isinstance(text, str):
    ...             return [(x, int(y - 2.25), w) for x, y, w, font
    ...                 in lab3.Layout(lab3.lex(text)).display_list]
    ...         else:
    ...             return [(x, int(y - 2.25), w, font) for x, y, w, font
    ...                 in lab3.Layout(text).display_list]
    ...     lab3.layout = layout
    ... else:
    ...      old_layout = lab3.layout
    ...      def layout(text):
    ...          try:
    ...              return old_layout(text)
    ...          except AttributeError as e:
    ...              expected_error = "'str' object has no attribute 'tag'"
    ...              if str(e) == expected_error:
    ...                  return old_layout(lab3.lex(text))
    ...              else:
    ...                  raise e
    ...      lab3.layout = layout

Note that these test doesn't use real `tkinter` fonts, but rather a
mock font that has faked metrics; in this font every character is N
pixels wide, where N is the font size.

3.3 Word by Word
----------------

The `layout` display list should now output one word at a time:

    >>> lab3.layout("abc")
    [(13, 18, 'abc')]
    >>> lab3.layout("abc def")
    [(13, 18, 'abc'), (61, 18, 'def')]
    
Different words should have different lengths:

    >>> lab3.layout("a bb ccc dddd")
    [(13, 18, 'a'), (37, 18, 'bb'), (73, 18, 'ccc'), (121, 18, 'dddd')]

Line breaking still works:

    >>> lab3.WIDTH
    800
    >>> lab3.WIDTH = 70
    >>> lab3.layout("a b c") #doctest: +NORMALIZE_WHITESPACE
    [(13, 18, 'a'), (37, 18, 'b'), (13, 33.0, 'c')]
    >>> lab3.WIDTH = 800


Note that the step sizes are 24, 36, and 48 pixels; that's because
it's measuring 2, 3, and 4 letters---note the space character!
