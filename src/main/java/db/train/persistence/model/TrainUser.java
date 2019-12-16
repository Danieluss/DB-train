package db.train.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class TrainUser {
    @Id
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    private String name;
    private String surname;
    @OneToMany(mappedBy = "trainUser")
    private List<Ticket> tickets;
}
