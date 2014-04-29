package com.defrac.sample.flutterman;

import defrac.app.Bootstrap;
import defrac.app.GenericApp;
import defrac.display.TextureData;
import defrac.display.TextureDataFormat;
import defrac.display.TextureDataRepeat;
import defrac.display.TextureDataSmoothing;
import defrac.resource.ResourceGroup;
import defrac.resource.TextureDataResource;

import javax.annotation.Nonnull;
import java.util.List;

public final class FluttermanApp extends GenericApp {
  private static final String BACKGROUNDS_PNG = "backgrounds.png";
  private static final String SPRITESHEET_PNG = "spritesheet.png";
  private static final String SPLASHSCREEN_PNG = "splash.png";

  public static void main(final String[] args) {
    Bootstrap.run(new FluttermanApp());
  }

  FluttermanGame game;

  @Override
  protected void onCreate() {
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
        createGame(backgrounds, spritesheet, splashscreen);
      }
    });

    resources.load();
  }

  private void createGame(@Nonnull final TextureData backgroundsData,
                          @Nonnull final TextureData spritesheetData,
                          @Nonnull final TextureData splashscreenData) {
    game = new FluttermanGame(backgroundsData, spritesheetData, splashscreenData, stage());
    game.resizeTo(width(), height());
  }

  @Override
  protected void onResize(final float width, final float height) {
    if(null == game) {
      return;
    }

    game.resizeTo(width, height);
  }
}
