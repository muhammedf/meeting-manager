package muhammedf.meetingmanager.controller;

import muhammedf.meetingmanager.model.Employee;
import muhammedf.meetingmanager.model.Identity;
import org.junit.Test;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.OptimisticLockException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * Base class for common CRUD operation tests.
 * @param <T> the type of the entity that controller manages.
 * @param <ID> the type of the id of the entity that controller manages.
 */
public abstract class BaseCRUDControllerTest<T extends Identity, ID extends Serializable> {

    /**
     *
     * @return the controller that will be tested.
     */
    public abstract BaseCRUDController getController();

    /**
     *
     * @return the repository that will be mocked.
     */
    public abstract CrudRepository getRepository();

    /**
     *
     * @return the class of the type of the entity.
     */
    public abstract Class<T> getTClass();

    /**
     *
     * @return new instance of the entity.
     */
    public abstract T getNewTInstance();

    /**
     *
     * @return new instance in the type of the entity's id.
     */
    public abstract ID getNewIDInstance();

    @Test
    public void createEmployee() throws IllegalAccessException, InstantiationException {
        when(getRepository().save(any(getTClass()))).thenReturn(any(getTClass()));
        ResponseEntity re = getController().create(getTClass().newInstance());
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void deleteNonexisting() throws IllegalAccessException, InstantiationException {
        when(getRepository().findOne(any())).thenReturn(null);
        ResponseEntity re = getController().deleteById(getNewIDInstance());
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void deleteExisting() throws IllegalAccessException, InstantiationException {
        when(getRepository().findOne(any())).thenReturn(new Employee());
        ResponseEntity re = getController().deleteById(getNewIDInstance());
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void findExisting() throws IllegalAccessException, InstantiationException {
        when(getRepository().findOne(any())).thenReturn(getNewTInstance());
        ResponseEntity re = getController().findById(getNewIDInstance());
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).isNotNull();
    }

    @Test
    public void findNonexisting(){
        when(getRepository().findOne(any())).thenReturn(null);
        ResponseEntity re = getController().findById(getNewIDInstance());
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(re.getBody()).isNull();
    }

    @Test
    public void updateWithNullEntity(){
        ResponseEntity re = getController().update(getNewIDInstance(), null);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void updateWithConflictingIds(){
        T entity = getNewTInstance();
        entity.setId(getNewIDInstance());
        ResponseEntity re = getController().update(getNewIDInstance(), entity);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void updateNonexistingEntity(){
        T entity = getNewTInstance();
        ID id = getNewIDInstance();
        entity.setId(id);
        when(getRepository().findOne(any())).thenReturn(null);
        ResponseEntity re = getController().update(id, entity);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void updateWithOptimisticLockException(){
        T entity = getNewTInstance();
        ID id = getNewIDInstance();
        entity.setId(id);
        when(getRepository().findOne(any())).thenReturn(getNewTInstance());
        when(getRepository().save(any(getTClass()))).thenThrow(OptimisticLockException.class);
        ResponseEntity re = getController().update(id, entity);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    public void updateWithSuccess(){
        T entity = getNewTInstance();
        ID id = getNewIDInstance();
        entity.setId(id);
        when(getRepository().findOne(any())).thenReturn(getNewTInstance());
        ResponseEntity re = getController().update(id, entity);
        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void listEmployees(){
        List<T> entities = Arrays.asList(getNewTInstance(), getNewTInstance(), getNewTInstance());
        when(getRepository().findAll()).thenReturn(entities);
        assertThat(getController().listAll()).isEqualTo(entities);
    }

}
