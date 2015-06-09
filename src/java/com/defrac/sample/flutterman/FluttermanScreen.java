package com.defrac.sample.flutterman;

import defrac.display.*;
import defrac.resource.ResourceGroup;
import defrac.resource.TextureDataResource;
import defrac.ui.DisplayList;
import defrac.ui.Screen;

import javax.annotation.Nonnull;
import java.util.List;

public final class FluttermanScreen extends Screen {
  private static final String BACKGROUNDS_PNG = "backgrounds.png";
  private static final String SPRITESHEET_PNG = "spritesheet.png";
  private static final String SPLASHSCREEN_PNG = "splash.png";

  DisplayList displayList;

  FluttermanGame game;

  @Override
  protected void onCreate() {
    super.onCreate();

    // Force landscape orientation (not part of defrac.ui.Screen yet)
    //orientation(Orientation.LANDSCAPE);

    displayList = new DisplayList();

    // Load a set of resources, but please: no smoothing
    final ResourceGroup<TextureData> resources = ResourceGroup.of(
        TextureDataResource.from(BACKGROUNDS_PNG, TextureDataFormat.RGBA, TextureDataRepeat.NO_REPEAT, TextureDataSmoothing.NO_SMOOTHING),
        TextureDataResource.from(SPRITESHEET_PNG, TextureDataFormat.RGBA, TextureDataRepeat.NO_REPEAT, TextureDataSmoothing.NO_SMOOTHING),
        TextureDataResource.from(SPLASHSCREEN_PNG, TextureDataFormat.RGBA, TextureDataRepeat.NO_REPEAT, TextureDataSmoothing.NO_SMOOTHING));

    resources.listener(new ResourceGroup.SimpleListener<TextureData>() {
      @Override
      public void onResourceGroupComplete(@Nonnull ResourceGroup<TextureData> resourceGroup,
                                          @Nonnull List<TextureData> content) {
        final TextureData backgrounds  = content.get(0);
        final TextureData spritesheet  = content.get(1);
        final TextureData splashscreen = content.get(2);

        // Wait for the Stage and then create the Game
        displayList.onStageReady(stage -> createGame(stage, backgrounds, spritesheet, splashscreen));
      }
    });

    resources.load();

    rootView(displayList);
  }

  @Override
  protected void onPause() {
    super.onPause();
    displayList.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    displayList.onResume();
  }

  private void createGame(@Nonnull final Stage stage,
                          @Nonnull final TextureData backgroundsData,
                          @Nonnull final TextureData spritesheetData,
                          @Nonnull final TextureData splashscreenData) {
    game = new FluttermanGame(backgroundsData, spritesheetData, splashscreenData, stage);
    game.resizeTo(width(), height());

    stage.globalEvents().onResize.add(event -> game.resizeTo(event.width, event.height));
  }
}
