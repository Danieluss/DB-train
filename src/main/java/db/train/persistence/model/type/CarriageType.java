package db.train.persistence.model.type;

import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@SequenceGenerator(name = "carriage_type_gen", sequenceName = "carriage_type_seq", initialValue = 1000)
public class CarriageType {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carriage_type_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    private Integer seats;
    private Boolean cabin;

}
