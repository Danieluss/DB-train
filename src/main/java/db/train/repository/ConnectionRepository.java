package db.train.repository;
import db.train.persistence.model.Connection;
import java.lang.Long;
import org.webrepogen.ICRUDRepository;
import org.springframework.stereotype.Repository;;

@Repository
public interface ConnectionRepository extends ICRUDRepository<Connection, Long> {}
