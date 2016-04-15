package sample.halforms;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;

import sample.halforms.jpa.CategoryRepository;
import sample.halforms.jpa.TaskRepository;
import sample.halforms.model.Category;
import sample.halforms.model.Task;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@EnableHypermediaSupport(type = { HypermediaType.HAL })
public class HalFormsSample {

	@Bean
	CommandLineRunner init(CategoryRepository categoryRepository, TaskRepository taskRepository) {
		return (evt) -> {
			List<Category> categories = Arrays.asList(new Category("shopping"), new Category("family"),
					new Category("hobbies"));
			categoryRepository.save(categories);

			taskRepository.save(new Task("take some photos", categories.get(2)));
		};
	}

	@Bean
	public CurieProvider curieProvider() {
		return new DefaultCurieProvider("task", new UriTemplate("http://localhost:8080/rels/{rel}"));
	}

	public static void main(String[] args) {
		SpringApplication.run(HalFormsSample.class, args);
	}
}
