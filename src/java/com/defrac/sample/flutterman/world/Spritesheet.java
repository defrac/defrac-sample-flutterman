package com.defrac.sample.flutterman.world;

import defrac.display.Texture;
import defrac.display.TextureData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class Spritesheet {
  public static final int TILE_WIDTH = 21;
  public static final int TILE_HEIGHT = 21;
  private static final int TILE_GAP = 2;

  @Nonnull
  private final TextureData data;

  @Nonnull
  private final Texture[] sprites = new Texture[Sprites.MAX_SPRITES];

  public Spritesheet(@Nonnull final TextureData data) {
    this.data = data;
    createTextures();
  }

  @Nonnull
  private Texture newTexture(int x, int y) {
    final int u = TILE_GAP + x * (TILE_WIDTH + TILE_GAP);
    final int v = TILE_GAP + y * (TILE_HEIGHT + TILE_GAP);
    return new Texture(data, u, v, TILE_WIDTH, TILE_HEIGHT);
  }

  private void createTextures() {
    sprites[Sprites.EARTH] = newTexture(2, 5);
    sprites[Sprites.GRASS] = newTexture(3, 4);
    sprites[Sprites.GRASS_BUMP_UP] = newTexture(6, 4);
    sprites[Sprites.GRASS_BUMP_DOWN] = newTexture(7, 4);
    sprites[Sprites.GRASS_BUMP_UP_BELOW] = newTexture(6, 5);
    sprites[Sprites.GRASS_BUMP_DOWN_BELOW] = newTexture(7, 5);
    sprites[Sprites.GREEN_THING_HEAD] = newTexture(16, 6);
    sprites[Sprites.GREEN_THING_BODY] = newTexture(16, 7);
    sprites[Sprites.ANVIL] = newTexture(12, 2);
    sprites[Sprites.CHAIN] = newTexture(13, 2);
    sprites[Sprites.LOLLY_STICK] = newTexture(26, 21);
    sprites[Sprites.LOLLY_0] = newTexture(26, 20);
    sprites[Sprites.LOLLY_1] = newTexture(27, 20);
    sprites[Sprites.LOLLY_2] = newTexture(27, 19);
    sprites[Sprites.LOLLY_3] = newTexture(26, 19);
    sprites[Sprites.MELTED_LOLLY_0] = newTexture(9, 20);
    sprites[Sprites.MELTED_LOLLY_1] = newTexture(13, 20);
    sprites[Sprites.CANDY_HEAD_0] = newTexture(6, 20);
    sprites[Sprites.CANDY_BODY_0] = newTexture(8, 20);
    sprites[Sprites.CANDY_HEAD_1] = newTexture(10, 20);
    sprites[Sprites.CANDY_BODY_1] = newTexture(12, 20);
    sprites[Sprites.HERO_MOVING_0] = newTexture(26, 0);
    sprites[Sprites.HERO_MOVING_1] = newTexture(27, 0);
    sprites[Sprites.HERO_UPWARDS] = newTexture(21, 0);
    sprites[Sprites.HERO_SAD] = newTexture(23, 0);
    sprites[Sprites.DIGIT_0] = newTexture(14, 14);
    sprites[Sprites.DIGIT_1] = newTexture(15, 14);
    sprites[Sprites.DIGIT_2] = newTexture(16, 14);
    sprites[Sprites.DIGIT_3] = newTexture(17, 14);
    sprites[Sprites.DIGIT_4] = newTexture(18, 14);
    sprites[Sprites.DIGIT_5] = newTexture(19, 14);
    sprites[Sprites.DIGIT_6] = newTexture(13, 15);
    sprites[Sprites.DIGIT_7] = newTexture(14, 15);
    sprites[Sprites.DIGIT_8] = newTexture(15, 15);
    sprites[Sprites.DIGIT_9] = newTexture(16, 15);
    sprites[Sprites.BOX_0] = newTexture(10, 6);
    sprites[Sprites.BOX_1] = newTexture(11, 6);
    sprites[Sprites.BOX_2] = newTexture(12, 6);
    sprites[Sprites.BOX_3] = newTexture(13, 6);
    sprites[Sprites.HEART] = newTexture(13, 12);
    sprites[Sprites.SPARKLE_0] = newTexture(26, 28);
    sprites[Sprites.SPARKLE_1] = newTexture(27, 28);
    sprites[Sprites.SPARKLE_2] = newTexture(26, 29);
    sprites[Sprites.SPARKLE_3] = newTexture(27, 29);
    sprites[Sprites.DECORATION_0] = newTexture(5, 15);
    sprites[Sprites.DECORATION_1] = newTexture(6, 15);
    sprites[Sprites.DECORATION_2] = newTexture(7, 15);
    sprites[Sprites.DECORATION_3] = newTexture(8, 15);
    sprites[Sprites.DECORATION_4] = newTexture(9, 15);
    sprites[Sprites.DECORATION_5] = newTexture(10, 15);
    sprites[Sprites.DECORATION_6] = newTexture(16, 0);
    sprites[Sprites.DECORATION_7] = newTexture(17, 0);
    sprites[Sprites.DECORATION_8] = newTexture(18, 0);
    sprites[Sprites.DECORATION_9] = newTexture(16, 1);
    sprites[Sprites.DECORATION_10] = newTexture(17, 1);
    sprites[Sprites.DECORATION_11] = newTexture(13, 21);
    sprites[Sprites.DECORATION_12] = newTexture(17, 3);
    sprites[Sprites.DECORATION_13] = newTexture(18, 3);
    sprites[Sprites.DECORATION_14] = newTexture(13, 22);

  }

  @Nullable
  public Texture get(final int id) {
    return Sprites.NONE == id ? null : sprites[id];
  }
}
