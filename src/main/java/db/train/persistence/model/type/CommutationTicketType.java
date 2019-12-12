package db.train.persistence.model.type;

import db.train.persistence.model.Zone;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class CommutationTicketType {
    @Id
    private Long id;
    private String name;
    private Double price;
    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;
}
