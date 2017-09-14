package muhammedf.controller;

import muhammedf.model.Department;
import muhammedf.model.Employee;
import muhammedf.repositories.DepartmentRepository;
import muhammedf.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.MediaType;

import static org.springframework.http.ResponseEntity.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/department")
public class DepartmentController extends AbstractCRUDController<Department, Long> {

    public CrudRepository<Department, Long> getRepository(){
        return dr;
    }
    public String getPath(){
        return "/department";
    }

    @Autowired
    private DepartmentRepository dr;

    @Autowired
    private EmployeeRepository er;

    @RequestMapping(path = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity create(@RequestBody Department entity){
        return super.create(entity);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.DELETE)
    public ResponseEntity deleteById(@PathVariable("id") Long id){
        return super.deleteById(id);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity<Department> findById(@PathVariable("id") Long id){
        return super.findById(id);
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON)
    public ResponseEntity update(@PathVariable("id") Long id, @RequestBody Department entity){
        return super.update(id, entity);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public Iterable<Department> listAll(){
        return super.listAll();
    }

    @RequestMapping(path = "/{did:[0-9][0-9]*}/employee/{eid:[0-9][0-9]*}", method = RequestMethod.PUT)
    public ResponseEntity addEmployee(@PathVariable("did") Long did, @PathVariable("eid") Long eid){
        Department department = dr.findOne(did);
        Employee employee = er.findOne(eid);
        if(department==null || employee==null){
            return notFound().build();
        }
        if(!department.getEmployees().add(employee)){
            return status(HttpStatus.CONFLICT).build();
        }
        dr.save(department);
        return ok().build();
    }

    @RequestMapping(path = "/{did:[0-9][0-9]*}/employee/{eid:[0-9][0-9]*}", method = RequestMethod.DELETE)
    public ResponseEntity removeEmployee(@PathVariable("did") Long did, @PathVariable("eid") Long eid){
        Department department = dr.findOne(did);
        Employee employee = er.findOne(eid);
        if(department==null || employee==null){
            return notFound().build();
        }
        if(!department.getEmployees().remove(employee)){
            return status(HttpStatus.CONFLICT).build();
        }
        dr.save(department);
        return ok().build();
    }

    @RequestMapping(path = "/{id:[0-9][0-9]*}/employee/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON)
    public ResponseEntity listEmployees(@PathVariable("id") Long id){
        Department department = dr.findOne(id);
        if(department==null){
            return ResponseEntity.notFound().build();
        }
        return ok(department.getEmployees());
    }
}
