package muhammedf.meetingmanager.controller;

import muhammedf.meetingmanager.model.Employee;
import muhammedf.meetingmanager.repositories.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;


import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.OptimisticLockException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeneyselTest {

//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext wac;
//
    @Before
    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).defaultRequest().build();
//       e = Mockito.mock()
    }

    @InjectMocks
    private EmployeeController ec;

    @Mock
    private EmployeeRepository er;

    @Test
    public void createEmployee(){
        when(er.save(any(Employee.class))).thenReturn(any(Employee.class));
        ResponseEntity re = ec.create(new Employee());
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void deleteNonexisting(){
        when(er.findOne(any())).thenReturn(null);
        ResponseEntity re = ec.deleteById(0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteExisting(){
        when(er.findOne(any())).thenReturn(new Employee());
        ResponseEntity re = ec.deleteById(0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void findExisting(){
        when(er.findOne(any())).thenReturn(new Employee());
        ResponseEntity re = ec.findById(0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isNotNull();
    }

    @Test
    public void findNonexisting(){
        when(er.findOne(any())).thenReturn(null);
        ResponseEntity re = ec.findById(0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(re.getBody()).isNull();
    }

    @Test
    public void updateWithNullEntity(){
        ResponseEntity re = ec.update(0l, null);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateWithConflictingIds(){
        Employee employee = new Employee();
        employee.setId(2l);
        ResponseEntity re = ec.update(1l, employee);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void updateNonexistingEntity(){
        Employee employee = new Employee();
        employee.setId(1l);
        when(er.findOne(any())).thenReturn(null);
        ResponseEntity re = ec.update(1l, employee);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateWithOptimisticLockException(){
        Employee employee = new Employee();
        employee.setId(1l);
        when(er.findOne(any())).thenReturn(new Employee());
        when(er.save(any(Employee.class))).thenThrow(OptimisticLockException.class);
        ResponseEntity re = ec.update(1l, employee);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void updateWithSuccess(){
        Employee employee = new Employee();
        employee.setId(1l);
        when(er.findOne(any())).thenReturn(new Employee());
        ResponseEntity re = ec.update(1l, employee);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void listEmployees(){
        List<Employee> employees = Arrays.asList(new Employee(), new Employee(), new Employee());
        when(er.findAll()).thenReturn(employees);
        assertThat(ec.listAll()).isEqualTo(employees);
    }

//    @Test
//    public void testCreate() throws Exception {
//
//        Employee employee = new Employee();
//        Employee employee1 = new Employee();
//        employee1.setId(1l);
//
//        String json = new ObjectMapper().writeValueAsString(employee);
//
////        when(ec.create(employee)).thenReturn(Response.status(Response.Status.CREATED).build());
//        when(er.save(any(Employee.class))).thenReturn(employee1);
////        when(ep.save(employee)).thenReturn(employee1);
////        System.out.println(ec.create(employee).getStatus());
//
//        mockMvc.perform(post("/employees/")
//                .accept(MediaType.APPLICATION_JSON)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(status().isCreated())
////                .andExpect(jsonPath("$.name").value("furkan"))
////                .andExpect(jsonPath("$.surname").value("güngör"))
//        ;
//
//    }
//
////    @Test
//    public void getTest() throws Exception {
//
//        Employee employee = new Employee();
//        employee.setName("furkan");
//
////        when(ec.get(1l)).thenReturn(employee);
//
////        mockMvc.perform(get("/employees/1")).andExpect(status().isOk()).andExpect();
//
//    }
}
