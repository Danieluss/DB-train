package db.train.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.webrepogen.ICRUDRepository;

@Repository
public interface TrainUserRepository extends ICRUDRepository<TrainUser, Long> {

    TrainUser findByUsername(String string);

    TrainUser findByEmail(String username);
}
