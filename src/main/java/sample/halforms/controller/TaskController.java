package sample.halforms.controller;

import static de.escalon.hypermedia.spring.AffordanceBuilder.linkTo;
import static de.escalon.hypermedia.spring.AffordanceBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.escalon.hypermedia.spring.AffordanceBuilder;
import sample.halforms.jpa.CategoryRepository;
import sample.halforms.model.Task;
import sample.halforms.model.TaskFilter;
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

	@RequestMapping(method = RequestMethod.GET)
	public Resources<TaskResource> list() {
		Link link = linkTo(TaskController.class).withSelfRel();
		Link linkFilter = linkTo(methodOn(TaskController.class).filter(new TaskFilter())).withRel("filter");

		AffordanceBuilder formBuilder = linkTo(methodOn(TaskController.class).create(new Task()));

		return new Resources<TaskResource>(new TaskResourceAssembler().toResources(taskService.findAll()), link,
				linkFilter, formBuilder.withRel("tasks"));
	}

	@RequestMapping(method = RequestMethod.HEAD)
	public ResponseEntity<?> head() {

		HttpHeaders headers = new HttpHeaders();

		Link linkCreate = linkTo(methodOn(TaskController.class).create(new Task())).withRel("create-task");
		headers.add("Link", new Links(linkCreate).toString());

		return new ResponseEntity<Object>(headers, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public TaskResource read(@PathVariable Long id) {
		return new TaskResourceAssembler().toResource(taskService.findOne(id));
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.HEAD)
	public ResponseEntity<?> headEdit(@PathVariable Long id) {

		HttpHeaders headers = new HttpHeaders();

		Link linkCreate = linkTo(methodOn(TaskController.class).edit(id, new Task())).withRel("update-task");
		headers.add("Link", new Links(linkCreate).toString());

		return new ResponseEntity<Object>(headers, HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/{id}/edit-form", method = RequestMethod.GET, produces = "application/prs.hal-forms+json")
	public ResourceSupport editForm(@PathVariable Long id) {

		AffordanceBuilder formBuilder = linkTo(methodOn(TaskController.class).edit(id, new Task()));
		Link link = linkTo(methodOn(TaskController.class).editForm(id)).and(formBuilder).withSelfRel();

		ResourceSupport halForm = new ResourceSupport();
		halForm.add(link);
		return halForm;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> edit(@PathVariable Long id, @RequestBody Task task) {
		taskService.update(id, task);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", params = "cancel", method = RequestMethod.PUT)
	public ResponseEntity<?> edit(@PathVariable Long id, @RequestParam String cancel, @RequestBody Task task) {
		taskService.update(id, task);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}/completed", method = RequestMethod.PUT)
	public TaskResource markAsCompleted(@PathVariable Long id) {
		return new TaskResourceAssembler().toResource(taskService.markAsCompleted(id));
	}

	@RequestMapping(value = "/{id}/uncompleted", method = RequestMethod.PUT)
	public TaskResource markAsUncompleted(@PathVariable Long id) {
		return new TaskResourceAssembler().toResource(taskService.markAsUncompleted(id));
	}

	@RequestMapping(method = RequestMethod.POST)
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

	@RequestMapping(value = "/filtered", method = RequestMethod.GET)
	public ResponseEntity<?> filter(TaskFilter filter) {
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
