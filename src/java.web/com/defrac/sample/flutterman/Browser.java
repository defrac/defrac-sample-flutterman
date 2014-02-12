package com.defrac.sample.flutterman;

import intrinsic.Toplevel;

import static defrac.lang.Bridge.toJSString;

/**
 *
 */
public final class Browser {
  public static boolean openWebsite() {
    Toplevel.window().open(
        toJSString("http://www.defrac.com/"),
        toJSString("_blank"));
    return true;
  }
}
