package com.defrac.sample.flutterman.visual;

import com.defrac.sample.flutterman.world.Sprites;
import com.defrac.sample.flutterman.world.Spritesheet;
import defrac.display.Image;
import defrac.display.Layer;
import defrac.display.Texture;

import javax.annotation.Nonnull;

public final class Score extends Layer {
  // Simple three-digit display

  @Nonnull
  private final Image digit0, digit1, digit2;
  @Nonnull
  private final Texture[] textures;

  public Score(@Nonnull final Spritesheet spritesheet) {
    super(3);

    textures = new Texture[] {
        spritesheet.get(Sprites.DIGIT_0),
        spritesheet.get(Sprites.DIGIT_1),
        spritesheet.get(Sprites.DIGIT_2),
        spritesheet.get(Sprites.DIGIT_3),
        spritesheet.get(Sprites.DIGIT_4),
        spritesheet.get(Sprites.DIGIT_5),
        spritesheet.get(Sprites.DIGIT_6),
        spritesheet.get(Sprites.DIGIT_7),
        spritesheet.get(Sprites.DIGIT_8),
        spritesheet.get(Sprites.DIGIT_9)
    };

    digit0 = addChild(new Image(textures[0]));
    digit1 = addChild(new Image(textures[0]));
    digit2 = addChild(new Image(textures[0]));
  }

  @Nonnull
  public Score resizeTo(final float scale) {
    digit0.scaleTo(scale);
    digit1.scaleTo(scale);
    digit2.scaleTo(scale);

    final float y = -4.0f * scale;
    digit0.moveTo(-6.0f * scale, y);
    digit1.moveTo( 5.0f * scale, y);
    digit2.moveTo(16.0f * scale, y);
    return this;
  }

  public void reset() {
    digit0.texture(textures[0]);
    digit1.texture(textures[0]);
    digit2.texture(textures[0]);
  }

  public void show(final int value) {
    final int d0 = value / 100;
    final int value2 = value - (d0 << 6) - (d0 << 5) - (d0 << 2);
    final int d1 = value2 / 10;
    final int d2 = value2 - (d1 << 3) - (d1 << 1);
    digit0.texture(textures[d0]);
    digit1.texture(textures[d1]);
    digit2.texture(textures[d2]);
  }
}
