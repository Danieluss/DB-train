package db.train.persistence.model.join;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.Connection;
import db.train.persistence.model.Station;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@SequenceGenerator(name = "stations_connections_gen", sequenceName = "stations_connections_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class StationsConnections implements Serializable {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("number", "Number of station in connection")
            .put("stop", "Does the train stop at the station?")
            .put("arrival", "Time of arrival")
            .put("departure", "Time of departure")
            .put("station", "Station")
            .put("connection", "Connection")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stations_connections_gen")
    @Id
    private Long id;
    @DecimalMin("0")
    @DecimalMax("100000")
    @Column(nullable = false)
    private Integer number;
    @Column(nullable = false)
    private Boolean stop;
    @Column(nullable = false)
    private LocalTime arrival;
    @Column(nullable = false)
    private LocalTime departure;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;
    @NotNull
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

    @AssertTrue(message = "Arrival should be before")
    private boolean isCrossValid() {
        return (!(this.arrival.isAfter(this.departure)));
    }

}
