package org.mitre.tdp.boogie.arinc;

import java.util.Optional;

public abstract class ArincInteger extends TrimmableField<Integer> {

  @Override
  protected final Optional<Integer> parseTrimmed(String source, int startOffset, int endOffset) {
    int digitsStart = digitsStart(source, startOffset);
    if (digitsStart == endOffset || !containsOnlyDigits(source, digitsStart, endOffset)) {
      return Optional.empty();
    }

    return Optional.of(Integer.parseInt(source, startOffset, endOffset, 10));
  }

  private static boolean containsOnlyDigits(String source, int startOffset, int endOffset) {
    for (int index = startOffset; index < endOffset; index++) {
      char character = source.charAt(index);
      if (character < '0' || character > '9') {
        return false;
      }
    }
    return true;
  }

  private static int digitsStart(String source, int startOffset) {
    char first = source.charAt(startOffset);
    return first == '+' || first == '-' ? startOffset + 1 : startOffset;
  }
}
