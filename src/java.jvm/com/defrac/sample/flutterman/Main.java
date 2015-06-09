package com.defrac.sample.flutterman;

import defrac.ui.FrameBuilder;

/**
 *
 */
public final class Main {
  public static void main(String[] args) {
    FrameBuilder.
        forScreen(new FluttermanScreen()).
        width(1344).
        height(756).
        title("Flutterman").
        show();
  }
}
