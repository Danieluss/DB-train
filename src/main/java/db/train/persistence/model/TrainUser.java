package db.train.persistence.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class TrainUser {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    private String name;
    private String surname;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trainUser")
    private List<Ticket> tickets;
}
