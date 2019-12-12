package db.train.persistence.model.type;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CarriageType {
    @Id
    private Long id;
    private Integer seats;
    private Boolean cabin;

}
