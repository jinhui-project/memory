package com.jinhui.common.resolver;

import com.jinhui.common.resolver.JsonParamArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * @autor wsc
 * @create 2018-04-02 15:24
 **/
@Configuration
public class WebMvcAdapter extends WebMvcConfigurerAdapter {

    //token拦截器
   /* public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor()).excludePathPatterns("/swagger-resources*//**");
    }
*/
    //注册自定义的参数解析器
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new JsonParamArgumentResolver());
    }
}
