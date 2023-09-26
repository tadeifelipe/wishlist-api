package com.tadeifelipe.wishlistapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.web.bind.annotation.RestController;

@OpenAPIDefinition(
        info = @Info(title = "Wishlist API", version = "0.0.1-SNAPSHOT"),
        servers = {@Server(description = "Localhost", url = "http://localhost:8080")}
)
@RestController
@SecurityScheme(
        name = "basicAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "basic"
)
public class OpenAPIConfig {
}
