package db.train.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@SequenceGenerator(name = "discount_gen", sequenceName = "discount_seq", initialValue = 1000)
public class Discount {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_gen")
    @Id
    private Long id;
    private String name;
    @Column(nullable = false)
    private Double percentOff;
}
