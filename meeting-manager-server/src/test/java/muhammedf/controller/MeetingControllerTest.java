package muhammedf.controller;

import muhammedf.model.Meeting;
import muhammedf.model.Department;
import muhammedf.repositories.DepartmentRepository;
import muhammedf.repositories.MeetingRepository;
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
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MeetingControllerTest extends AbstractCRUDControllerTest<Meeting, Long> {

    @InjectMocks
    private MeetingController mc;

    @Mock
    private MeetingRepository mr;
    
    @Mock
    private DepartmentRepository dr;

    @Override
    public AbstractCRUDController getController() {
        return mc;
    }

    @Override
    public CrudRepository getRepository() {
        return mr;
    }

    @Override
    public Class<Meeting> getTClass() {
        return Meeting.class;
    }

    @Override
    public Meeting getNewTInstance() {
        return new Meeting();
    }

    private Long lastInstance = 0l;

    @Override
    public Long getNewIDInstance() {
        lastInstance+=1;
        return lastInstance;
    }

    @Test
    public void addDepartmentToNonexsitingMeeting(){
        when(mr.findOne(any())).thenReturn(null);
        when(dr.findOne(any())).thenReturn(new Department());
        ResponseEntity re=mc.addDepartment(0l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addNonExistingDepartmentToMeeting(){
        when(dr.findOne(any())).thenReturn(null);
        when(mr.findOne(any())).thenReturn(new Meeting());
        ResponseEntity re=mc.addDepartment(1l, 0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void addCurrentlyIncludedDepartmentToMeeting(){
        Meeting meeting=new Meeting();
        Department department1=new Department();
        department1.setId(1l);
        Department department2=new Department();
        meeting.getDepartments().add(department1);
        when(mr.findOne(any())).thenReturn(meeting);
        when(dr.findOne(any())).thenReturn(department1);
        ResponseEntity re=mc.addDepartment(1l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(meeting.getDepartments()).contains(department1).doesNotContain(department2);
    }

    @Test
    public void addDepartmentToMeeting(){
        when(mr.findOne(any())).thenReturn(new Meeting());
        when(dr.findOne(any())).thenReturn(new Department());
        ResponseEntity re=mc.addDepartment(1l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void removeDepartmentFromNonexistingMeeting(){
        when(mr.findOne(any())).thenReturn(null);
        when(dr.findOne(any())).thenReturn(new Department());
        ResponseEntity re=mc.removeDepartment(0l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void removeNonexistingDepartmentFromMeeting(){
        when(mr.findOne(any())).thenReturn(new Meeting());
        when(dr.findOne(any())).thenReturn(null);
        ResponseEntity re=mc.removeDepartment(1l,0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void removeCurrentlyNotIncludedDepartmentFromMeeting(){
        when(mr.findOne(any())).thenReturn(new Meeting());
        when(dr.findOne(any())).thenReturn(new Department());
        ResponseEntity re=mc.removeDepartment(1l,1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void listDepartmentsOfNonexistingMeeting(){
        when(mr.findOne(any())).thenReturn(null);
        ResponseEntity re=mc.listDepartments(0l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void listDepartmentsOfMeeting(){
        Set<Department> departments = new HashSet<>(Arrays.asList(new Department(), new Department(), new Department()));
        Meeting meeting = new Meeting();
        meeting.setDepartments(departments);
        when(mr.findOne(any())).thenReturn(meeting);
        ResponseEntity re=mc.listDepartments(1l);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isEqualTo(departments);
    }
}
