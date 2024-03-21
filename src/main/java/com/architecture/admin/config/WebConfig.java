package com.architecture.admin.config;

import com.amazonaws.HttpMethod;
import com.architecture.admin.config.interceptor.JwtInterceptor;
import com.architecture.admin.libraries.filter.Filter;
import com.architecture.admin.libraries.filter.LogFilterLibrary;
import com.architecture.admin.libraries.filter.LoginCheckFilterLibrary;
import com.architecture.admin.libraries.jwt.JwtLibrary;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*****************************************************
 * 필터 등록
 ****************************************************/
@Configuration
public class WebConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods(
                        HttpMethod.HEAD.name(),
                        HttpMethod.POST.name(),
                        HttpMethod.PUT.name(),
                        HttpMethod.PATCH.name(),
                        HttpMethod.DELETE.name()
                );
    }

    @Bean
    public FilterRegistrationBean<Filter> logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilterLibrary()); // 등록 할 필터를 지정
        filterRegistrationBean.setOrder(1);  // 순서가 낮을수록 먼저 동작한다.
        filterRegistrationBean.addUrlPatterns("/*"); // 필터를 적용할 URL 패턴을 지정

        return filterRegistrationBean;
    }

    @Override
    public void addInterceptors(InterceptorRegistry reg) {
        reg.addInterceptor(jwtInterceptor())
                .order(1)
                .addPathPatterns("/**");
    }


    @Bean
    public FilterRegistrationBean<Filter> loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilterLibrary());
        filterRegistrationBean.setOrder(2);
        filterRegistrationBean.addUrlPatterns("/*");

        return filterRegistrationBean;
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        JwtLibrary jwtLibrary = new JwtLibrary();
        return new JwtInterceptor(jwtLibrary);
    }
}
