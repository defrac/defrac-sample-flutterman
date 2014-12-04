package com.defrac.sample.flutterman.util;

import defrac.ios.uikit.UIApplication;

import static defrac.ios.foundation.NSURL.URLWithString;

/**
 *
 */
public final class BrowserUtilImpl {
  public static boolean openWebsite(final String url) {
    return UIApplication.sharedApplication().openURL(URLWithString(url));
  }

  private BrowserUtilImpl() {}
}
