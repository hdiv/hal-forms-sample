package sample.halforms.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;

import sample.halforms.model.Category;

public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {
	Category findByName(String name);
}
