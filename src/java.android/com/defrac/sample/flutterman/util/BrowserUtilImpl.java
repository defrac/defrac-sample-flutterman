package com.defrac.sample.flutterman.util;

import android.content.Intent;
import android.net.Uri;

/**
 *
 */
public final class BrowserUtilImpl {
  public static boolean openWebsite(final String url) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    defrac.app.DefracActivity.current().startActivity(i);
    return true;
  }

  private BrowserUtilImpl() {}
}
