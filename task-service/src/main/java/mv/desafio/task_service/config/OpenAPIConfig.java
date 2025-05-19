package mv.desafio.task_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() throws IOException {
        InputStream inputStream = new ClassPathResource("openapi.yaml").getInputStream();
        String yaml = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        SwaggerParseResult parseResult = new OpenAPIV3Parser().readContents(yaml);
        return parseResult.getOpenAPI();
    }
}
