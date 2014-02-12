package com.defrac.sample.flutterman;

import defrac.app.Bootstrap;
import defrac.app.GenericApp;
import defrac.display.TextureData;
import defrac.display.TextureDataFormat;
import defrac.display.TextureDataRepeat;
import defrac.display.TextureDataSmoothing;
import defrac.event.ResourceGroupEvent;
import defrac.event.StageEvent;
import defrac.lang.Procedure;
import defrac.resource.ResourceGroup;
import defrac.resource.TextureDataResource;

import javax.annotation.Nonnull;

public final class FluttermanApp extends GenericApp {
  private static final String BACKGROUNDS_PNG = "backgrounds.png";
  private static final String SPRITESHEET_PNG = "spritesheet.png";
  private static final String SPLASHSCREEN_PNG = "splash.png";

  public static void main(final String[] args) {
    Bootstrap.run(new FluttermanApp());
  }

  @Override
  protected void onCreate() {
    // Load a set of resources, but please: no smoothing
    final ResourceGroup<TextureData> resources = ResourceGroup.of(
        TextureDataResource.from(BACKGROUNDS_PNG, TextureDataFormat.RGBA, TextureDataRepeat.NO_REPEAT, TextureDataSmoothing.NO_SMOOTHING),
        TextureDataResource.from(SPRITESHEET_PNG, TextureDataFormat.RGBA, TextureDataRepeat.NO_REPEAT, TextureDataSmoothing.NO_SMOOTHING),
        TextureDataResource.from(SPLASHSCREEN_PNG, TextureDataFormat.RGBA, TextureDataRepeat.NO_REPEAT, TextureDataSmoothing.NO_SMOOTHING));

    resources.onComplete.attach(new Procedure<ResourceGroupEvent.Complete<TextureData>>() {
      @Override
      public void apply(ResourceGroupEvent.Complete<TextureData> event) {
        final TextureData backgrounds  = event.contents.get(0);
        final TextureData spritesheet  = event.contents.get(1);
        final TextureData splashscreen = event.contents.get(2);
        createGame(backgrounds, spritesheet, splashscreen);
      }
    });

    resources.load();
  }

  private void createGame(@Nonnull final TextureData backgroundsData,
                          @Nonnull final TextureData spritesheetData,
                          @Nonnull final TextureData splashscreenData) {
    final FluttermanGame game =
        new FluttermanGame(backgroundsData, spritesheetData, splashscreenData, stage());

    game.resizeTo(width(), height());
    onResize().attach(new Procedure<StageEvent.Resize>() {
      @Override
      public void apply(StageEvent.Resize event) {
        game.resizeTo(width(), height());
      }
    });
  }
}
