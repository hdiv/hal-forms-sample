package sample.halforms;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.Jackson2HalFormsModule;
import org.springframework.hateoas.hal.Jackson2HalFormsModule.HalFormsHandlerInstantiator;

import com.fasterxml.jackson.databind.ObjectMapper;

import sample.halforms.model.Category;

@Configuration
public class JacksonCustomizations {

	@Autowired
	private ObjectMapper halObjectMapper;

	@Autowired
	private RelProvider relProvider;

	@Autowired
	private CurieProvider curieProvider;

	@Autowired
	private MessageSourceAccessor resourceDescriptionMessageSourceAccessor;

	@Autowired
	private RepositoryRestConfiguration restConfig;

	@PostConstruct
	public void addHalFormsModule() {

		halObjectMapper.registerModule(new Jackson2HalFormsModule());
		halObjectMapper.setHandlerInstantiator(new HalFormsHandlerInstantiator(relProvider, curieProvider,
				resourceDescriptionMessageSourceAccessor, true));

		restConfig.exposeIdsFor(Category.class);
	}
}