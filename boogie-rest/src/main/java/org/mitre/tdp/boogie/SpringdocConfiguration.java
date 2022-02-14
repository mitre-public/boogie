package org.mitre.tdp.boogie;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
class SpringdocConfiguration {

  /**
   * Jackson hates our more complex data models and we're not using it anyway so disable the schema portion of the docs.
   */
  private static void disableSchemasBecauseJackson() {
    ModelConverters mcManager = ModelConverters.getInstance();
    mcManager.getConverters().forEach(mcManager::removeConverter);
  }

  @Bean
  OpenAPI openApi() {
    disableSchemasBecauseJackson();
    return new OpenAPI().info(new Info().title("Boogie"));
  }
}
