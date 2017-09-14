package muhammedf.meetingmanager.repositories;

import muhammedf.meetingmanager.model.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository
        extends CrudRepository<Employee, Long> {
}
