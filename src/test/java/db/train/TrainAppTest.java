package db.train;

import db.train.persistence.model.Carriage;
import db.train.web.AbstractWebController;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.webrepogen.ICRUDController;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class TrainAppTest {

    @Test
    public void loadContextTest() {

    }


}
