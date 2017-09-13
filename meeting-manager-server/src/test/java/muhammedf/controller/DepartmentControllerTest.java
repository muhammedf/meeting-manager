package muhammedf.controller;

import muhammedf.model.Department;
import muhammedf.model.Employee;
import muhammedf.repositories.DepartmentRepository;
import muhammedf.repositories.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentControllerTest extends AbstractCRUDControllerTest<Department, Long> {

    @InjectMocks
    private DepartmentController dc;

    @Mock
    private DepartmentRepository dr;

    @Mock
    private EmployeeRepository er;

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

    @Test
    public void addEmployeeToNonexsitingDepartment(){
        when(dr.findOne(any())).thenReturn(null);
        when(er.findOne(any())).thenReturn(new Employee());
        ResponseEntity re=dc.addEmployee(0l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addNonExistingEmployeeToDepartment(){
        when(er.findOne(any())).thenReturn(null);
        when(dr.findOne(any())).thenReturn(new Department());
        ResponseEntity re=dc.addEmployee(1l, 0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addCurrentlyIncludedEmployeeToDepartment(){
        Department department=new Department();
        Employee employee1=new Employee();
        employee1.setId(1l);
        Employee employee2=new Employee();
        department.getEmployees().add(employee1);
        when(dr.findOne(any())).thenReturn(department);
        when(er.findOne(any())).thenReturn(employee1);
        ResponseEntity re=dc.addEmployee(1l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(department.getEmployees()).contains(employee1).doesNotContain(employee2);
    }

    @Test
    public void addEmployeeToDepartment(){
        when(dr.findOne(any())).thenReturn(new Department());
        when(er.findOne(any())).thenReturn(new Employee());
        ResponseEntity re=dc.addEmployee(1l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void removeEmployeeFromNonexistingDepartment(){
        when(dr.findOne(any())).thenReturn(null);
        when(er.findOne(any())).thenReturn(new Employee());
        ResponseEntity re=dc.removeEmployee(0l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void removeNonexistingEmployeeFromDepartment(){
        when(dr.findOne(any())).thenReturn(new Department());
        when(er.findOne(any())).thenReturn(null);
        ResponseEntity re=dc.removeEmployee(1l,0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void removeCurrentlyNotIncludedEmployeeFromDepartment(){
        when(dr.findOne(any())).thenReturn(new Department());
        when(er.findOne(any())).thenReturn(new Employee());
        ResponseEntity re=dc.removeEmployee(1l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void listEmployeesOfNonexistingDepartment(){
        when(dr.findOne(any())).thenReturn(null);
        ResponseEntity re=dc.listEmployees(0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void listEmployeesOfDepartment(){
        Set<Employee> employees = new HashSet<>(Arrays.asList(new Employee(), new Employee(), new Employee()));
        Department department = new Department();
        department.setEmployees(employees);
        when(dr.findOne(any())).thenReturn(department);
        ResponseEntity re=dc.listEmployees(1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo(employees);
    }
}
