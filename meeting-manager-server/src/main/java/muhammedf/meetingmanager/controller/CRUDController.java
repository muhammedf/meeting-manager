package muhammedf.meetingmanager.controller;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * Interface for CRUD rest api calls.
 * @see org.springframework.data.repository.CrudRepository
 * @param <T> the type of the entity that controller manages.
 * @param <ID> the type of the id of the entity that controller manages.
 */
public interface CRUDController<T, ID extends Serializable> {
    public ResponseEntity create(T entity);
    public ResponseEntity deleteById(ID id);
    public ResponseEntity<T> findById(ID id);
    public ResponseEntity update(ID id, T entity);
    public Iterable<T> listAll();
}
