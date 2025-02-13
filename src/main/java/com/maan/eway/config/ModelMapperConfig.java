/**
 * @author : Ashok Kumar S 
 * @since  : 10-02-2025
 */
package com.maan.eway.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
	
	@Bean
	ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
              .setFieldMatchingEnabled(true)
              .setAmbiguityIgnored(true);
        return mapper;
	}

}
