package sample.halforms.model;

import sample.halforms.model.Task.Priority;

public class TaskFilter {
	private Priority priority;

	private boolean completed;

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

}
