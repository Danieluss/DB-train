package db.train.persistence.model;

import com.fasterxml.jackson.annotation.*;
import db.train.persistence.model.type.CarriageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
public class Edge {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(nullable = false)
    private Double distance;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station1_id")
    private Station station1;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station2_id")
    private Station station2;

    @JsonProperty("station1")
    public void setStation1(Long id) {
        station1 = new Station();
        station1.setId(id);
    }

    @JsonProperty("station2")
    public void setStation2(Long id) {
        station2 = new Station();
        station2.setId(id);
    }

}
