package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.BlankSpec;

public class TestArincData {

  @Test
  public void testParseWithSpecFailsWhenFinalOffsetNotEqualToRecordLength() {
    ArincData data = new ArincData("himom");

    RecordSpec spec = mock(RecordSpec.class);
    when(spec.recordFields()).thenReturn(Collections.singletonList(ArincField.newField("small", new BlankSpec(4))));

    assertThrows(IllegalArgumentException.class, () -> data.parseWithSpec(spec));
  }
}
