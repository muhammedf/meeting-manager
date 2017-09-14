package muhammedf.meetingmanager.controller;

import muhammedf.meetingmanager.model.Identity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.OptimisticLockException;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;

import static org.springframework.http.ResponseEntity.*;

/**
 * Base class for common CRUD operations.
 * @param <T> the type of the entity that controller manages.
 * @param <ID> the type of the id of the entity that controller manages.
 */
public abstract class BaseCRUDController<T extends Identity, ID extends Serializable> implements CRUDController<T,ID> {

    /**
     * Will be using to execute CRUD operations.
     * @return related entity's repository.
     */
    public abstract CrudRepository<T, ID> getRepository();

    /**
     * Will be using to create response.
     * @return {@link org.springframework.web.bind.annotation.RequestMapping} path.
     */
    public abstract String getPath();

    @Override
    public ResponseEntity create(T entity) {
        getRepository().save(entity);
        return created(UriBuilder.fromPath(getPath())
                .path(String.valueOf(entity.getId())).build()).body(entity.getId());
    }

    @Override
    public ResponseEntity<T> findById(ID id) {
        T entity = getRepository().findOne(id);
        if(entity==null){
            return notFound().build();
        }
        return ok(entity);
    }

    @Override
    public ResponseEntity deleteById(ID id) {
        T entity = getRepository().findOne(id);
        if(entity==null){
            return notFound().build();
        }
        getRepository().delete(id);
        return noContent().build();
    }

    @Override
    public ResponseEntity update(ID id, T entity) {
        if(entity == null || id == null){
            return badRequest().build();
        }
        if(!id.equals(entity.getId())){
            return status(HttpStatus.CONFLICT).build();
        }
        if(getRepository().findOne(id)==null){
            return notFound().build();
        }
        try {
            entity = getRepository().save(entity);
        }catch (OptimisticLockException e){
            return status(HttpStatus.CONFLICT).build();
        }
        return noContent().build();
    }

    @Override
    public Iterable<T> listAll() {
        return getRepository().findAll();
    }
}
