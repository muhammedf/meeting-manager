package muhammedf.controller;

import muhammedf.model.Department;
import muhammedf.repositories.DepartmentRepository;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentControllerTest extends AbstractCRUDControllerTest<Department, Long> {

    @InjectMocks
    private DepartmentController dc;

    @Mock
    private DepartmentRepository dr;

    @Override
    public AbstractCRUDController getController() {
        return dc;
    }

    @Override
    public CrudRepository getRepository() {
        return dr;
    }

    @Override
    public Class<Department> getTClass() {
        return Department.class;
    }

    @Override
    public Department getNewTInstance(){
        return new Department();
    }

    private Long lastInstance=0l;

    @Override
    public Long getNewIDInstance(){
        lastInstance+=1;
        return lastInstance;
    }
}
