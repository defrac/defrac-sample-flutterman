package com.defrac.sample.flutterman.world;

import com.defrac.sample.flutterman.visual.TileSlice;

import javax.annotation.Nonnull;
import java.util.Random;

public final class WorldGenerator {
  /** The state of generating flat grass */
  private static final int STATE_FLAT = 0;
  /** The state of generating a bump */
  private static final int STATE_BUMP = 1;
  /** The phase speed of the gap generator */
  private static final float PHASE_SPEED = 880.0f / 44100.0f;
  /** The seed of the random number generator */
  private static final int SEED = 0xdef24c;

  /** The random number generator used to create the world */
  @Nonnull
  private final Random random = new Random(SEED);
  /** The second random number generator used to create the world (since we already like the output of the first generator) */
  @Nonnull
  private final Random random2 = new Random(SEED);
  /** The current slice */
  private int sliceIndex = 0;
  /** The state of the generator */
  private int state;
  /** Some info associated with a state */
  private int stateInfo;
  /** The phase for the gap generation */
  private float phi;

  public void generate(@Nonnull final TileSlice slice) {
    final int[] tiles = slice.tiles;

    // If there is no obstacle, there are no points to gain.
    // We assume that this slice has already been counted
    // for the high-score
    slice.counted = true;

    // Always have a box at the top!
    switch(random2.nextInt(4)) {
      case 0: tiles[0] = Sprites.BOX_0; break;
      case 1: tiles[0] = Sprites.BOX_1; break;
      case 2: tiles[0] = Sprites.BOX_2; break;
      case 3: tiles[0] = Sprites.BOX_3; break;
    }

    switch(state) {
      case STATE_FLAT: {
        // We are generating some flat grass, first fill the entire
        // slice but the last element with a void
        for(int i = 1; i < Constants.HEIGHT - 1; ++i) {
          tiles[i] = Sprites.NONE;
        }

        // Now put some grass at the bottom
        tiles[Constants.HEIGHT - 1] = Sprites.GRASS;

        // NOTE: We never switch from obstacle to bump
        //       without at least one slice of flat land
        //       in between since it would be too hard.
        //       That's what the else-if is good for here.
        //       Only switch to bump-mode if no obstacle
        //       was placed

        // Should we generate an obstacle here?
        //
        // NOTE: Check for stateInfo > 0 since we do not
        //       want to create an obstacle right after
        //       entering STATE_FLAT -- we might get into
        //       trouble because this way an obstacle
        //       could be inserted right after a bump
        //       at a low position which is too hard
        if(stateInfo > 1 && generateObstacle()) {
          // Well, yes! So in that case the slice contributes to the
          // high-score, mark it as not-counted
          slice.counted = false;
          // ... and place an obstacle on the slice. The full height
          // minus the grass tile may be used
          placeObstacle(tiles, 1, Constants.HEIGHT - 1);
          stateInfo++;
        } else {
          if(generateDecal()) {
            // Place some decoration on the ground
            placeDecal(tiles, Constants.HEIGHT - 2);
          }

          if(random.nextFloat() < 0.125f && stateInfo > 1) {
            // We do not want to generate an obstacle so it might
            // be time to insert a bump after this slice and make
            // the world a little bit more interesting
            state = STATE_BUMP;
            stateInfo = 0;
          } else {
            stateInfo++;
          }
        }
        break;
      }
      case STATE_BUMP: {
        // We are generating a bump -- the height is always two!
        // Simply set everything else to Sprites.NONE
        for(int i = 1; i < Constants.HEIGHT - 2; ++i) {
          tiles[i] = Sprites.NONE;
        }

        // In fact, the STATE_BUMP state contains a couple of states
        // on its own. Those are represented with the stateInfo
        if(stateInfo == 0) {
          // The start of the bump, no obstacle here
          tiles[Constants.HEIGHT - 2] = Sprites.GRASS_BUMP_UP;
          tiles[Constants.HEIGHT - 1] = Sprites.GRASS_BUMP_UP_BELOW;
          // How long is it? Somewhere in [1,10]
          stateInfo = 3 + random.nextInt(10);
        } else if(stateInfo > 2) {
          // Keep generating the bump ...
          tiles[Constants.HEIGHT - 2] = Sprites.GRASS;
          tiles[Constants.HEIGHT - 1] = Sprites.EARTH;
          // We might want to place an obstacle here
          if(generateObstacle()) {
            // Do not forget to count it for the high-score
            slice.counted = false;
            placeObstacle(tiles, 1, Constants.HEIGHT - 2);
          } else {
            if(generateDecal()) {
              // Place some decoration on the ground
              placeDecal(tiles, Constants.HEIGHT - 3);
            }
          }
          // This is a very ugly hack to get into the end-state
          stateInfo--;
        } else if(stateInfo == 2){
          // The end of the bump
          tiles[Constants.HEIGHT - 2] = Sprites.GRASS_BUMP_DOWN;
          tiles[Constants.HEIGHT - 1] = Sprites.GRASS_BUMP_DOWN_BELOW;
          // Continue generating flat land...
          state = STATE_FLAT;
          stateInfo = 0;
        }
        break;
      }
    }

    // We modified the tiles array so we should be nice and
    // tell the slice about it so it can swap the textures
    slice.updateTiles();

    // Here we keep track of the slice and the phase for the
    // gap generation
    ++sliceIndex;
    phi += PHASE_SPEED;
    if(phi > 1.0f) {
      --phi;
    }
  }

  private boolean generateObstacle() {
    // We want to generate obstacles only if:
    //   - We generate at least the 10th slice of the world
    //   - The slice is divisible by 4
    //
    // NOTE: This may lead to acceptable gaps of course
    //       since we never generate an obstacle on top
    //       of a BUMP_UP or BUMP_DOWN tile as this
    //       beautiful ascii-art drawing explains
    //
    //      no obstacle here!
    //        |
    //  obstacle
    //    |   |    obstacle
    //    |  _|   |
    // ___|_/ \___|_
    //
    //     <-gap->
    //
    // No problem though since it gives the player some
    // time to breathe and we generate enough obstacles
    // anyways
    return sliceIndex > 15 && (sliceIndex & 3) == 0;
  }

  private boolean generateDecal() {
    return random2.nextFloat() < 0.4f;
  }

  public void reset() {
    // When resetting the world generator, we simply
    // have to re-seed the pseudo-random-number-generator
    // and set other values like phase and sliceIndex
    // to zero
    state = STATE_FLAT;
    stateInfo = 0;
    sliceIndex = 0;
    phi = 0.0f;
    random.setSeed(SEED);
    random2.setSeed(SEED);
  }

  private void placeObstacle(@Nonnull final int[] tiles, final int from, final int to) {
    // We have to generate an obstacle. And there are so many
    // to choose from. Oh my...

    // How much space is still left?
    final float d = (to - (from + 1));

    // And where should we place the gap?
    final double alpha = phi * Math.PI * 2.0;
    int gap = from + 1
        + (int)Math.round(d * 0.5f + Math.sin(alpha) * d * 0.25f)  // Use a sine wave for the gap position
        - random.nextInt(2)                                        // ... and then spice it up a little bit
        + random.nextInt(2);                                       // ... by adding some random to it

    // Do not make a gap at the very bottom, which could happen due to
    // some of unfortunate events of the random numbers and phase
    if(gap > to - 2) {
      gap = to - 2;
    }

    // Let's see if the gap is at position 1 or 0.
    // In that case there is no space for the
    // precious anvil, so it must be greater than 1
    // if we want to place it
    if(gap > 2) {
      // Let's start with a chain at the top
      tiles[from] = Sprites.CHAIN;

      // Now continue with that chain until but stop before (gap - 2)
      for(int i = from + 1; i < gap - 2; ++i) {
        tiles[i] = Sprites.CHAIN;
      }

      // ... because we want to place the anvil here :)
      tiles[gap - 2] = Sprites.ANVIL;
    }

    // If the anvil is at gap - 2 and we continue at gap + 1
    // we have gap and (gap - 1) as empty space. Two tiles
    // are enough, one would be too hard
    //
    // This value might be negative if there is 0 or less
    // space left below the anvil
    final int availableSpaceBelowAnvil = to - (gap + 1);

    // Depending on how much space we have left, we might want
    // to choose from our collection of different candies
    if(availableSpaceBelowAnvil == 1) {
      // One of these occupies exactly one tile
      tiles[to - 1] =
          random.nextBoolean()
              ? Sprites.MELTED_LOLLY_0
              : Sprites.MELTED_LOLLY_1;
    } else if(availableSpaceBelowAnvil == 2) {
      // Those require exactly two tiles ...
      switch(random.nextInt(5)) {
        case 0:
          tiles[to - 2] = Sprites.GREEN_THING_HEAD;
          tiles[to - 1] = Sprites.GREEN_THING_BODY;
          break;
        case 1:
          tiles[to - 2] = Sprites.LOLLY_0;
          tiles[to - 1] = Sprites.LOLLY_STICK;
          break;
        case 2:
          tiles[to - 2] = Sprites.LOLLY_1;
          tiles[to - 1] = Sprites.LOLLY_STICK;
          break;
        case 3:
          tiles[to - 2] = Sprites.LOLLY_2;
          tiles[to - 1] = Sprites.LOLLY_STICK;
          break;
        case 4:
          tiles[to - 2] = Sprites.LOLLY_3;
          tiles[to - 1] = Sprites.LOLLY_STICK;
          break;
      }
    } else if(availableSpaceBelowAnvil > 0) {
      // If we have an arbitrary amount of space left,
      // we generate one of those guys since they can
      // grow in height without looking ugly
      final int head;
      final int body;

      if(random.nextBoolean()) {
        head = Sprites.CANDY_HEAD_0;
        body = Sprites.CANDY_BODY_0;
      } else {
        head = Sprites.CANDY_HEAD_1;
        body = Sprites.CANDY_BODY_1;
      }

      tiles[gap + 1] = head;
      for(int i = gap + 2; i < to; ++i) {
        tiles[i] = body;
      }
    }
  }

  private void placeDecal(final int[] tiles, final int pos) {
    tiles[pos] = Sprites.DECORATION_0 + random2.nextInt(Sprites.MAX_DECORATION);
  }
}
