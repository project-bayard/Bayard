package edu.usm.config;

import edu.usm.Application;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@ComponentScan(basePackageClasses = Application.class, excludeFilters = @Filter({Controller.class, RestController.class,Configuration.class}))
class ApplicationConfig {
	
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocation(new ClassPathResource("/persistence.properties"));
		return ppc;
	}
	
}