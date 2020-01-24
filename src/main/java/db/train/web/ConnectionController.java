package db.train.web;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import db.train.persistence.model.Connection;
import java.lang.Long;
import db.train.web.AbstractWebController;
import org.webrepogen.ICRUDRepository;

@RestController
@RequestMapping("/api/connection")
public class ConnectionController extends AbstractWebController<Connection, Long> {
	@Autowired
	public ConnectionController(ICRUDRepository<Connection, Long> repository) {
		init(repository, Connection.class, Long.class);
	}
}
