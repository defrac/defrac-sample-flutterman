package com.defrac.sample.flutterman.macro.ios;

import defrac.compiler.Context;
import defrac.compiler.macro.Macro;
import defrac.compiler.macro.MethodBody;

import javax.annotation.Nonnull;

public class Browser extends Macro {
    public Browser(@Nonnull Context context) {
        super(context);
    }

  @Nonnull
  public MethodBody openWebsite() {
    return MethodBody(
      //TODO(joa): make this work without modifying xcode project
      //Untyped("[[UIApplication sharedApplication] openURL:[NSURL URLWithString: @\"http://www.defrac.com/\"]]"),
      Return(False()));
  }
}
