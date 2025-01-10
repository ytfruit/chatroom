package com.example.demo.config;

import com.example.demo.token.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Configuration
public class GloabalCorsConfig implements WebMvcConfigurer {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")    //添加映射路径，“/**”表示对所有的路径实行全局跨域访问权限的设置
                        .allowedOriginPatterns("*")    //开放哪些ip、端口、域名的访问权限 SpringBoot2.4.0以后allowedOrigins被allowedOriginPatterns代替
                        .allowCredentials(true)  //是否允许发送Cookie信息
                        .allowedMethods("GET", "POST", "PUT", "DELETE")     //开放哪些Http方法，允许跨域访问
                        .allowedHeaders("*")     //允许HTTP请求中的携带哪些Header信息
                        .exposedHeaders("*");   //暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
            }
            private TokenInterceptor tokenInterceptor;
            //构造方法
            public GloabalCorsConfig(TokenInterceptor tokenInterceptor){
                this.tokenInterceptor = tokenInterceptor;
            }

            @Override
            public void configureAsyncSupport(AsyncSupportConfigurer configurer){
                configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3)));
                configurer.setDefaultTimeout(30000);
            }

            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(tokenInterceptor)
                        .addPathPatterns("/**") // 拦截所有请求
                        .excludePathPatterns("/user/login", "/user/register"); // 排除登录和注册接口
                WebMvcConfigurer.super.addInterceptors(registry);
            }
}
