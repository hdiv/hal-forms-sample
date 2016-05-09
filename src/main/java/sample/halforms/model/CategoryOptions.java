package sample.halforms.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Component;

import de.escalon.hypermedia.action.Options;
import de.escalon.hypermedia.affordance.Suggest;
import de.escalon.hypermedia.affordance.SuggestImpl;
import de.escalon.hypermedia.affordance.SuggestType;
import de.escalon.hypermedia.spring.AffordanceBuilder;
import sample.halforms.controller.CategoryController;
import sample.halforms.jpa.CategoryRepository;

@Component
public class CategoryOptions implements Options<Resource<Category>> {

	@Autowired
	private CategoryRepository categories;

	@Override
	public Suggest<Resource<Category>>[] get(SuggestType type, String[] value, Object... args) {
		List<Resource<Category>> cats = new ArrayList<Resource<Category>>();
		Iterator<Category> iterator = categories.findAll().iterator();
		while (iterator.hasNext()) {
			Resource<Category> resource = new Resource<Category>(iterator.next());
			resource.add(AffordanceBuilder
					.linkTo(AffordanceBuilder.methodOn(CategoryController.class).get(resource.getContent().getId()))
					.withSelfRel());
			cats.add(resource);
		}
		return SuggestImpl.wrap(cats, "name", "description", type);
	}

}
