package db.train.web;

import db.train.repository.SpecificationFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.webrepogen.ICRUDController;
import org.webrepogen.ICRUDRepository;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public abstract class AbstractWebController<T, ID extends Serializable> implements ICRUDController<T, ID> {

    private Map<String, String> fields;
    private ICRUDRepository<T, ID> repo;
    private Class<T> clazz;
    private Class<ID> idClazz;

    @Autowired
    private SpecificationFactory specificationFactory;

    @Override
    public void init(ICRUDRepository<T, ID> repository, Class<T> clazz, Class<ID> idClazz) {
        this.repo = repository;
        this.fields = Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toMap(Field::getName, field -> field.getType().getSimpleName()));
        this.clazz = clazz;
        this.idClazz = idClazz;
    }

    @RequestMapping(value = "/tooltips", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> tooltips() throws InvocationTargetException, IllegalAccessException {
        Method m = null;
        try {
            m = clazz.getMethod("getTooltips");
        } catch (NoSuchMethodException e) {
            return null;
        }
        return (Map<String, String>) m.invoke(null);
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
    public Iterable<T> sort(Sort sort) {
        return repo.findAll(sort);
    }

    @RequestMapping(value = "/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<T> page(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @RequestMapping(value = "/upsert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public T upsert(@Valid @RequestBody T entity) {
        return repo.save(entity);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable(value = "id") ID id) {
        repo.deleteById(id);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public T get(@PathVariable(value = "id") ID id) {
        return repo.getOne(id);
    }

    @RequestMapping(value = "/search/deep", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<T> deepSearch(@RequestParam(value = "query") String string, @RequestParam(value = "depth") @Valid @Min(0) Integer depth, Pageable pageable) {
        return repo.findAll(Specification.where(specificationFactory.deepSearch(string, clazz, depth)), pageable);
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<T> search(@RequestParam(value = "query") String string) {
        return repo.findAll(Specification.where(specificationFactory.search(string, clazz)));
    }

    @RequestMapping(value = "/search/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<T> searchPage(@RequestParam(value = "query") String string, Pageable pageable) {
        return repo.findAll(Specification.where(specificationFactory.search(string, clazz)), pageable);
    }

    @RequestMapping(value = "/filter/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<T> filter(@RequestParam(value = "query") String string) {
        return repo.findAll(Specification.where(specificationFactory.filterQuery(string, clazz)));
    }

    @RequestMapping(value = "/filter/page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<T> filterPage(@RequestParam(value = "query") String string, Pageable pageable) {
        return repo.findAll(Specification.where(specificationFactory.filterQuery(string, clazz)), pageable);
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

