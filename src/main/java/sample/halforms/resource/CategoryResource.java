package sample.halforms.resource;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import sample.halforms.controller.CategoryController;
import sample.halforms.model.Category;

public class CategoryResource extends Resource<Category> {

	public CategoryResource(Category category) {
		super(category);

		add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategoryController.class).get(category.getId()))
				.withSelfRel());
	}

}
