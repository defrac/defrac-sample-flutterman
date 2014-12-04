package com.defrac.sample.flutterman.util;

import static defrac.web.Toplevel.window;

/**
 *
 */
public final class BrowserUtilImpl {
  public static boolean openWebsite(final String url) {
    window().open(url, "_blank");
    return true;
  }

  private BrowserUtilImpl() {}
}
