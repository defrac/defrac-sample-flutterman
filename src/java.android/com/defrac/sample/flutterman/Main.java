package com.defrac.sample.flutterman;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import defrac.dni.Activity;
import defrac.dni.IntentFilter;
import defrac.dni.UsesSdk;
import defrac.ui.Screen;
import defrac.ui.ScreenActivity;
import defrac.ui.View;

@Activity(
    label = "Flutterman",
    screenOrientation = "landscape",
    filter = @IntentFilter(action = Intent.ACTION_MAIN, category = Intent.CATEGORY_LAUNCHER))
@UsesSdk(minSdkVersion = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public final class Main extends ScreenActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);

    super.onCreate(savedInstanceState);
  }

  @Override
  protected Screen createScreen() {
    return new FluttermanScreen();
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);

    if (hasFocus) {
      if (android.os.Build.VERSION.SDK_INT >= 19) {
        getWindow().getDecorView().setSystemUiVisibility(
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN |
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
      }
    }
  }
}
