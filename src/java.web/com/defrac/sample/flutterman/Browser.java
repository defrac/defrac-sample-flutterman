package com.defrac.sample.flutterman;

import defrac.web.Toplevel;

/**
 *
 */
public final class Browser {
  public static boolean openWebsite() {
    Toplevel.window().open(
        "http://www.defrac.com/",
        "_blank");
    return true;
  }
}
