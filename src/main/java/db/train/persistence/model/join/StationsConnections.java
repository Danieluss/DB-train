package db.train.persistence.model.join;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "stations_connections_gen", sequenceName = "stations_connections_seq", initialValue = 1000)
public class StationsConnections implements Serializable {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stations_connections_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    private Integer number;
    @Column(nullable = false)
    private Boolean stop;
    @Column(nullable =  false)
    private LocalTime arrival;
    @Column(nullable = false)
    private LocalTime departure;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    private Station station;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "connection_id")
    private Connection connection;

    @JsonProperty("station")
    public void setStation(Long id) {
        station = new Station();
        station.setId(id);
    }

    @JsonProperty("connection")
    public void setConnection(Long id) {
        connection = new Connection();
        connection.setId(id);
    }

}
