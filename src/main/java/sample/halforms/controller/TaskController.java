package sample.halforms.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.forms.Form;
import org.springframework.hateoas.forms.TemplatedResource;
import org.springframework.hateoas.forms.TemplatedResources;
import org.springframework.hateoas.mvc.ControllerFormBuilder;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sample.halforms.jpa.CategoryRepository;
import sample.halforms.model.Task;
import sample.halforms.model.Task.Priority;
import sample.halforms.resource.TaskResource;
import sample.halforms.resource.TaskResourceAssembler;
import sample.halforms.service.TaskService;

@RestController
@ExposesResourceFor(Task.class)
@RequestMapping(value = "/tasks", produces = { "application/json" })
public class TaskController {

	@Autowired
	private TaskService taskService;
	
	@Autowired
	private CategoryRepository categories;

	@Autowired
	private EntityLinks entityLinks;

	@RequestMapping(method = RequestMethod.GET)
	public TemplatedResources<TaskResource> list() {
		Link link = linkTo(TaskController.class).withSelfRel();

		ControllerFormBuilder formBuilder = ControllerFormBuilder
				.formTo(ControllerFormBuilder.methodOn(TaskController.class).create(new Task()));

		formBuilder.property("description").readonly(false);
		formBuilder.property("priority").suggest().values(Priority.values());
		Link categoriesLink = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(CategoryController.class).list()).withRel("categories");
		formBuilder.property("category").suggest().link(categoriesLink);

		Form form = formBuilder.withDefaultKey();

		return new TemplatedResources<TaskResource>(new TaskResourceAssembler().toResources(taskService.findAll()), link, form);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TaskResource read(@PathVariable Long id) {
		return new TaskResourceAssembler().toResource(taskService.findOne(id));
	}
	
	@RequestMapping(value = "/{id}/edit-form", method = RequestMethod.GET)
	public TemplatedResource<Task> editForm(@PathVariable Long id) {
		
		Link link = ControllerLinkBuilder
				.linkTo(ControllerLinkBuilder.methodOn(TaskController.class).read(id)).withSelfRel();
		
		ControllerFormBuilder formBuilder = ControllerFormBuilder
				.formTo(ControllerFormBuilder.methodOn(TaskController.class).create(new Task()));

		formBuilder.property("description").readonly(false);
		formBuilder.property("priority").suggest().values(Priority.values());
		formBuilder.property("category").suggest().embedded(categories.findAll());

		Form form = formBuilder.withDefaultKey();
		
		return new TemplatedResource<Task>(taskService.findOne(id), link, form);
	}

	@RequestMapping(value = "/{id}/completed", method = RequestMethod.PUT)
	public TaskResource markAsCompleted(@PathVariable Long id) {
		return new TaskResourceAssembler().toResource(taskService.markAsCompleted(id));
	}

	@RequestMapping(value = "/{itemId}/uncompleted", method = RequestMethod.PUT)
	public TaskResource markAsUncompleted(@PathVariable Long id) {
		return new TaskResourceAssembler().toResource(taskService.markAsUncompleted(id));
	}

	@RequestMapping(method = RequestMethod.POST, consumes = { "application/json" })
	public ResponseEntity<?> create(@RequestBody Task task) {
		taskService.save(task);
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setLocation(linkTo(methodOn(TaskController.class).read(task.getId())).toUri());
		return new ResponseEntity<Object>(responseHeaders, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable Long id) {
		taskService.delete(id);
		return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
	}
}
