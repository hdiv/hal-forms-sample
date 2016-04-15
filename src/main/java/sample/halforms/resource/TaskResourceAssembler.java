package sample.halforms.resource;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import sample.halforms.controller.TaskController;
import sample.halforms.model.Task;

public class TaskResourceAssembler extends ResourceAssemblerSupport<Task, TaskResource> {

	public TaskResourceAssembler() {
		super(TaskController.class, TaskResource.class);
	}

	@Override
	public TaskResource toResource(Task task) {
		return new TaskResource(task);
	}

}
