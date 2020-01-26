package db.train.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@RestController
@RequestMapping("/api/function")
public class FunctionController {

    @Autowired
    private EntityManager entityManager;

    @RequestMapping(value = "/total", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Double total() {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("total");

        query.execute();

        return (Double) query.getSingleResult();
    }

    @RequestMapping(value = "/pricerise", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void priceRise(@RequestParam Double modifier) {
        StoredProcedureQuery query = entityManager
                .createStoredProcedureQuery("price_rise")
                .registerStoredProcedureParameter("modifier", Double.class, ParameterMode.IN)
                .setParameter("modifier", modifier);

        query.execute();
    }

}
