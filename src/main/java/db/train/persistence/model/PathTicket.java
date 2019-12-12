package db.train.persistence.model;

import db.train.persistence.model.join.StationsConnections;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;


@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class PathTicket extends Ticket {

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "station1_id", referencedColumnName = "station_id", insertable = false, updatable = false),
            @JoinColumn(name = "connection_id", referencedColumnName = "connection_id", insertable = false, updatable = false)
    })
    private StationsConnections stationConnection1;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "station2_id", referencedColumnName = "station_id", insertable = false, updatable = false),
            @JoinColumn(name = "connection_id", referencedColumnName = "connection_id", insertable = false, updatable = false)
    })
    private StationsConnections stationConnection2;

}