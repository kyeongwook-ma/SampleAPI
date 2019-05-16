package com.recsys.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration

public class CommonConfig {


    @Bean
    DispatcherServlet dispatcherServlet() {
        DispatcherServlet ds = new DispatcherServlet();
        ds.setThrowExceptionIfNoHandlerFound(true);
        ds.setDetectAllHandlerExceptionResolvers(true);
        return ds;
    }
}

