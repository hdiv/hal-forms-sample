package sample.halforms.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Relation(value = "category", collectionRelation = "categories")
public class Category extends AbstractEntity {

	private String name;

	private String description;

	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY, orphanRemoval = true)
	@JsonIgnore
	private Set<Task> tasks;

	public Category() {
	}

	public Category(String name) {
		this.name = name;
	}

	public Category(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
