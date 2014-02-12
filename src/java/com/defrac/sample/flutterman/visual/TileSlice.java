package com.defrac.sample.flutterman.visual;

import com.defrac.sample.flutterman.world.Constants;
import com.defrac.sample.flutterman.world.Spritesheet;
import defrac.display.Image;
import defrac.display.Layer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class TileSlice extends Layer {
  // A TileSlice is one column of tiles in the world.
  // TileSlice objects form a single-linked-list
  // which makes it quite easy for us to create the
  // illusion of an endless-scrolling world since
  // we simply add the slice that drops off the left
  // corner to the end of the list.

  @Nullable
  public TileSlice next;

  @Nonnull
  public final int[] tiles = new int[Constants.HEIGHT];

  @Nonnull
  private final Image[] images;

  @Nonnull
  private final Spritesheet spritesheet;

  public boolean counted;

  public TileSlice(@Nonnull final Spritesheet spritesheet) {
    super(Constants.HEIGHT);

    this.spritesheet = spritesheet;

    images = new Image[Constants.HEIGHT];

    for(int i = 0; i < Constants.HEIGHT; ++i) {
      final Image image = addChild(new Image());
      image.y(Math.round(i * Spritesheet.TILE_HEIGHT));
      images[i] = image;
    }
  }

  public void adjustSize(final float width, final float height) {
    final float scaleX = width / (float)(Constants.WIDTH * Spritesheet.TILE_HEIGHT);
    final float scaleY = height / (float)(Constants.HEIGHT * Spritesheet.TILE_HEIGHT);
    final float scale = Math.max(scaleX, scaleY);
    final float scaledHeight = Spritesheet.TILE_HEIGHT * scale;
    float offsetY = 0.0f;

    for(int i = 0; i < Constants.HEIGHT; ++i) {
      images[i].
          scaleTo(scale).
          y(Math.round(offsetY));
      offsetY += scaledHeight;
    }
  }

  public void updateTiles() {
    for(int i = 0; i < Constants.HEIGHT; ++i) {
      images[i].texture(spritesheet.get(tiles[i]));
    }
  }
}
