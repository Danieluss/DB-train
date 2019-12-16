package db.train.persistence.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Discount {
    @Id
    private Long id;
    private String name;
    @Column(nullable = false)
    private Double percentOff;
}
