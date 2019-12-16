package db.train.persistence.model.type;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class CarriageType {
    @Id
    private Long id;
    @Column(nullable = false)
    private Integer seats;
    private Boolean cabin;

}
