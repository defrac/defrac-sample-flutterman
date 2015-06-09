package com.defrac.sample.flutterman.util;

import defrac.annotation.Injector;
import defrac.ios.uikit.UIApplication;

import static defrac.ios.foundation.NSURL.URLWithString;

/**
 *
 */
@Injector("com.defrac.sample.flutterman.util.BrowserUtil")
public final class BrowserUtilImpl {
  public static boolean openWebsite(final String url) {
    return UIApplication.sharedApplication().openURL(URLWithString(url));
  }

  private BrowserUtilImpl() {}
}
