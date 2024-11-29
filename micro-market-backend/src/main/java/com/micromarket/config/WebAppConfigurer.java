package com.micromarket.config;

import com.micromarket.interceptor.SysInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    /**
     * 跨域解决
     * */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT",
                        "DELETE","OPTIONS")
                .maxAge(3600);
    }


    /**
     * 拦截器
     * */
    @Bean
    public SysInterceptor sysInterceptor(){
        return new SysInterceptor();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns=new String[]{"/adminLogin","/product/**","/bigtype/**","/user/wxlogin","/weixin/**","/login","/image/**"};
        registry.addInterceptor(sysInterceptor()).addPathPatterns("/**").excludePathPatterns(patterns);
//        WebMvcConfigurer.super.addInterceptors(registry);
    }

    /**
     * 本地静态资源
     * */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/image/swiper/**").addResourceLocations("file:E:\\images\\micro_mart\\images\\swiperImages\\");
        registry.addResourceHandler("/image/bigtype/**").addResourceLocations("file:E:\\images\\micro_mart\\images\\bigTypeImages\\");
        registry.addResourceHandler("/image/product/**").addResourceLocations("file:E:\\images\\micro_mart\\images\\productImages\\");
        registry.addResourceHandler("/image/productSwiperImage/**").addResourceLocations("file:E:\\images\\micro_mart\\images\\productSwiperImages\\");
        registry.addResourceHandler("/image/productIntroImage/**").addResourceLocations("file:E:\\images\\micro_mart\\images\\productIntroImages\\");
        registry.addResourceHandler("/image/productParaImage/**").addResourceLocations("file:E:\\images\\micro_mart\\images\\productParaImages\\");
        registry.addResourceHandler("/audio/**").addResourceLocations("file:E:\\images\\micro_mart\\audio\\");
    }
}
