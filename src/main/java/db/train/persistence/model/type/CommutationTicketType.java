package db.train.persistence.model.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import db.train.persistence.model.Zone;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CommutationTicketType {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    private String name;
    @Column(nullable = false)
    private Double price;
    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
