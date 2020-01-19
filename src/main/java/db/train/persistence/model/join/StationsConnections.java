package db.train.persistence.model.join;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import db.train.persistence.model.embeddedable.DoubleId;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StationsConnections {
    @EmbeddedId
    private DoubleId id;
    @Column(nullable = false)
    private Integer number;
    @Column(nullable = false)
    private Boolean stop;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @MapsId("id1")
    private Station station;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connection_id")
    @MapsId("id2")
    private Connection connection;
}
