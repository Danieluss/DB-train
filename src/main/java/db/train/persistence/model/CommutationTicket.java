package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.type.CommutationTicketType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@Entity
public class CommutationTicket extends Ticket {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("uuid", "")
            .put("discount", "Przecena")
            .put("price", "Cena")
            .put("trainUser", "Właściciel biletu")
            .put("startDate", "Data rozpoczęcia ważności")
            .put("endDate", "Data zakończenia ważności biletu")
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

}
