import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.infos.ArincPointInfo;
import org.mitre.boogie.xml.model.infos.ArincRecordInfo;

import nl.jqno.equalsverifier.EqualsVerifier;

public class ModelTests {
  @Test
  void testArincRecordInfo() {
    EqualsVerifier.forClass(ArincRecordInfo.class);
  }

  @Test
  void testArincPointInfo() {
    EqualsVerifier.forClass(ArincPointInfo.class);
  }
}
