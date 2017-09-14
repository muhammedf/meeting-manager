package muhammedf.meetingmanager.controller;

import muhammedf.meetingmanager.model.Employee;
import muhammedf.meetingmanager.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseCRUDController {

    public CrudRepository<Employee, Long> getRepository(){
        return er;
    }
    public String getPath(){
        return "/employee";
    }

    @Autowired
    private EmployeeRepository er;

    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity create(@RequestBody Employee entity){
        return super.create(entity);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        return super.deleteById(id);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Employee> findById(@PathVariable("id") Long id){
        return super.findById(id);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity update(@PathVariable Long id, @RequestBody Employee entity){
        return super.update(id, entity);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public Iterable<Employee> listAll(){
        return super.listAll();
    }

}
