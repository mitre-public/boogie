package org.mitre.tdp.boogie.dafif;

import java.util.function.Function;

public final class FlyOverIndicator implements Function<String, Boolean> {
  public static final FlyOverIndicator INSTANCE = new FlyOverIndicator();
  private  FlyOverIndicator() {}
  @Override
  public Boolean apply(String s) {
    return switch (s) {
      case "B", "Y" -> true;
      case "" -> false;
      default -> null;
    };
  }
}
