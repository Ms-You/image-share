package com.share.image.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("C:/Users/명수/Documents/공부/스프링/image/profile_imgs/")
    private String profileUploadFolder;

    @Value("C:/Users/명수/Documents/공부/스프링/image/tag_imgs/")
    private String tagUploadFolder;

    @Value("C:/Users/명수/Documents/공부/스프링/image/feed_imgs/")
    private String feedUploadFolder;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);

        registry
                .addResourceHandler("/profile_imgs/**")
                .addResourceLocations("file:///" + profileUploadFolder)
                .setCachePeriod(60 * 10 * 6)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        registry
                .addResourceHandler("/tag_imgs/**")
                .addResourceLocations("file:///" + tagUploadFolder)
                .setCachePeriod(60 * 10 * 6)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

        registry
                .addResourceHandler("/feed_imgs/**")
                .addResourceLocations("file:///" + feedUploadFolder)
                .setCachePeriod(60 * 10 * 6)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());


    }
}
