package db.train.repository;

import db.train.persistence.model.Connection;
import org.springframework.stereotype.Repository;
import org.webrepogen.ICRUDRepository;

;

@Repository
public interface ConnectionRepository extends ICRUDRepository<Connection, Long> {}
