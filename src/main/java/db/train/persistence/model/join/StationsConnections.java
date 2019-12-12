package db.train.persistence.model.join;

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
    @ManyToOne
    @JoinColumn(name = "station_id")
    @MapsId("id1")
    private Station station;
    @ManyToOne
    @JoinColumn(name = "connection_id")
    @MapsId("id2")
    private Connection connection;
}
