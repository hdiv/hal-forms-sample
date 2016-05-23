package sample.halforms.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sample.halforms.EntityNotFoundException;
import sample.halforms.jpa.CategoryRepository;
import sample.halforms.jpa.TaskRepository;
import sample.halforms.model.Category;
import sample.halforms.model.Task;

@Component
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	public Collection<Task> findByCategoryId(Long categoryId) {
		return taskRepository.findByCategoryId(categoryId);
	}

	public Iterable<Task> findAll() {
		return taskRepository.findAll();
	}

	public Task findOne(Long id) {
		return taskRepository.findOne(id);
	}

	public Task markAsCompleted(Long id) {
		Task item = taskRepository.findOne(id);
		if (item != null) {
			item.markAsCompleted();
			save(item);
		}
		return item;
	}

	public Task markAsUncompleted(Long id) {
		Task item = taskRepository.findOne(id);
		if (item != null) {
			item.markAsUncompleted();
			save(item);
		}
		return item;
	}

	public Task save(Task task) {
		Category category = categoryRepository.findOne(task.getCategory().getId());
		if (category == null) {
			throw new EntityNotFoundException(String.format("Category with id %s not found", task.getCategory().getId()));
		}
		task.setCategory(category);
		return taskRepository.save(task);
	}

	public void delete(Long id) {
		taskRepository.delete(id);
	}

	public void update(Long id, Task task) {
		Task savedTask = taskRepository.findOne(id);
		savedTask.setDescription(task.getDescription());
		Category category = categoryRepository.findOne(task.getCategory().getId());
		if (category == null) {
			throw new EntityNotFoundException(String.format("Category with id %s not found", task.getCategory().getId()));
		}
		savedTask.setCategory(category);
		savedTask.setPriority(task.getPriority());
		if (task.isCompleted()) {
			savedTask.markAsCompleted();
		}
		else if (savedTask.isCompleted()) {
			savedTask.markAsUncompleted();
		}
		taskRepository.save(savedTask);
	}
}
