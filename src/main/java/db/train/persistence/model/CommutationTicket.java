package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import db.train.persistence.model.type.CommutationTicketType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Entity
public class CommutationTicket extends Ticket {

    @Column(nullable = false)
    private Date startDate;
    @Column(nullable = false)
    private Date endDate;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private CommutationTicketType type;

    @JsonProperty("type")
    public void setType(Long id) {
        type = new CommutationTicketType();
        type.setId(id);
    }

}
