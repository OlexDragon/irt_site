package irt.stock.web.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/webjars/**")
	    .addResourceLocations("/webjars/")
        .resourceChain(true);

	}

//	public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/home").setViewName("home");
//        registry.addViewController("/").setViewName("home");
//        registry.addViewController("/index").setViewName("home");
//        registry.addViewController("/login").setViewName("home");
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
//    }
}