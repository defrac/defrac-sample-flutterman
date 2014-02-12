package com.defrac.sample.flutterman.world;

import defrac.geom.Rectangle;

import javax.annotation.Nonnull;

/**
 *
 */
public final class Collisions {
  @Nonnull
  private static final Rectangle customAABB = new Rectangle();

  // Here we take some custom AABB dimensions into account
  // or check if we actually hit a triangle since it would
  // look quite stupid if we check just for the AABB
  public static boolean is(
      final int spriteId,
      final Rectangle heroAABB,
      final Rectangle tileAABB,
      final float scale) {
    switch(spriteId) {
      case Sprites.GREEN_THING_BODY:
      case Sprites.GREEN_THING_HEAD:
        customAABB.x = tileAABB.x + scale * 3.0f;
        customAABB.y = tileAABB.y;
        customAABB.width = 15.0f * scale;
        customAABB.height = tileAABB.height;
        break;
      case Sprites.LOLLY_STICK:
        customAABB.x = tileAABB.x + scale * 9.0f;
        customAABB.y = tileAABB.y;
        customAABB.width = 3.0f * scale;
        customAABB.height = tileAABB.height;
        break;
      case Sprites.MELTED_LOLLY_0:
      case Sprites.MELTED_LOLLY_1:
        customAABB.x = tileAABB.x + scale * 5.0f;
        customAABB.y = tileAABB.y + scale * 4.0f;
        customAABB.width =
        customAABB.height = 16.0f * scale;
        break;
      case Sprites.GRASS_BUMP_UP: {
        // test if bottom-right is inside triangle
        final float left = tileAABB.x;
        final float right = left + tileAABB.width;
        final float top = tileAABB.y;
        final float bottom = top + tileAABB.height;
        return pointIsInTriangle(
            heroAABB.x + heroAABB.width,
            heroAABB.y + heroAABB.height,
            left, bottom,
            right, top,
            right, bottom);
      }
      case Sprites.GRASS_BUMP_DOWN: {
        // test if bottom-left is inside triangle
        final float left = tileAABB.x;
        final float right = left + tileAABB.width;
        final float top = tileAABB.y;
        final float bottom = top + tileAABB.height;
        return pointIsInTriangle(
            heroAABB.x,
            heroAABB.y + heroAABB.height,
            left, bottom,
            left, top,
            right, bottom);
      }
      case Sprites.CHAIN:
        customAABB.x = tileAABB.x + scale * 7.0f;
        customAABB.y = tileAABB.y;
        customAABB.width = 7.0f * scale;
        customAABB.height = tileAABB.height;
        break;
      default:
        return true;
    }

    return heroAABB.intersects(customAABB);
  }

  private static boolean pointIsInTriangle(final float px, final float py,
                                           final float t1x, final float t1y,
                                           final float t2x, final float t2y,
                                           final float t3x, final float t3y) {
    float alpha = ((t2y - t3y) * (px - t3x) + (t3x - t2x) * (py - t3y)) /
        ((t2y - t3y) * (t1x - t3x) + (t3x - t2x) * (t1y - t3y));
    float beta = ((t3y - t1y) * (px - t3x) + (t1x - t3x) * (py - t3y)) /
        ((t2y - t3y) * (t1x - t3x) + (t3x - t2x) * (t1y - t3y));
    float gamma = 1.0f - alpha - beta;
    return alpha > 0.0f && beta > 0.0f && gamma > 0.0f;
  }
}
