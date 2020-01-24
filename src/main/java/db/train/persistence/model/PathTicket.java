package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.join.StationsConnections;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;


@Setter
@Getter
@Entity
public class PathTicket extends Ticket {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("uuid", "")
            .put("discount", "Przecena")
            .put("price", "Cena")
            .put("trainUser", "Właściciel")
            .put("date", "Data ważności biletu")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @Column(nullable = false)
    private Date date;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "station1_id", referencedColumnName = "station_id", insertable = false, updatable = false),
            @JoinColumn(name = "connection_id", referencedColumnName = "connection_id", insertable = false, updatable = false)
    })
    private StationsConnections stationConnection1;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumns({
            @JoinColumn(name = "station2_id", referencedColumnName = "station_id", insertable = false, updatable = false),
            @JoinColumn(name = "connection_id", referencedColumnName = "connection_id", insertable = false, updatable = false)
    })
    private StationsConnections stationConnection2;

    @JsonProperty("stationConnection1")
    public void stationConnection1(Long id) {
        stationConnection2 = new StationsConnections();
        stationConnection2.setId(id);
    }

    @JsonProperty("stationConnection2")
    public void stationConnection2(Long id) {
        stationConnection1 = new StationsConnections();
        stationConnection1.setId(id);
    }

}
