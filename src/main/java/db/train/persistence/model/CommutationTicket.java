package db.train.persistence.model;

import db.train.persistence.model.type.CommutationTicketType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class CommutationTicket extends Ticket {

    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private CommutationTicketType type;

}
