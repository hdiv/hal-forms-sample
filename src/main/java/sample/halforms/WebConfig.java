package sample.halforms;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.escalon.hypermedia.spring.halforms.HalFormsMessageConverter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RelProvider relProvider;

	@Autowired
	private CurieProvider curieProvider;

	@Autowired
	private MessageSourceAccessor resourceDescriptionMessageSourceAccessor;

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(halFormsMessageConverter());
	}

	@Bean
	public HalFormsMessageConverter halFormsMessageConverter() {

		HalFormsMessageConverter converter = new HalFormsMessageConverter(objectMapper, relProvider, curieProvider,
				resourceDescriptionMessageSourceAccessor);
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("application/prs.hal-forms+json")));
		return converter;
	}

}
