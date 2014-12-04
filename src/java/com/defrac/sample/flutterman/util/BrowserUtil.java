package com.defrac.sample.flutterman.util;

import defrac.annotation.Delegate;

/**
 *
 */
@Delegate("com.defrac.sample.flutterman.util.BrowserUtilImpl")
public final class BrowserUtil {
  public static native boolean openWebsite(final String url);

  private BrowserUtil() {}
}
