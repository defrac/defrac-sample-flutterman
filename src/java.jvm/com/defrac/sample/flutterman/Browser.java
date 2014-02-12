package com.defrac.sample.flutterman;

import java.net.URI;

/**
 *
 */
public final class Browser {
  public static boolean openWebsite() {
    try {
      java.awt.Desktop.getDesktop().browse(new URI("http://www.defrac.com/"));
      return true;
    } catch(final Throwable t) {
      return false;
    }
  }
}
