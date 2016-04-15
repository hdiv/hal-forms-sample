package sample.halforms.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sample.halforms.jpa.CategoryRepository;
import sample.halforms.model.Category;
import sample.halforms.resource.CategoryResource;

@RestController
@ExposesResourceFor(Category.class)
@RequestMapping(value = "/categories")
public class CategoryController {

	@Autowired
	private CategoryRepository categories;

	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(method = RequestMethod.GET)
	public Resources<CategoryResource> list() {
		List<CategoryResource> cats = new ArrayList<CategoryResource>();
		for (Category category : categories.findAll()) {
			cats.add(new CategoryResource(category));
		}
		return new Resources<CategoryResource>(cats, entityLinks.linkToCollectionResource(Category.class));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public CategoryResource get(@PathVariable Long id) {
		return new CategoryResource(categories.findOne(id));
	}

}
