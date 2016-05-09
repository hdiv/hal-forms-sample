package sample.halforms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.hateoas.IanaRels;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class HdivCurieProvider implements CurieProvider {

	public static final String REL_PARAM = "rel";

	private final Map<String, UriTemplate> curies;

	private final String defaultCurie;

	public HdivCurieProvider(String name, UriTemplate uriTemplate) {
		this(Collections.singletonMap(name, uriTemplate));
	}

	public HdivCurieProvider(Map<String, UriTemplate> curies) {
		this(curies, null);
	}

	public HdivCurieProvider(Map<String, UriTemplate> curies, String defaultCurieName) {
		Assert.notNull(curies, "Curies must not be null!");

		for (Entry<String, UriTemplate> entry : curies.entrySet()) {

			String name = entry.getKey();
			UriTemplate template = entry.getValue();

			Assert.hasText(name, "Curie name must not be null or empty!");
			Assert.notNull(template, "UriTemplate must not be null!");
		}

		this.defaultCurie = StringUtils.hasText(defaultCurieName) ? defaultCurieName
				: curies.size() == 1 ? curies.keySet().iterator().next() : null;
		this.curies = Collections.unmodifiableMap(curies);
	}

	@Override
	public String getNamespacedRelFrom(Link link) {
		return getNamespacedRelFor(link.getRel());
	}

	@Override
	public String getNamespacedRelFor(String rel) {
		boolean prefixingNeeded = defaultCurie != null && !IanaRels.isIanaRel(rel) && !rel.contains(":");
		return prefixingNeeded ? String.format("%s:%s", defaultCurie, rel) : rel;
	}

	@Override
	public Collection<? extends Object> getCurieInformation(Links links) {
		List<Curie> result = new ArrayList<Curie>(curies.size());

		for (Entry<String, UriTemplate> source : curies.entrySet()) {

			String name = source.getKey();
			UriTemplate template = source.getValue();

			result.add(new Curie(name, template.toString()));
		}

		return Collections.unmodifiableCollection(result);
	}

	/**
	 * Value object to get the curie {@link Link} rendered in JSON.
	 * 
	 * @author Oliver Gierke
	 */
	protected static class Curie extends Link {

		private static final long serialVersionUID = 1L;

		private final String name;

		public Curie(String name, String href) {

			super(href, "curies");
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}
