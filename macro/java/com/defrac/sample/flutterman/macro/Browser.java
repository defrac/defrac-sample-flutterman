package com.defrac.sample.flutterman.macro;

import defrac.compiler.Context;
import defrac.compiler.macro.Macro;
import defrac.compiler.macro.MethodBody;

import javax.annotation.Nonnull;

public final class Browser extends Macro {
  public Browser(@Nonnull Context context) {
    super(context);
  }

  @Nonnull
  public MethodBody openWebsite() {
    // This macro is equivalent to writing:
    //
    //   return com.defrac.sample.flutterman.Browser.openWebsite()
    //
    // Although we could do some more stuff like generate different
    // code for different platforms.
    //
    // The actual implementation can be found in the Java code for
    // the individual platforms.
    return MethodBody(
        Return(StaticCall("com.defrac.sample.flutterman.Browser", "openWebsite"))
    );
  }
}
