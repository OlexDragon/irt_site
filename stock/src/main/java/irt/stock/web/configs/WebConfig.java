package irt.stock.web.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import irt.stock.data.jpa.services.converters.EcoStatusConverter;
import irt.stock.data.jpa.services.converters.EcrCategoryConverter;
import irt.stock.data.jpa.services.converters.EcrStatusConverter;

@Configuration
@EnableTransactionManagement
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    registry.addResourceHandler("/webjars/**")
	    .addResourceLocations("/webjars/")
        .resourceChain(true);

	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new EcrCategoryConverter());
		registry.addConverter(new EcrStatusConverter());
		registry.addConverter(new EcoStatusConverter());
	}
}