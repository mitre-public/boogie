package org.mitre.tdp.boogie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * To run this class (from Intellij or another IDE) the application needs to know the path to the .properties file containing
 * the ARINC-424 source config information on the local machine.
 * <p>
 * Spring will search for this information in a few places:
 * <ul>
 *   <li>Arguments to the Jar - (which we can override in the IDE)</li>
 *   <li>{@link System#getProperty(String)} - which would typically be set externally</li>
 *   <li>{@link System#getenv(String)} - which would typically be set externally</li>
 * </ul>
 * As such to run via Intellij (assuming you have your local AWS credentials setup and you're MFA logged in) you can simply
 * override the arguments inline as:
 * <pre>{@code
 * SpringApplication.run(TempestApplication.class, "--arinc.config.path=/path/to/config/my/machine"); // and others for ASOS/etc.
 * }</pre>
 * Docker-wrapped versions of this exist and can be launched in accordance with the README.md.
 * <p>
 * Note the {@link EnableScheduling} annotation allows spring to schedule and execute the various {@link DirectoryWatcher}s.
 */
@SpringBootApplication
@EnableScheduling
public class BoogieApplication {

  public static void main(String[] args) {
    SpringApplication.run(BoogieApplication.class, args);
  }
}
