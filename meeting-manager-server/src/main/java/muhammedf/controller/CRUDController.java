package muhammedf.controller;

import org.springframework.http.ResponseEntity;

import java.io.Serializable;

public interface CRUDController<T, ID extends Serializable> {
    public ResponseEntity create(T entity);
    public ResponseEntity deleteById(ID id);
    public ResponseEntity<T> findById(ID id);
    public ResponseEntity update(ID id, T entity);
    public Iterable<T> listAll();
}
