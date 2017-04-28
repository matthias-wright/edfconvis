package de.rwthaachen.mi.convis.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.TimeUnit;

@EnableAutoConfiguration
@Configuration
@EnableWebMvc
@EnableCaching
public class WebAppConfig extends WebMvcConfigurerAdapter {


    /**
     * Adds resource handler for handling static content URLs
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/res/**")
                .addResourceLocations("/resources/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.SECONDS).mustRevalidate())
                .resourceChain(true);
    }

    /**
     * Defines simple views for several URIs without defining controller methods to handle the requests
     *
     * Helpfulness score: *****
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/home").setViewName("main");
    }

    @Controller
    static class FaviconController {
        @RequestMapping("favicon.png")
        String favicon() {
            return "forward:/res/images/favicon.png";
        }
    }

}
