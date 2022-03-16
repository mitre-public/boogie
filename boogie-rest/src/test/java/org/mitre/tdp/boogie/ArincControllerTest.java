package org.mitre.tdp.boogie;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.google.gson.Gson;

class ArincControllerTest {

  private static final Gson gson = new Gson();

  private static MockMvc mockMvc;

  private static File resourceFile(String filename) {
    return new File(System.getProperty("user.dir").concat("/src/test/resources/").concat(filename));
  }

  @BeforeAll
  static void setup() {
    BoogieState state = BoogieState.forVersion(ArincVersion.V19);
    state.accept(resourceFile("kjfk-and-friends.txt").toPath());

    ArincController controller = new ArincController(new Gson(), state);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

//  @Test
//  void test
}
