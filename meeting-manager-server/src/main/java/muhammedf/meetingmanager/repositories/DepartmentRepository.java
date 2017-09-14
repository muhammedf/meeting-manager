package muhammedf.meetingmanager.repositories;

import muhammedf.meetingmanager.model.Department;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface DepartmentRepository
		extends
			PagingAndSortingRepository<Department, Long> {
}