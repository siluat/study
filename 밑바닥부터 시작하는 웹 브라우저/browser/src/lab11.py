import sys
import ctypes
import sdl2
import skia
import wbetools
from lab2 import WIDTH, HEIGHT
from lab8 import Browser, Chrome
from lab10 import URL

FONTS = {}

def get_font(size, weight, style):
    key = (weight, style)
    if key not in FONTS:
        if weight == "bold":
            skia_weight = skia.FontStyle.kBold_Weight
        else:
            skia_weight = skia.FontStyle.kNormal_Weight
        if style == "italic":
            skia_style = skia.FontStyle.kItalic_Slant
        else:
            skia_style = skia.FontStyle.kUpright_Slant
        skia_width = skia.FontStyle.kNormal_Width
        style_info = \
            skia.FontStyle(skia_weight, skia_width, skia_style)
        font = skia.Typeface('Arial', style_info)
        FONTS[key] = font
    return skia.Font(FONTS[key], size)

NAMED_COLORS = {
    "black": "#000000",
    "gray":  "#808080",
    "white": "#ffffff",
    "red":   "#ff0000",
    "green": "#00ff00",
    "blue":  "#0000ff",
    "lightblue": "#add8e6",
    "lightgreen": "#90ee90",
    "orange": "#ffa500",
    "orangered": "#ff4500",
}

def parse_color(color):
    if color.startswith("#") and len(color) == 7:
        r = int(color[1:3], 16)
        g = int(color[3:5], 16)
        b = int(color[5:7], 16)
        return skia.Color(r, g, b)
    elif color.startswith("#") and len(color) == 9:
        r = int(color[1:3], 16)
        g = int(color[3:5], 16)
        b = int(color[5:7], 16)
        a = int(color[7:9], 16)
        return skia.Color(r, g, b, a)
    elif color in NAMED_COLORS:
        return parse_color(NAMED_COLORS[color])
    else:
        return skia.ColorBLACK

def linespace(font):
    metrics = font.getMetrics()
    return metrics.fDescent - metrics.fAscent

@wbetools.patch(Chrome)
class Chrome:
    def __init__(self, browser):
        self.browser = browser
        self.focus = None
        self.address_bar = ""

        self.font = get_font(20, "normal", "roman")
        self.font_height = linespace(self.font)

        self.padding = 5
        self.tabbar_top = 0
        self.tabbar_bottom = self.font_height + 2*self.padding

        plus_width = self.font.measureText("+") + 2*self.padding
        self.newtab_rect = skia.Rect.MakeLTRB(
            self.padding, self.padding,
            self.padding + plus_width,
            self.padding + self.font_height)

        self.urlbar_top = self.tabbar_bottom
        self.urlbar_bottom = self.urlbar_top + \
            self.font_height + 2*self.padding

        back_width = self.font.measureText("<") + 2*self.padding
        self.back_rect = skia.Rect.MakeLTRB(
            self.padding,
            self.urlbar_top + self.padding,
            self.padding + back_width,
            self.urlbar_bottom - self.padding)

        self.address_rect = skia.Rect.MakeLTRB(
            self.back_rect.top() + self.padding,
            self.urlbar_top + self.padding,
            WIDTH - self.padding,
            self.urlbar_bottom - self.padding)

        self.bottom = self.urlbar_bottom

    def paint(self):
        cmds = []
        # cmds.append(DrawLine(
        #     0, self.bottom, WIDTH,
        #     self.bottom, "black", 1))

        # cmds.append(DrawOutline(self.newtab_rect, "black", 1))
        # cmds.append(DrawText(
        #     self.newtab_rect.left() + self.padding,
        #     self.newtab_rect.top(),
        #     "+", self.font, "black"))

        # for i, tab in enumerate(self.browser.tabs):
        #     bounds = self.tab_rect(i)
        #     cmds.append(DrawLine(
        #         bounds.left(), 0, bounds.left(), bounds.bottom(),
        #         "black", 1))
        #     cmds.append(DrawLine(
        #         bounds.right(), 0, bounds.right(), bounds.bottom(),
        #         "black", 1))
        #     cmds.append(DrawText(
        #         bounds.left() + self.padding, bounds.top() + self.padding,
        #         "Tab {}".format(i), self.font, "black"))

        #     if tab == self.browser.active_tab:
        #         cmds.append(DrawLine(
        #             0, bounds.bottom(), bounds.left(), bounds.bottom(),
        #             "black", 1))
        #         cmds.append(DrawLine(
        #             bounds.right(), bounds.bottom(), WIDTH, bounds.bottom(),
        #             "black", 1))

        # cmds.append(DrawOutline(self.back_rect, "black", 1))
        # cmds.append(DrawText(
        #     self.back_rect.left() + self.padding,
        #     self.back_rect.top(),
        #     "<", self.font, "black"))

        # cmds.append(DrawOutline(self.address_rect, "black", 1))
        # if self.focus == "address bar":
        #     cmds.append(DrawText(
        #         self.address_rect.left() + self.padding,
        #         self.address_rect.top(),
        #         self.address_bar, self.font, "black"))
        #     w = self.font.measureText(self.address_bar)
        #     cmds.append(DrawLine(
        #         self.address_rect.left() + self.padding + w,
        #         self.address_rect.top(),
        #         self.address_rect.left() + self.padding + w,
        #         self.address_rect.bottom(),
        #         "red", 1))
        # else:
        #     url = str(self.browser.active_tab.url)
        #     cmds.append(DrawText(
        #         self.address_rect.left() + self.padding,
        #         self.address_rect.top(),
        #         url, self.font, "black"))

        return cmds        

@wbetools.patch(Browser)
class Browser:
    def __init__(self):
        self.chrome = Chrome(self)

        self.sdl_window = sdl2.SDL_CreateWindow(b"Browser",
            sdl2.SDL_WINDOWPOS_CENTERED, sdl2.SDL_WINDOWPOS_CENTERED,
            WIDTH, HEIGHT, sdl2.SDL_WINDOW_SHOWN)
        self.root_surface = skia.Surface.MakeRaster(
            skia.ImageInfo.Make(
                WIDTH, HEIGHT,
                ct=skia.kRGBA_8888_ColorType,
                at=skia.kUnpremul_AlphaType))

        if sdl2.SDL_BYTEORDER == sdl2.SDL_BIG_ENDIAN:
            self.RED_MASK = 0xff000000
            self.GREEN_MASK = 0x00ff0000
            self.BLUE_MASK = 0x0000ff00
            self.ALPHA_MASK = 0x000000ff
        else:
            self.RED_MASK = 0x000000ff
            self.GREEN_MASK = 0x0000ff00
            self.BLUE_MASK = 0x00ff0000
            self.ALPHA_MASK = 0xff000000

    def new_tab(self, url):
        pass

    def draw(self):
        canvas = self.root_surface.getCanvas()
        canvas.clear(skia.ColorWHITE)

        for cmd in self.chrome.paint():
            cmd.execute(0, canvas)

        skia_image = self.root_surface.makeImageSnapshot()
        skia_bytes = skia_image.tobytes()

        depth = 32 # Bits per pixel
        pitch = 4 * WIDTH # Bytes per row
        sdl_surface = sdl2.SDL_CreateRGBSurfaceFrom(
            skia_bytes, WIDTH, HEIGHT, depth, pitch,
            self.RED_MASK, self.GREEN_MASK,
            self.BLUE_MASK, self.ALPHA_MASK)

        rect = sdl2.SDL_Rect(0, 0, WIDTH, HEIGHT)
        window_surface = sdl2.SDL_GetWindowSurface(self.sdl_window)
        # SDL_BlitSurface is what actually does the copy.
        sdl2.SDL_BlitSurface(sdl_surface, rect, window_surface, rect)
        sdl2.SDL_UpdateWindowSurface(self.sdl_window)

    def handle_quit(self):
        sdl2.SDL_DestroyWindow(self.sdl_window)

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
