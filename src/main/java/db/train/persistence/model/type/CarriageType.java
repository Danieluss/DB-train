package db.train.persistence.model.type;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CarriageType {
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(nullable = false)
    private Integer seats;
    private Boolean cabin;

}
