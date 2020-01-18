package db.train.persistence.model;

import db.train.persistence.model.type.CarriageType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Carriage {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private CarriageType type;

}
