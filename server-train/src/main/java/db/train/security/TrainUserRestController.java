package db.train.security;

import db.train.persistence.model.type.CarriageType;
import db.train.web.AbstractWebController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.webrepogen.ICRUDRepository;

@RestController
@RequestMapping("/api/trainuser")
public class TrainUserRestController extends AbstractWebController<TrainUser, Long> {

    @Autowired
    public TrainUserRestController(TrainUserRepository repository) {
        init(repository, TrainUser.class, Long.class);
    }

}
