package com.defrac.sample.flutterman;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;

import defrac.dni.Activity;
import defrac.dni.IntentFilter;
import defrac.ui.Screen;
import defrac.ui.ScreenActivity;

@Activity(
    label = "Flutterman",
    screenOrientation = "landscape",
    filter = @IntentFilter(action = Intent.ACTION_MAIN, category = Intent.CATEGORY_LAUNCHER))
public final class Main extends ScreenActivity {
  @Override
  protected void onBeforeCreateContentView() {
    super.onBeforeCreateContentView();

    requestWindowFeature(Window.FEATURE_NO_TITLE);
    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
      }
    }
  }
}
