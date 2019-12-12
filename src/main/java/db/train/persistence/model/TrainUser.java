package db.train.persistence.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@Entity
public class TrainUser {
    @Id
    private Long id;
    private String username;
    private String email;
    private String name, surname;
    @OneToMany(mappedBy = "trainUser")
    private List<Ticket> tickets;
}
