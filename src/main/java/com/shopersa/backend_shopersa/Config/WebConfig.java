package com.shopersa.backend_shopersa.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.springframework.http.CacheControl;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Value("${app.uploads.products-dir:uploads/products}")
  private String productsDir;

  /* 
  @Override
  public void addCorsMappings(@NonNull CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("http://localhost:*")
        .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS")
        .allowedHeaders("Authorization","Content-Type")
        .exposedHeaders("Authorization","Content-Disposition")
        .allowCredentials(true)
        .maxAge(3600);
  }
  */

  
  @Override
  public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
    Path uploadPath = Paths.get(productsDir).toAbsolutePath().normalize();
    registry.addResourceHandler("/uploads/**")
        .addResourceLocations(uploadPath.toUri().toString())
        .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic());
  }
}
