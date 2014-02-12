package com.defrac.sample.flutterman.visual;

import com.defrac.sample.flutterman.world.Sprites;
import com.defrac.sample.flutterman.world.Spritesheet;
import defrac.display.Image;
import defrac.display.Texture;

import javax.annotation.Nonnull;

public final class Hero extends Image {

  private final Texture[] animationFrames;
  private final Texture upwardsFrame;

  private int currentFrame = 0;

  public float vy = 0.0f;

  public float rotationDestination = 0.0f;
  public float rotationCurrent = 0.0f;

  public Hero(@Nonnull final Spritesheet spritesheet) {
    super(spritesheet.get(Sprites.HERO_MOVING_0));

    animationFrames = new Texture[] {
        spritesheet.get(Sprites.HERO_MOVING_0),
        spritesheet.get(Sprites.HERO_MOVING_1),
    };

    upwardsFrame = spritesheet.get(Sprites.HERO_UPWARDS);

    centerRegistrationPoint();
  }

  public void flutter() {
    ++currentFrame;

    if(currentFrame == animationFrames.length) {
      currentFrame = 0;
    }
  }

  public void update() {
    if(vy < 0.0f) {
      rotationDestination = (float)Math.PI * -0.03125f;
    } else if(vy > 0.0f) {
      rotationDestination = (float)Math.PI * 0.0625f;
    }

    texture(vy < 0.0f ? upwardsFrame : animationFrames[currentFrame]);
    rotation(rotationCurrent = rotationCurrent + (rotationDestination - rotationCurrent) * 0.1f);
  }
}
