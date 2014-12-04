package com.defrac.sample.flutterman.util;

import java.net.URI;

/**
 *
 */
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
