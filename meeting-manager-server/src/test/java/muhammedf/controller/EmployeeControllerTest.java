package muhammedf.controller;

import muhammedf.model.Employee;
import muhammedf.repositories.EmployeeRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeControllerTest extends AbstractCRUDControllerTest<Employee, Long> {

    @InjectMocks
    private EmployeeController ec;

    @Mock
    private EmployeeRepository er;

    @Override
    public AbstractCRUDController getController() {
        return ec;
    }

    @Override
    public CrudRepository getRepository() {
        return er;
    }

    @Override
    public Class<Employee> getTClass() {
        return Employee.class;
    }

    @Override
    public Employee getNewTInstance() {
        return new Employee();
    }

    private Long lastInstance = 0l;

    @Override
    public Long getNewIDInstance() {
        lastInstance+=1;
        return lastInstance;
    }
}
