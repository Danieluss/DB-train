package db.train.web;

import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.webrepogen.ICRUDController;

import javax.validation.Valid;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractWebController<T, ID extends Serializable> implements ICRUDController<T, ID> {

    private JpaRepository<T, ID> repo;
    private Map<String, String> fields;

    public AbstractWebController() {
    }

    @Override
    public void init(JpaRepository<T, ID> jpaRepository, Class<T> clazz) {
        this.repo = jpaRepository;
        this.fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, field -> field.getType().getSimpleName()));
    }

    @RequestMapping(value = "/fields", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> fields() {
        return fields;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<T> list() {
        return repo.findAll();
    }

    @RequestMapping(value = "/sort", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Iterable<T> sort(@RequestParam Sort sort) {
        return repo.findAll(sort);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<T> page(@RequestParam Pageable pageable) {
        return repo.findAll(pageable);
    }

    @RequestMapping(value = "/upsert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public T upsert(@Valid @RequestBody T entity) {
        return repo.save(entity);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@Valid @PathVariable(value = "id") ID id) {
        repo.deleteById(id);
    }

    @RequestMapping(value = "get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public T get(@Valid @PathVariable(value = "id") ID id) {
        return repo.getOne(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

