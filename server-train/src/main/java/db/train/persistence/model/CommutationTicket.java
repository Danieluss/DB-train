package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.type.CommutationTicketType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(indexes = {@Index(columnList = "uuid", name = "uuid")})
public class CommutationTicket extends Ticket {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("uuid", "")
            .put("discount", "Discount")
            .put("price", "Price")
            .put("trainUser", "Owner")
            .put("startDate", "Validity beginning date")
            .put("endDate", "Expiration date")
            .put("type", "Commutation ticket type")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }


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

    @AssertTrue(message="startDate should be before endDate")
    private boolean isCrossValid() {
        return this.startDate.before(this.endDate);
    }

}
