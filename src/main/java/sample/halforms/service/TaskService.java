package sample.halforms.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sample.halforms.jpa.TaskRepository;
import sample.halforms.model.Task;

@Component
public class TaskService {

	@Autowired
	private TaskRepository taskRepository;

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

	public Task save(Task item) {
		return taskRepository.save(item);
	}

	public void delete(Long id) {
		taskRepository.delete(id);
	}

	public void update(Long id, Task task) {
		Task savedTask = taskRepository.findOne(id);
		savedTask.setDescription(task.getDescription());
		savedTask.setCategory(task.getCategory());
		savedTask.setPriority(task.getPriority());
		taskRepository.save(savedTask);
	}
}
