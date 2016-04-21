package sample.halforms.resource;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import sample.halforms.controller.TaskController;
import sample.halforms.model.Task;

public class TaskResource extends Resource<Task> {

	TaskResource(Task task) {
		super(task);

		add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).read(task.getId()))
				.withSelfRel());

		add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).editForm(task.getId()))
				.withRel("edit-form"));

		add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).list())
				.withRel("previous"));
		add(ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).markAsCompleted(task.getId()))
				.withRel("complete"));
		add(ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).markAsUncompleted(task.getId()))
				.withRel("uncomplete"));

	}

}
