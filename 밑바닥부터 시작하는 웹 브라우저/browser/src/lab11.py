import sys
import ctypes
import sdl2
import wbetools
from lab2 import WIDTH, HEIGHT
from lab8 import Browser
from lab10 import URL

@wbetools.patch(Browser)
class Browser:
    def __init__(self):
        self.sdl_window = sdl2.SDL_CreateWindow(b"Browser",
            sdl2.SDL_WINDOWPOS_CENTERED, sdl2.SDL_WINDOWPOS_CENTERED,
            WIDTH, HEIGHT, sdl2.SDL_WINDOW_SHOWN)

    def new_tab(self, url):
        pass

    def draw(self):
        pass

    def handle_quit(self):
        sdl2.SDL_DestoryWindow(self.sdl_window)

def mainloop(browser):
    event = sdl2.SDL_Event()
    while True:
        while sdl2.SDL_PollEvent(ctypes.byref(event)) != 0:
            if event.type == sdl2.SDL_QUIT:
                browser.handle_quit()
                sdl2.SDL_Quit()
                sys.exit()

if __name__ == "__main__":
    sdl2.SDL_Init(sdl2.SDL_INIT_EVENTS)
    browser = Browser()
    browser.new_tab(URL(sys.argv[1]))
    browser.draw()
    mainloop(browser)
