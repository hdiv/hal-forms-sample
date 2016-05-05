package sample.halforms.model;

import java.util.Arrays;

import de.escalon.hypermedia.action.Options;
import de.escalon.hypermedia.affordance.Suggest;
import de.escalon.hypermedia.affordance.SuggestImpl;
import de.escalon.hypermedia.affordance.SuggestType;

public class CategoryOptions implements Options<Category> {

	@Override
	public Suggest<Category>[] get(SuggestType type, String[] value, Object... args) {

		return SuggestImpl.wrap(Arrays.asList(new Category("big", "Big"), new Category("small", "Small")), "name",
				"description", SuggestType.EXTERNAL);
	}

}
