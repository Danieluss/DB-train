package db.train.persistence.model;

import db.train.persistence.model.join.StationsConnections;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class PathTicket extends Ticket {

    @Column(nullable = false)
    private Double price;
    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "station1_id", referencedColumnName = "station_id", insertable = false, updatable = false),
            @JoinColumn(name = "connection_id", referencedColumnName = "connection_id", insertable = false, updatable = false)
    })
    private StationsConnections stationConnection1;
    @ManyToOne(optional = false)
    @JoinColumns({
            @JoinColumn(name = "station2_id", referencedColumnName = "station_id", insertable = false, updatable = false),
            @JoinColumn(name = "connection_id", referencedColumnName = "connection_id", insertable = false, updatable = false)
    })
    private StationsConnections stationConnection2;

}