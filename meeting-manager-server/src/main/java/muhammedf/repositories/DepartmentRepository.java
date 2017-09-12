package muhammedf.repositories;

import muhammedf.model.Department;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartmentRepository
		extends
			PagingAndSortingRepository<Department, Long> {
}