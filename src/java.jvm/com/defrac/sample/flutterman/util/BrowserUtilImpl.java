package com.defrac.sample.flutterman.util;

import defrac.annotation.Delegator;

import java.net.URI;

/**
 *
 */
@Delegator("com.defrac.sample.flutterman.util.BrowserUtil")
public final class BrowserUtilImpl {
  public static boolean openWebsite(final String url) {
    try {
      java.awt.Desktop.getDesktop().browse(URI.create(url));
      return true;
    } catch(final Exception exception) {
      return false;
    }
  }

  private BrowserUtilImpl() {}
}
