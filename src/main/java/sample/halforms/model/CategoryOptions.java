package sample.halforms.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.escalon.hypermedia.action.Options;
import de.escalon.hypermedia.affordance.Suggest;
import de.escalon.hypermedia.affordance.SuggestImpl;
import de.escalon.hypermedia.affordance.SuggestType;
import sample.halforms.jpa.CategoryRepository;

@Component
public class CategoryOptions implements Options<Category> {

	@Autowired
	private CategoryRepository categories;

	@Override
	public Suggest<Category>[] get(SuggestType type, String[] value, Object... args) {
		List<Category> cats = new ArrayList<Category>();
		Iterator<Category> iterator = categories.findAll().iterator();
		while (iterator.hasNext()) {
			cats.add(iterator.next());
		}
		return SuggestImpl.wrap(cats, "name", "description", type);
	}

}
