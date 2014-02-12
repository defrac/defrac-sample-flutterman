package com.defrac.sample.flutterman.visual;

import com.defrac.sample.flutterman.world.Constants;
import com.defrac.sample.flutterman.world.Sprites;
import com.defrac.sample.flutterman.world.Spritesheet;
import defrac.display.Image;

import javax.annotation.Nonnull;
import java.util.Random;

public final class FireworkParticle extends Image {
  @Nonnull
  private static final Random random = new Random(0xd3f24c);

  public float vx;
  public float vy;
  public float life;
  public float lifeDecrement;

  public FireworkParticle(@Nonnull final Spritesheet spritesheet) {
    super(spritesheet.get(Sprites.SPARKLE_0 + random.nextInt(4)));

    life = 0.0f;
    centerRegistrationPoint();
  }

  public void init(float x, float y, float scale, float a) {
    final float sin = (float)Math.sin(a);
    final float cos = (float)Math.cos(a);
    vx = cos * random.nextFloat() * 4.0f;
    vy = sin * random.nextFloat() * 4.0f;
    life = 1.0f;
    lifeDecrement = random.nextFloat() * 0.075f + 0.006125f;
    rotation((float) Math.PI * 0.25f * (random.nextFloat() - random.nextFloat()));
    moveTo(
        x + (random.nextFloat() - random.nextFloat()) * 32.0f * scale + cos * 32.0f * scale,
        y + (random.nextFloat() - random.nextFloat()) * 32.0f * scale + sin * 32.0f * scale);
    scaleTo(scale);
  }

  public void update(final float scale) {
    if(life > 0.0f) {
      vx += (random.nextFloat() - random.nextFloat()) * 0.01f * life;
      vy += (random.nextFloat() - random.nextFloat()) * 0.01f * life;
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
