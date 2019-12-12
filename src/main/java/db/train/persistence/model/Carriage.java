package db.train.persistence.model;

import db.train.persistence.model.type.CarriageType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@Entity
public class Carriage {
    @Id
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private CarriageType type;

}
