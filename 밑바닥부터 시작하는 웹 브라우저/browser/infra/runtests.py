#!/usr/bin/env python

import doctest
import os
import json
import importlib
from unittest import mock

try:
    from blessings import Terminal
except ImportError:
    class Terminal:
        def bold(self, s): return s
        def red(self, s): return s
        def green(self, s): return s

def is_our_module(mod):
    if not hasattr(mod, "__file__"): return False
    file = mod.__file__
    return file and file.endswith("py") and \
        os.path.realpath(file).startswith(os.getcwd())

def reload_module(mod):
    assert is_our_module(mod), "Can't reload a builtin module"
    # To reload the module we need to do two steps:
    # - First, clear all current definitions
    # - Second, reevaluate the module to reload with new definitions
    for attr in dir(mod):
        if attr not in ('__name__', '__file__'):
            delattr(mod, attr)
    importlib.reload(mod)        

def run_tests(chapter, file_name):
    failure, count = doctest.testfile(os.path.abspath(file_name), module_relative=False)

    # This ugly code reloads all of our modules from scratch, in case
    # a test makes a mutation to a global for some reason
    our_mods = sorted([mod for mod in sys.modules.values() if is_our_module(mod)], key=lambda x: (len(x.__name__), x.__name__))
    for mod in our_mods:
        reload_module(mod)
    mock.patch.stopall()
        
    return failure, count

if __name__ == "__main__":
    import sys, argparse
    argparser = argparse.ArgumentParser()
    argparser.add_argument("config", type=str)
    argparser.add_argument("--chapter", type=str)
    args = argparser.parse_args()

    with open(args.config) as f:
        data = json.load(f)
        chapters = data["chapters"]

    src_path = os.path.abspath("src/")
    os.chdir(src_path)
    sys.path.insert(0, src_path)

    t = Terminal()
    results = {}
    failures = 0
    for chapter, metadata in data["chapters"].items():
        if args.chapter and args.chapter != "all" and chapter != args.chapter: continue
        print(f"{t.bold(chapter)}: Running tests")
        for key, value in metadata.items():
            if key == "tests":
                print(f"  {t.bold(value)}: Testing {chapter}...", end=" ")
                results[value] = run_tests(chapter, value)
                if not results[value][0]: print(t.green("pass"))
        failures += sum([failures for failures, count in results.values()])

    if not results:
        if args.chapter:
            print(f"Could not find chapter {args.chapter}")
            print("  Extant chapters:", ", ".join(data["chapters"].keys()))
        elif args.key:
            print(f"Could not find key {args.key}")
            key_sets = [set(list(metadata.keys())) for chapter, metadata in data["chapters"].items()]
            keys = set([]).union(*key_sets) - set(["disabled"])
            print("  Extant chapters:", ", ".join(keys))
        sys.exit(-1)

    total = sum([count for failures, count in results.values()])
    if failures:
        print(f"Failed {failures} of {total} tests")
        sys.exit(failures)
