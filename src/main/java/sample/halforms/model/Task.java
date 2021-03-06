package sample.halforms.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.escalon.hypermedia.action.Input;
import de.escalon.hypermedia.action.Select;
import de.escalon.hypermedia.affordance.SuggestType;

@Entity
@Relation(value = "task", collectionRelation = "tasks")
public class Task extends AbstractEntity {

	private String description;

	private Priority priority = Priority.MEDIUM;

	private boolean completed = false;

	private LocalDateTime completedAt;

	@ManyToOne
	private Category category;

	public Task() {
	}

	public Task(String description, Category category) {
		this.description = description;
		this.category = category;
	}

	@JsonCreator
	public Task(@JsonProperty("description") String description, @JsonProperty("priority") Priority priority,
			@JsonProperty("category") Category category) {
		this.description = description;
		this.priority = priority;
		this.category = category;
	}

	public void markAsCompleted() {
		this.completed = true;
		this.completedAt = LocalDateTime.now(ZoneId.of("UTC"));
	}

	public void markAsUncompleted() {
		this.completed = false;
		this.completedAt = null;
	}

	public void setDescription(@Input(required = true, editable = true) String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return completed;
	}

	public LocalDateTime getCompletedAt() {
		return completedAt;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(@Select(options = CategoryOptions.class, type = SuggestType.EXTERNAL) Category category) {
		this.category = category;
	}

	public enum Priority {
		HIGH, MEDIUM, LOW
	}
}
