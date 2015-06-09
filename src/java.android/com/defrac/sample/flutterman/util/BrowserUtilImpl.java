package com.defrac.sample.flutterman.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import defrac.annotation.Injector;
import defrac.ui.ActivityContext;

/**
 *
 */
@Injector("com.defrac.sample.flutterman.util.BrowserUtil")
public final class BrowserUtilImpl {
  public static boolean openWebsite(final String url) {
    final Context context = ActivityContext.get();

    if(context == null) {
      return false;
    }

    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    context.startActivity(i);
    return true;
  }

  private BrowserUtilImpl() {}
}
