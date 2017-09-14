package muhammedf.controller;

import muhammedf.model.Identity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.persistence.OptimisticLockException;
import javax.ws.rs.core.UriBuilder;
import java.io.Serializable;

import static org.springframework.http.ResponseEntity.*;

public abstract class AbstractCRUDController<T extends Identity, ID extends Serializable> implements CRUDController<T,ID> {

    public abstract CrudRepository<T, ID> getRepository();
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
            System.out.println("before save");
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
