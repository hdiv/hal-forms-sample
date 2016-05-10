package sample.halforms;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.hdiv.web.hateoas.servlet.support.ServicesHdivRequestDataValueProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.escalon.hypermedia.spring.halforms.HalFormsMessageConverter;
import sample.halforms.model.Category;

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

	@Autowired
	private RepositoryRestConfiguration restConfig;

	@Override
	public void configureMessageConverters(final List<HttpMessageConverter<?>> converters) {
		converters.add(halFormsMessageConverter());
		/**
		 * TODO Ugly ugly workaround to modify location attributes in the header when the body is empty
		 */
		converters.add(new TypeConstrainedMappingJackson2HttpMessageConverter(Object.class) {

			@Override
			public boolean canRead(final Class<?> clazz, final MediaType mediaType) {
				return Object.class == clazz;
			}

			@Override
			public boolean canRead(final Type type, final Class<?> contextClass, final MediaType mediaType) {
				return Object.class == getJavaType(type, contextClass).getRawClass();
			}

			@Override
			public boolean canWrite(final Class<?> clazz, final MediaType mediaType) {
				return Object.class == clazz;
			}
		});
	}

	@PostConstruct
	public void configRest() {
		restConfig.exposeIdsFor(Category.class);
	}

	@Bean
	public HalFormsMessageConverter halFormsMessageConverter() {

		HalFormsMessageConverter converter = new HalFormsMessageConverter(objectMapper, relProvider, curieProvider,
				resourceDescriptionMessageSourceAccessor);
		converter.setSupportedMediaTypes(Arrays.asList(MediaType.parseMediaType("application/prs.hal-forms+json")));
		return converter;
	}

	@Bean
	public ServicesHdivRequestDataValueProcessor dataValueProcessor() {
		return new ServicesHdivRequestDataValueProcessor();
	}

}
