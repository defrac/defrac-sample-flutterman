package com.defrac.sample.flutterman.visual;

import com.defrac.sample.flutterman.world.Constants;
import com.defrac.sample.flutterman.world.Sprites;
import com.defrac.sample.flutterman.world.Spritesheet;
import defrac.display.Image;

import javax.annotation.Nonnull;
import java.util.Random;

public final class HeartParticle extends Image {
  @Nonnull
  private static final Random random = new Random(0xd3f24c);

  public float vx;
  public float vy;
  public float life;
  public float lifeDecrement;

  public HeartParticle(@Nonnull final Spritesheet spritesheet) {
    super(spritesheet.get(Sprites.HEART));
  }

  public void init(float width, float height, float scale) {
    vx = (random.nextFloat() - random.nextFloat()) * 10.0f;
    vy = random.nextFloat() * -10.0f;
    life = 1.0f;
    lifeDecrement = random.nextFloat() * 0.075f + 0.006125f;
    rotation((float) Math.PI * 0.25f * (random.nextFloat() - random.nextFloat()));
    moveTo(width * 0.5f - width() * scale * 0.5f, height + 16.0f * scale);
    scaleTo(scale);
    visible(true);
  }

  public void update(final float scale) {
    if(life > 0.0f) {
      vy += Constants.GRAVITY;
      vy *= Constants.FRICTION;
      vx *= Constants.FRICTION;
      moveBy(vx * scale, vy * scale);
      scaleTo(scale * life);
      life -= lifeDecrement;
    } else {
      visible(false);
    }
  }
}
