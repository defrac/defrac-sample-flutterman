package com.defrac.sample.flutterman;

import com.defrac.sample.flutterman.visual.*;
import com.defrac.sample.flutterman.world.*;
import defrac.annotation.MacroA5D;
import defrac.annotation.MacroJVM;
import defrac.annotation.MacroWeb;
import defrac.display.*;
import defrac.display.event.*;
import defrac.event.EnterFrameEvent;
import defrac.event.Events;
import defrac.geom.Point;
import defrac.geom.Rectangle;
import defrac.lang.Procedure;

import javax.annotation.Nonnull;

public final class FluttermanGame {
  private static final boolean HEARTS_ENABLED = true;
  private static final int NUM_HEARTS = 64;
  private static final int NUM_SPARKLES = 64;
  private static final int STATE_SPLASH = 0;
  private static final int STATE_RUNNING = 1;
  private static final int STATE_GAME_OVER = 2;
  private static final float SCROLL_SPEED = 1.0f;

  @Nonnull
  private final Spritesheet spritesheet;

  @Nonnull
  private final InfiniteBackground background;

  @Nonnull
  private final Score score;

  private int passedObstacles;

  @Nonnull
  private TileSlice slices;

  @Nonnull
  private final WorldGenerator worldGenerator;

  @Nonnull
  private final Hero hero;

  @Nonnull
  private final DisplayObjectContainer container;
  @Nonnull
  private final DisplayObjectContainer splashScreen;

  @Nonnull
  private final DisplayObject splashImage, splashLogo;

  @Nonnull
  private final Rectangle heroAABB = new Rectangle();

  @Nonnull
  private final Rectangle tileAABB = new Rectangle();

  private int frame;
  private float scale = 1.0f;
  private float scaledWidth = Spritesheet.TILE_WIDTH;
  private float scaledHeight = Spritesheet.TILE_HEIGHT;
  private float width, height;
  private int state;
  private float scrollSpeed = SCROLL_SPEED;

  @Nonnull
  private final HeartParticle[] hearts = new HeartParticle[NUM_HEARTS];

  @Nonnull
  private final FireworkParticle[] fireworkParticles = new FireworkParticle[NUM_SPARKLES];

  private int currentSparkle = 0;

  public FluttermanGame(@Nonnull final TextureData backgroundsData,
                        @Nonnull final TextureData spritesheetData,
                        @Nonnull final TextureData splashscreenData,
                        @Nonnull final DisplayObjectContainer container) {
    this.container = container;

    spritesheet = new Spritesheet(spritesheetData);
    worldGenerator = new WorldGenerator();

    // Add the background first
    background = container.addChild(new InfiniteBackground(backgroundsData));
    // Then the tiles which will be used to display the world
    slices = createSlices(container, spritesheet);
    // Add some space for effects
    final DisplayObjectContainer effectLayer = container.addChild(new Layer(NUM_SPARKLES));
    // Then finally the hero
    hero = container.addChild(new Hero(spritesheet));

    // The hearts are added on top of the hero
    if(HEARTS_ENABLED) {
      final Layer heartLayer =
          container.addChild(new Layer(NUM_HEARTS));

      for(int i = 0; i < NUM_HEARTS; ++i) {
        hearts[i] = heartLayer.addChild(new HeartParticle(spritesheet));
        hearts[i].visible(false);
      }
    }

    // And the score always on top
    score = container.addChild(new Score(spritesheet));

    // The splash screen is living on its own layer on top of everything else so far
    splashScreen = container.addChild(new Layer(2));
    splashImage = splashScreen.addChild(
        new Image(
            new Texture(
                splashscreenData,
                0.0f, 71.0f,
                336.0f, 189.0f,
                0,
                0.0f, 71.0f,
                336.0f, 118.0f)));
    splashLogo = splashScreen.addChild(
        new Image(
            new Texture(
                splashscreenData,
                49.0f, 19.0f,
                252.0f, 44.0f
            )
        )
    );


    // Generate all the fireworks
    for(int i = 0; i < NUM_SPARKLES; ++i) {
      fireworkParticles[i] = effectLayer.addChild(new FireworkParticle(spritesheet));
      fireworkParticles[i].visible(false);
    }

    // Start with the splash screen
    state = STATE_SPLASH;

    // ... and do not forget to fill our world!
    TileSlice p = slices;
    while(p != null) {
      worldGenerator.generate(p);
      p = p.next;
    }

    // On each frame, we want to update the game
    Events.onEnterFrame.attach(new Procedure<EnterFrameEvent>() {
      @Override
      public void apply(EnterFrameEvent enterFrameEvent) {
        tick();
      }
    });

    // For some very check user interaction, we simply attach ourselves to
    // the container and react on the events if the state is correct
    container.addProcessHook(new UIProcessHook() {
      @Override
      public void processEvent(@Nonnull UIEvent event) throws UIHookInterrupt {
        switch(event.type) {
          case UIEventType.ACTION_SINGLE:
            if(state == STATE_SPLASH) {
              // If the user hits the defrac logo, we want to open
              // http://www.defrac.com so check if it has been hit
              final float defracX = splashLogo.x() + 223.0f * scale;
              final float defracY = 65.0f * scale;
              final float defracW = 33.0f * scale;
              final float defracH = 10.0f * scale;
              final Point pos = ((UIActionEvent)event).pos;
              final boolean openWebsite;

              openWebsite =
                   pos.x >= defracX
                && pos.x < (defracX + defracW)
                && pos.y >= defracY
                && pos.y < (defracY + defracH)  // ... if we made it here, the logo has been hit
                && openWebsite();               // ... so let's call our macro

              if(!openWebsite) {
                // User didn't/couldn't open the website, let's move into the
                // running state
                frame = 0;
                state = STATE_RUNNING;
              }
            }
            break;
          case UIEventType.ACTION_BEGIN:
            if(state == STATE_RUNNING) {
              // We are running, the user touched/clicked ->
              // add some velocity to the hero
              hero.vy -= 2.5f;
              break;
            }
        }
      }
    });
  }

  // Macro to open http://www.defrac.com/ in a browser window
  @MacroJVM("com.defrac.sample.flutterman.macro.Browser.openWebsite")
  @MacroWeb("com.defrac.sample.flutterman.macro.Browser.openWebsite")
  @MacroA5D("com.defrac.sample.flutterman.macro.Browser.openWebsite")
  private static native boolean openWebsite();

  @Nonnull
  private static TileSlice createSlices(@Nonnull final DisplayObjectContainer container,
                                        @Nonnull final Spritesheet spritesheet) {
    // Create a linked list of slices (+ one extra)
    TileSlice next = null;
    int i = 0;

    do {
      final TileSlice prev = container.addChild(new TileSlice(spritesheet));
      prev.x(Constants.WIDTH * Spritesheet.TILE_WIDTH - i * Spritesheet.TILE_WIDTH);
      prev.next = next;
      next = prev;
    } while(i++ < Constants.WIDTH);

    return next;
  }

  public void resizeTo(final float width, final float height) {
    // NOTE: The whole game is not really resizing correct.
    //       Also the scale is used quite often since each tile
    //       is scaled individually which means we transform
    //       way to often between coordinate systems.
    //       However for the purpose of this game we can live with it.
    //
    //       Due to the way resize works, we might end up with the game
    //       being to large for the screen in its width. Therefore
    //       The hero is not necessarily always centered. But we do not
    //       care about that for now.
    //
    //       The splash's logo is always at the right position since it would
    //       look really weird if it wasn't centered correct.
    this.width = width;
    this.height = height;

    final float scaleX = width / (float)(Constants.WIDTH * Spritesheet.TILE_WIDTH);
    final float scaleY = height / (float)(Constants.HEIGHT * Spritesheet.TILE_HEIGHT);
    final float scale = Math.max(scaleX, scaleY);
    final float scaleX2 = scale * 2.0f;
    final float scaledWidth = Math.round(Spritesheet.TILE_WIDTH * scale);
    final float scaledHeight = Math.round(Spritesheet.TILE_HEIGHT * scale);
    float offsetX = 0.0f;

    splashImage.scaleTo(scale);
    splashLogo.scaleTo(scale);
    splashLogo.moveTo(0.5f * (width - splashLogo.width() * scale), 28.0f * scale);

    this.scale = scale;
    this.scaledWidth = scaledWidth;
    this.scaledHeight = scaledHeight;
    this.scrollSpeed = SCROLL_SPEED * scale;

    background.resizeTo(width, height);
    score.resizeTo(scale).moveTo(scale * 5.0f, scale * 4.0f);

    TileSlice p = slices;
    while(p != null) {
      p.x(offsetX);
      p.adjustSize(width, height);
      p = p.next;
      offsetX += scaledWidth;
    }

    tileAABB.width = heroAABB.width = scaledWidth;
    tileAABB.height = heroAABB.height = scaledHeight;

    hero
        .scaleTo(scale)
        .moveTo(
            heroAABB.x = 0.5f * (float) Constants.WIDTH * scaledWidth,
            heroAABB.y = 0.5f * (float) Constants.HEIGHT * scaledHeight
        );

    // The hero is moved to the center of the screen. Since the registration
    // point of the display object is centered, we need to offset the AABB
    heroAABB.x -= 0.5f * scaledWidth;
    heroAABB.y -= 0.5f * scaledHeight;

    // Also, the bounding box should be trimmed a little bit since the
    // hero is not occupying the full tile
    heroAABB.x += scale;
    heroAABB.width -= scaleX2;
  }

  private void tick() {
    if(STATE_SPLASH == state) {
      handleSplash();
    } else if(STATE_RUNNING == state) {
      handleRunning();
    } else if(state == STATE_GAME_OVER) {
      handleGameOver();
    }
  }

  private void handleSplash() {
    ++frame;

    if((frame & 3) == 0) {
      // Animate the hero every 4th frame
      hero.flutter();
      hero.update();
    }

    //... and make it look funny
    hero.y(0.5f * (float) Constants.HEIGHT * scaledHeight + (float)Math.sin((float)frame * 0.1f) * 2.0f * scale);
  }

  private void handleRunning() {
    ++frame;

    { // #0: HANDLE THE SPLASH
      if(frame <= 16) {
        // Splash is still fading out ...
        splashScreen.alpha(1.0f - (float)frame / 16.0f);

        if(frame == 16) {
          // Kill the splash screen!
          container.removeChild(splashScreen);
          state = STATE_RUNNING;
        }
      }
    }

    { // #1: UPDATE THE WORLD

      // Render the background at its new position
      background.render(frame * 0.25f * scrollSpeed);

      // Animate the hero every 4th frame
      if((frame & 3) == 0) {
        hero.flutter();
      }

      // Update velocity and position of the hero
      hero.vy += Constants.GRAVITY;
      hero.vy *= Constants.FRICTION;
      hero.moveBy(0.0f, scale * hero.vy);
      hero.update();
      heroAABB.y = hero.y() - 0.5f * scaledHeight;

      // Move the world
      TileSlice p = slices;
      while(p != null) {
        p.x(Math.round(p.x() - scrollSpeed));
        p = p.next;
      }
    }

    { // #2: CONTINUE THE WORLD

      // If we dropped a TileSlice (x < 0) regenerate its
      // content and place it behind the last slice we
      // still have to create an infinite scrolling world
      if(slices.x() <= -scaledWidth) {
        final TileSlice newSlice = slices;
        assert null != newSlice.next;

        // Since the slice we are regenerating was the head
        // of the list we need to set the new head to the
        // next slice
        slices = newSlice.next;
        newSlice.next = null;

        // Search for the last slice in O(n)
        // This can be done in O(1) by keeping
        // track of the last slice
        TileSlice lastSlice = slices;
        while(lastSlice.next != null) {
          lastSlice = lastSlice.next;

          // This check is just in here to make our IDEs happy
          // since we already know lastSlice is not null (newSlice.next != null)
          // and lastSlice.next is also not null
          // so this lastSlice is also not null ...
          assert lastSlice != null;
        }

        // Add our new slice to the end of the list and regenerate
        // its content
        lastSlice.next = newSlice;
        newSlice.x(Math.round(lastSlice.x() + scaledWidth));
        worldGenerator.generate(newSlice);
      }
    }

    { // #3: CHECK STATE OF WORLD AND UPDATE SCORE

      // Iterate over all slices and check for potential collisions
      TileSlice p = slices;
      loop: while(p != null) {
        tileAABB.x = p.x();

        // Drop complete slices we do not have to check as early
        // as possible
        if(
            (tileAABB.x + tileAABB.width) >= heroAABB.x &&
                tileAABB.x <= (heroAABB.x + heroAABB.width)
            ) {
          // The x-axis intersects, we need to check this slice for collisions
          // and simply iterate the tiles top to bottom
          tileAABB.y = 0.0f;

          final int[] tiles = p.tiles;

          for(int i = 0; i < Constants.HEIGHT; ++i) {
            final int id = tiles[i];

            if(    id != Sprites.NONE                           // Only check for a collision if this tile is occupied
                && id <  Sprites.DECORATION_0                     // ... and not just decoration
                && heroAABB.intersects(tileAABB)                // ... and it intersects the AABB of the hero
                && Collisions.is(id, heroAABB, tileAABB, scale) // ... and the more individual test succeeds
                ) {
              handleCollision();

              // Stop! Hammertime
              break loop;
            }

            tileAABB.y += scaledHeight;
          }
        } else {
          if(heroAABB.x > (tileAABB.x + tileAABB.width) && !p.counted) {
            passedObstacles++;
            score.show(passedObstacles);
            p.counted = true;
          }
        }

        p = p.next;
      }
    }
  }

  private void handleCollision() {
    frame = 0;

    // Make that dying move...
    hero.vy = -5.0f;
    hero.texture(spritesheet.get(Sprites.HERO_SAD));
    hero.rotationDestination = (float)-Math.PI;

    // Show some love (if enabled)
    if(HEARTS_ENABLED) {
      for(int i = 0; i < NUM_HEARTS; ++i) {
        final HeartParticle heart = hearts[i];
        heart.init(width, height, scale);
      }
    }

    // And move to the game-over state
    state = STATE_GAME_OVER;
  }

  private void handleGameOver() {
    if(++frame == 128) {
      // Switch to splash screen after some time
      switchToSplashScreen();
    } else if(passedObstacles > 0 &&
        (frame == 20 || frame == 24 || frame == 32 || frame == 40 || frame == 48 || frame == 64 || frame == 100)) {
      // Show some particles for the final score
      final int n = 20;
      float angle = 0.0f;
      float angleIncrement = (2.0f * (float)Math.PI) / (float)(n - 1);
      for(int i = 0; i < n; ++i) {
        fireworkParticles[currentSparkle].init(
            score.x() + (score.width() - 4.0f * scale) * 0.5f,
            score.y() + (score.height() - 4.0f * scale) * 0.5f,
            scale, angle);
        fireworkParticles[currentSparkle].visible(true);
        angle += angleIncrement;
        if(++currentSparkle == NUM_SPARKLES) currentSparkle = 0;
      }
    } else if(frame > 8) {
      // Update the love (if enabled)
      if(passedObstacles == 0 && HEARTS_ENABLED) {
        for(final HeartParticle heart : hearts) {
          heart.update(scale);
        }
      }

      // Move the score to center of screen, make it also bigger
      score.
          moveTo(
              Math.round(score.x() + ((width - score.width() - 12.0f * scale) * 0.5f - score.x()) * 0.2f),
              Math.round(score.y() + ((height - score.height() - 4.0f * scale) * 0.5f - score.y()) * 0.2f)).
          scaleTo(
              score.scaleX() + (2.0f - score.scaleX()) * 0.4f
          );

      // Update the hero but let it fall down a little bit quicker
      hero.vy += Constants.GRAVITY * 1.5f;
      hero.vy *= Constants.FRICTION;
      hero.moveBy(0.0f, scale * hero.vy);
      hero.rotation(hero.rotationCurrent = hero.rotationCurrent + (hero.rotationDestination - hero.rotationCurrent) * 0.1f);
    }

    // Update all those nice particles!
    for(final FireworkParticle fireworkParticle : fireworkParticles) {
      fireworkParticle.update(scale);
    }
  }

  private void switchToSplashScreen() {
    // Hide the love (if enabled)
    if(HEARTS_ENABLED) {
      for(int i = 0; i < NUM_HEARTS; ++i) {
        final HeartParticle heart = hearts[i];
        heart.visible(false);
      }
    }

    // Restore the initial state
    reset();
    state = STATE_SPLASH;
    background.render(frame * 0.25f * scrollSpeed);
    container.addChild(splashScreen.alpha(1.0f));
    hero.update();
  }

  private void reset() {
    // Reset the world
    worldGenerator.reset();
    frame = 0;
    passedObstacles = 0;
    score.reset();

    float offsetX = 0.0f;

    // Re-generate the world
    TileSlice p = slices;
    while(p != null) {
      p.x(offsetX);
      worldGenerator.generate(p);
      p = p.next;
      offsetX += scaledWidth;
    }

    // Hide the fireworks
    currentSparkle = 0;
    for(final FireworkParticle fireworkParticle : fireworkParticles) {
      fireworkParticle.visible(false);
    }

    // Move the score back to its original position
    score.scaleTo(1.0f).moveTo(5.0f * scale, 4.0f * scale);

    // The hero should also be moved to its initial position
    hero
        .scaleTo(scale)
        .moveTo(
            heroAABB.x = 0.5f * (float) Constants.WIDTH * scaledWidth,
            heroAABB.y = 0.5f * (float) Constants.HEIGHT * scaledHeight
        );
    heroAABB.x -= 0.5f * scaledWidth;
    heroAABB.y -= 0.5f * scaledHeight;
    heroAABB.x += scale;
    hero.rotationCurrent = 0.0f;
    hero.rotationDestination = 0.0f;
    hero.vy = 0.0f;

    // And we are running by default ...
    state = STATE_RUNNING;
  }
}
