package com.defrac.sample.flutterman.util;

import defrac.annotation.Injector;

import static defrac.web.Toplevel.window;

/**
 *
 */
@Injector("com.defrac.sample.flutterman.util.BrowserUtil")
public final class BrowserUtilImpl {
  public static boolean openWebsite(final String url) {
    window().open(url, "_blank");
    return true;
  }

  private BrowserUtilImpl() {}
}
