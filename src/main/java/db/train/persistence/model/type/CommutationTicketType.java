package db.train.persistence.model.type;

import db.train.persistence.model.Zone;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CommutationTicketType {
    @Id
    private Long id;
    private String name;
    @Column(nullable = false)
    private Double price;
    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
