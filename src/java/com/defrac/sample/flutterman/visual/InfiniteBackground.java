package com.defrac.sample.flutterman.visual;

import defrac.display.Image;
import defrac.display.Layer;
import defrac.display.Texture;
import defrac.display.TextureData;

import javax.annotation.Nonnull;

public final class InfiniteBackground extends Layer {
  private final Image firstSlice, secondSlice;
  private float scaledWidth;

  public InfiniteBackground(@Nonnull final TextureData textureData) {
    super(2);

    final Texture texture = new Texture(textureData, 0, 0, 231, 63);
    firstSlice = addChild(new Image(texture));
    secondSlice = addChild(new Image(texture));
  }

  public void resizeTo(final float width, final float height) {
    final float scaleX = width / firstSlice.width();
    final float scaleY = height / firstSlice.height();
    final float scale = Math.max(scaleX, scaleY);
    firstSlice.scaleTo(scale);
    secondSlice.scaleTo(scale);
    this.scaledWidth = firstSlice.width() * scale;
  }

  public void render(float x) {
    float localXOffset = x;

    while(localXOffset > scaledWidth) {
      localXOffset -= scaledWidth;
    }

    firstSlice.x(-localXOffset);
    secondSlice.x(-localXOffset + scaledWidth);
  }
}
