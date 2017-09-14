package muhammedf.meetingmanager.controller;

import muhammedf.meetingmanager.model.Department;
import muhammedf.meetingmanager.model.Meeting;
import muhammedf.meetingmanager.repositories.DepartmentRepository;
import muhammedf.meetingmanager.repositories.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

import static org.springframework.http.ResponseEntity.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/meeting")
public class MeetingController extends AbstractCRUDController<Meeting, Long> {

    public CrudRepository<Meeting, Long> getRepository(){
        return mr;
    }
    public  String getPath(){
        return "/meeting";
    }

    @Autowired
    private MeetingRepository mr;

    @Autowired
    private DepartmentRepository dr;

    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity create(@RequestBody Meeting entity){
        return super.create(entity);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        return super.deleteById(id);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Meeting> findById(@PathVariable("id") Long id){
        return super.findById(id);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity update(@PathVariable Long id, @RequestBody Meeting entity){
        return super.update(id, entity);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public Iterable<Meeting> listAll(){
        return super.listAll();
    }

    @RequestMapping(path = "/{mid:[0-9][0-9]*}/department/{did:[0-9][0-9]*}", method = RequestMethod.PUT)
    public ResponseEntity addDepartment(@PathVariable("mid") Long mid, @PathVariable("did") Long did){
        Meeting meeting = mr.findOne(mid);
        Department department = dr.findOne(did);
        if(meeting==null || department==null){
            return notFound().build();
        }
        if(!meeting.getDepartments().add(department)){
            return status(HttpStatus.CONFLICT).build();
        }
        mr.save(meeting);
        return ok().build();
    }

    @RequestMapping(path = "/{mid:[0-9][0-9]*}/department/{did:[0-9][0-9]*}", method = RequestMethod.DELETE)
    public ResponseEntity removeDepartment(@PathVariable("mid") Long mid, @PathVariable("did") Long did){
        Meeting meeting = mr.findOne(mid);
        Department department = dr.findOne(did);
        if(meeting==null || department==null){
            return notFound().build();
        }
        if(!meeting.getDepartments().remove(department)){
            return status(HttpStatus.CONFLICT).build();
        }
        mr.save(meeting);
        return ok().build();
    }

    @RequestMapping(path = "/{mid:[0-9][0-9]*}/department/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity listDepartments(@PathVariable("mid") Long id){
        Meeting meeting = mr.findOne(id);
        if(meeting==null){
            return ResponseEntity.notFound().build();
        }
        return ok(meeting.getDepartments());
    }
}
