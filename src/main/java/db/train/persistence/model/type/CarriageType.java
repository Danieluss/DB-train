package db.train.persistence.model.type;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class CarriageType {
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(nullable = false)
    private Integer seats;
    private Boolean cabin;

}
