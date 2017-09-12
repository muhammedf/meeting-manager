package muhammedf.repositories;

import muhammedf.model.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository
        extends CrudRepository<Employee, Long> {
}
