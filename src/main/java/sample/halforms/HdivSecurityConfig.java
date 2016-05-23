package sample.halforms;

import java.util.ArrayList;
import java.util.List;

import org.hdiv.config.HDIVConfig;
import org.hdiv.config.annotation.EnableHdivWebSecurity;
import org.hdiv.config.annotation.ExclusionRegistry;
import org.hdiv.config.annotation.ValidationConfigurer;
import org.hdiv.config.annotation.builders.SecurityConfigBuilder;
import org.hdiv.config.annotation.configuration.HdivWebSecurityConfigurerAdapter;
import org.hdiv.dataComposer.DataComposerFactory;
import org.hdiv.dataValidator.IDataValidator;
import org.hdiv.filter.IValidationHelper;
import org.hdiv.filter.ValidatorErrorHandler;
import org.hdiv.filter.ValidatorFilter;
import org.hdiv.idGenerator.UidGenerator;
import org.hdiv.init.DefaultRequestInitializer;
import org.hdiv.init.RequestInitializer;
import org.hdiv.listener.InitListener;
import org.hdiv.session.ISession;
import org.hdiv.state.StateUtil;
import org.hdiv.state.scope.StateScopeManager;
import org.hdiv.urlProcessor.BasicUrlProcessor;
import org.hdiv.web.hateoas.DefaultRelationTypeRegistry;
import org.hdiv.web.hateoas.DefaultRequestMappingHandlerMappingProcessor;
import org.hdiv.web.hateoas.DefaultRequestMappingRegistry;
import org.hdiv.web.hateoas.RelationTypeRegistry;
import org.hdiv.web.hateoas.RequestMappingHandlerMappingConfiguration;
import org.hdiv.web.hateoas.RequestMappingHandlerMappingProcessor;
import org.hdiv.web.hateoas.RequestMappingRegistry;
import org.hdiv.web.hateoas.ResourceRequestDataValueProcessor;
import org.hdiv.web.hateoas.dataComposer.HateoasDataComposerFactory;
import org.hdiv.web.hateoas.error.HateoasValidationErrorHander;
import org.hdiv.web.hateoas.filter.HttpRequestBodyValidator;
import org.hdiv.web.hateoas.filter.JsonHttpRequestBodyValidator;
import org.hdiv.web.hateoas.filter.ValidatorHelperRestRequest;
import org.hdiv.web.hateoas.init.RequestBodyReaderRequestInitializer;
import org.hdiv.web.hateoas.state.AliasRegistry;
import org.hdiv.web.hateoas.state.DefaultAliasRegistry;
import org.hdiv.web.hateoas.state.DefaultHdivMappingRegistry;
import org.hdiv.web.hateoas.state.DefaultStateUpdater;
import org.hdiv.web.hateoas.state.HdivMappingRegistry;
import org.hdiv.web.hateoas.state.JsonSchemaParameterFactory;
import org.hdiv.web.hateoas.state.ParameterFactory;
import org.hdiv.web.hateoas.state.StateUpdater;
import org.hdiv.web.hateoas.support.DefaultResourceLinkProcessor;
import org.hdiv.web.hateoas.support.DefaultResourceTemplateProcessor;
import org.hdiv.web.hateoas.support.ResourceLinkProcessor;
import org.hdiv.web.hateoas.support.ResourceTemplateProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableHdivWebSecurity
public class HdivSecurityConfig extends HdivWebSecurityConfigurerAdapter {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private RequestDataValueProcessor requestDataValueProcessor;

	@Autowired
	private HDIVConfig hdivConfig;

	@Autowired
	private StateUtil stateUtil;

	// requestValidationHelper

	@Autowired
	private IDataValidator dataValidator;

	@Autowired
	private ISession session;

	@Autowired
	private StateScopeManager stateScopeManager;

	@Autowired
	private BasicUrlProcessor urlProcessor;

	// RequestValidationHelper

	@Autowired
	private UidGenerator uidGenerator;

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Autowired
	private RelProvider relProvider;

	@Autowired
	private CurieProvider curieProvider;

	@Override
	public void configure(SecurityConfigBuilder builder) {

		builder.confidentiality(false).sessionExpired().homePage("/").loginPage("/login");
		builder.showErrorPageOnEditableValidation(true);
		builder.reuseExistingPageInAjaxRequest(true);
	}

	@Override
	public void addExclusions(ExclusionRegistry registry) {
		registry.addUrlExclusions("/browser/.*");
	}

	@Override
	public void configureEditableValidation(ValidationConfigurer validationConfigurer) {
		validationConfigurer.addValidation("/.*");
	}

	@Bean
	public InitListener initListener() {
		return new InitListener();
	}

	@Bean
	public ValidatorFilter validatorFilter() {
		return new ValidatorFilter();
	}

	@Bean
	@Qualifier("org.hdiv.filter.IValidationHelper")
	public IValidationHelper requestValidationHelper() {
		ValidatorHelperRestRequest validatorHelperRequest = new ValidatorHelperRestRequest();
		validatorHelperRequest.setHdivConfig(hdivConfig);
		validatorHelperRequest.setDataComposerFactory(dataComposerFactory());
		validatorHelperRequest.setDataValidator(dataValidator);
		validatorHelperRequest.setSession(session);
		validatorHelperRequest.setStateScopeManager(stateScopeManager);
		validatorHelperRequest.setStateUtil(stateUtil);
		validatorHelperRequest.setUrlProcessor(urlProcessor);

		List<HttpRequestBodyValidator> validators = new ArrayList<HttpRequestBodyValidator>();
		validatorHelperRequest.setValidators(validators);

		validators.add(new JsonHttpRequestBodyValidator(objectMapper, requestMappingRegistry(),
				hdivConfig.getEditableDataValidationProvider()));
		return validatorHelperRequest;

	}

	@Bean
	public DataComposerFactory dataComposerFactory() {
		HateoasDataComposerFactory factory = new HateoasDataComposerFactory();
		factory.setConfig(hdivConfig);
		factory.setSession(session);
		factory.setUidGenerator(uidGenerator);
		factory.setStateScopeManager(stateScopeManager);
		factory.setStateUtil(stateUtil);
		return factory;
	}

	@Bean
	public AliasRegistry aliasRegistry() {
		return new DefaultAliasRegistry();
	}

	@Bean
	public HdivMappingRegistry hdivMappingRegistry() {
		DefaultHdivMappingRegistry lrr = new DefaultHdivMappingRegistry(aliasRegistry(), requestMappingRegistry());
		return lrr;
	}

	@Bean
	public RelationTypeRegistry relationTypeRegistry() {
		RelationTypeRegistry rtr = new DefaultRelationTypeRegistry();
		return rtr;
	}

	@Bean
	public StateUpdater stateUpdater() {
		return new DefaultStateUpdater(stateUtil, aliasRegistry(), hdivConfig, objectMapper);
	}

	@Bean
	public ConversionService defaultConversionService() {
		return new DefaultFormattingConversionService();
	}

	@Bean
	public ParameterFactory parameterFactory() {
		return new JsonSchemaParameterFactory(objectMapper, aliasRegistry(), defaultConversionService());
	}

	@Bean
	public RequestMappingRegistry requestMappingRegistry() {
		return new DefaultRequestMappingRegistry(relationTypeRegistry());
	}

	@Bean
	public ResourceLinkProcessor resourceLinkProcessor() {
		return new DefaultResourceLinkProcessor(requestDataValueProcessor, requestMappingRegistry(), objectMapper);
	}

	@Bean
	public ResourceRequestDataValueProcessor resourceRequestDataValueProcessor() {
		return new ResourceRequestDataValueProcessor();
	}

	@Bean
	public ValidatorErrorHandler validatorErrorHandler() {
		return new HateoasValidationErrorHander(objectMapper);
	}

	@Bean
	public RequestInitializer securityRequestInitializer() {
		DefaultRequestInitializer ri = new RequestBodyReaderRequestInitializer(requestMappingRegistry());
		ri.setConfig(hdivConfig);
		return ri;
	}

	@Bean
	public ResourceTemplateProcessor resourceTemplateProcessor() {
		return new DefaultResourceTemplateProcessor(stateUpdater(), requestDataValueProcessor, objectMapper, hdivConfig,
				relProvider, curieProvider);
	}

	@Bean
	public RequestMappingHandlerMappingProcessor requestMappingHandlerMappingProcessor() {
		return new DefaultRequestMappingHandlerMappingProcessor(requestMappingHandlerMapping,
				requestMappingHandlerMappingConfiguration(), parameterFactory(), aliasRegistry(), hdivMappingRegistry(),
				requestMappingRegistry());
	}

	@Bean
	public RequestMappingHandlerMappingConfiguration requestMappingHandlerMappingConfiguration() {
		return new RequestMappingHandlerMappingConfiguration();
	}
}
