package org.mitre.tdp.boogie;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestMagneticVariation {

  @Test
  public void testMagneticToTruePublished() {
    MagneticVariation localVariation = mock(MagneticVariation.class);

    when(localVariation.published()).thenReturn(Optional.of(7.0f));
    when(localVariation.modeled()).thenReturn(10.0f);
    when(localVariation.magneticToTrue(anyDouble())).thenCallRealMethod();

    assertEquals(12.0d, localVariation.magneticToTrue(5.0));
  }

  @Test
  public void testMagneticToTrueModeled() {
    MagneticVariation localVariation = mock(MagneticVariation.class);

    when(localVariation.published()).thenReturn(Optional.empty());
    when(localVariation.modeled()).thenReturn(10.0f);
    when(localVariation.magneticToTrue(anyDouble())).thenCallRealMethod();

    assertEquals(12.0d, localVariation.magneticToTrue(2.0));
  }

  @Test
  public void testTrueToMagneticPublished() {
    MagneticVariation localVariation = mock(MagneticVariation.class);

    when(localVariation.published()).thenReturn(Optional.of(7.0f));
    when(localVariation.modeled()).thenReturn(10.0f);
    when(localVariation.trueToMagnetic(anyDouble())).thenCallRealMethod();

    assertEquals(12.0d, localVariation.trueToMagnetic(19.0));
  }

  @Test
  public void testTrueToMagneticModeled() {
    MagneticVariation localVariation = mock(MagneticVariation.class);

    when(localVariation.published()).thenReturn(Optional.empty());
    when(localVariation.modeled()).thenReturn(10.0f);
    when(localVariation.trueToMagnetic(anyDouble())).thenCallRealMethod();

    assertEquals(12.0d, localVariation.trueToMagnetic(22.0));
  }
}
