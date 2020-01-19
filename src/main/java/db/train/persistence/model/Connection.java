package db.train.persistence.model;

import com.fasterxml.jackson.annotation.*;
import db.train.persistence.model.join.StationsConnections;
import db.train.persistence.model.type.CommutationTicketType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
public class Connection {
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Id
    private Long id;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "connections")
    private List<Train> trains;
    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "connection")
    @OrderBy(value = "number")
    private List<StationsConnections> stations;

    @JsonProperty("trains")
    public void setTrains(List<Long> ids) {
        trains = ids.stream().map(id -> {
            Train train = new Train();
            train.setId(id);
            return train;
        }).collect(Collectors.toList());
    }

    @JsonProperty("stations")
    public void setStations(List<Long> ids) {
        stations = ids.stream().map(id -> {
            StationsConnections stations = new StationsConnections();
            stations.setId(id);
            return stations;
        }).collect(Collectors.toList());
    }

}
