package db.train.security;

import db.train.persistence.model.Train;
import db.train.persistence.model.type.CarriageType;
import db.train.web.AbstractWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.webrepogen.ICRUDRepository;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/trainuser")
public class TrainUserRestController extends AbstractWebController<TrainUser, Long> {

    @Autowired
    private TrainUserService service;

    @Autowired
    public TrainUserRestController(TrainUserRepository repository) {
        init(repository, TrainUser.class, Long.class);
    }

    @Override
    @RequestMapping(value = "/upsert", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public TrainUser upsert(@RequestBody TrainUser entity) {
        return service.upsertEntity(entity);
    }

}
