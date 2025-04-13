package chatflow.memberservice.controller;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RequiredArgsConstructor
@RestController
@RequestMapping("/common")
public class CommonController {
    private final Environment env;

    @GetMapping("/health-check")
    public String status() {
        return String.format("Working in Member Service %s",
                env.getProperty("tag.version"));
    }
}
