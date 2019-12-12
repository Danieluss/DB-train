package db.train.persistence.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Edge {
    @Id
    private Long id;
    @Column(nullable = false)
    private Double distance;
    @ManyToOne(optional = false)
    @JoinColumn(name = "station1_id")
    private Station station1;
    @ManyToOne(optional = false)
    @JoinColumn(name = "station2_id")
    private Station station2;
}
