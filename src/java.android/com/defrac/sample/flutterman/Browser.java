package com.defrac.sample.flutterman;

import android.content.Intent;
import android.net.Uri;

/**
 *
 */
public final class Browser {
  public static boolean openWebsite() {
    String url = "http://www.defrac.com/";
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    defrac.app.DefracActivity.current().startActivity(i);
    return true;
  }
}
