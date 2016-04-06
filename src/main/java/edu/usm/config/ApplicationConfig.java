package edu.usm.config;

import edu.usm.Application;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.context.annotation.ComponentScan.Filter;

@Configuration
@EnableSpringDataWebSupport
@ComponentScan(basePackageClasses = Application.class, excludeFilters = @Filter({Controller.class, RestController.class,Configuration.class}))
class ApplicationConfig {
	
	@Bean
	public static PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		ppc.setLocations(new ClassPathResource("/application.properties"), new ClassPathResource("/config.properties"));
		return ppc;
	}
	
}