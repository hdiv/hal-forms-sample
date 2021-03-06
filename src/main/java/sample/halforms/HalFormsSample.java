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
			List<Category> categories = Arrays.asList(new Category("shopping", "Going shopping"),
					new Category("family", "Family things"), new Category("hobbies", "Hobbies"));
			categoryRepository.save(categories);

			taskRepository.save(new Task("take some photos", categories.get(2)));
		};
	}

	@Bean
	public CurieProvider curieProvider() {
		return new HdivCurieProvider("halforms", new UriTemplate("{href}{?rel}"));
	}

	public static void main(String[] args) {
		SpringApplication.run(HalFormsSample.class, args);
	}
}
