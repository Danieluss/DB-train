package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.join.StationsConnections;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.util.Date;
import java.util.Map;


@Setter
@Getter
@Entity
@Table(indexes = {@Index(columnList = "uuid", name = "uuid")})
public class PathTicket extends Ticket {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("uuid", "")
            .put("discount", "Discount")
            .put("price", "Price")
            .put("trainUser", "Owner")
            .put("date", "Expiration date")
            .put("stationConnection1", "From station in connection")
            .put("stationConnection2", "To station in connection")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @Column(nullable = false)
    @DecimalMin("0")
    @DecimalMax("100000")
    private Double price;
    @Column(nullable = false)
    private Date date;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stationconnection_id1")
    private StationsConnections stationConnection1;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stationconnection_id2")
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
