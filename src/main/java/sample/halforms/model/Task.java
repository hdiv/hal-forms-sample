package sample.halforms.model;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.hateoas.core.Relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import de.escalon.hypermedia.action.Input;

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
	public Task(@Input(required = true) @JsonProperty("description") String description,
			@JsonProperty("priority") Priority priority, @JsonProperty("category") Category category) {
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

	public void setDescription(String description) {
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

	public void setCategory(Category category) {
		this.category = category;
	}

	public enum Priority {
		HIGH, MEDIUM, LOW
	}
}
