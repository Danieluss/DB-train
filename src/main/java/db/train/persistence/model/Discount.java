package db.train.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Discount {
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    @Id
    private Long id;
    private String name;
    @Column(nullable = false)
    private Double percentOff;
}
