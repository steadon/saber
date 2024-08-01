package com.steadon.saber.config;

import com.steadon.saber.handler.interceptor.TokenInterceptor;
import com.steadon.saber.handler.interceptor.TraceIdInterceptor;
import com.steadon.saber.handler.interceptor.auth.ROOTInterceptor;
import com.steadon.saber.handler.interceptor.auth.SIPCInterceptor;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@AllArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private TokenInterceptor tokenInterceptor;
    private ROOTInterceptor rootInterceptor;
    private SIPCInterceptor sipcInterceptor;
    private TraceIdInterceptor traceIdInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/saber/login")
                .excludePathPatterns("/saber/app/access");
        registry.addInterceptor(sipcInterceptor)
                .addPathPatterns("/saber/log")
                .addPathPatterns("/saber/log/histogram");
        registry.addInterceptor(rootInterceptor)
                .addPathPatterns("/saber/admin/list")
                .addPathPatterns("/saber/app/add")
                .addPathPatterns("/saber/app/delete")
                .addPathPatterns("/saber/app/update")
                .addPathPatterns("/saber/app/list")
                .addPathPatterns("/saber/app/bind")
                .addPathPatterns("/saber/app/secret/update");
        registry.addInterceptor(traceIdInterceptor)
                .addPathPatterns("/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST")
                .maxAge(3600)
                .allowedHeaders("*");
    }
}
