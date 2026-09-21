package com.naveench.portfolioassist.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The front end is a plain static HTML file, not a dev-served app, so it can
 * be opened from disk (origin "null") during local testing or dropped into
 * the existing portfolio site once deployed. Tighten this to the real
 * deployed domain before going live.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST")
                .allowedHeaders("*");
    }
}
