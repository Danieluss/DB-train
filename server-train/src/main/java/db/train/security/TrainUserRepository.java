package db.train.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainUserRepository extends JpaRepository<TrainUser, Long> {

    TrainUser findByUsername(String string);

    TrainUser findByEmail(String username);
}
