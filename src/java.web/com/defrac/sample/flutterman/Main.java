package com.defrac.sample.flutterman;

import defrac.ui.FrameBuilder;

/**
 *
 */
public final class Main {
  public static void main(String[] args) {
    FrameBuilder.
        forScreen(new FluttermanScreen()).
        containerById("screen").
        show();
  }
}
