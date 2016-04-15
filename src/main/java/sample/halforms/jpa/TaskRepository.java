package sample.halforms.jpa;

import java.util.Collection;

import org.springframework.data.repository.PagingAndSortingRepository;

import sample.halforms.model.Task;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long> {
	Collection<Task> findByCategoryId(Long id);
}
