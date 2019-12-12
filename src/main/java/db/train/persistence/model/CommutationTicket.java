package db.train.persistence.model;

import db.train.persistence.model.type.CommutationTicketType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=true)
@Entity
public class CommutationTicket extends Ticket {

    private Date startDate;
    private Date endDate;
    @ManyToOne
    @JoinColumn(name = "type_id")
    private CommutationTicketType type;

}
