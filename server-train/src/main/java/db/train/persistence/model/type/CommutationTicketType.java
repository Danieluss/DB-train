package db.train.persistence.model.type;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.Zone;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "commutation_ticket_type_gen", sequenceName = "commutation_ticket_type_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class CommutationTicketType {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("name", "Name of the ticket type")
            .put("price", "Price")
            .put("zone", "Permitted zone")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commutation_ticket_type_gen")
    @Id
    private Long id;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @DecimalMin("0")
    @DecimalMax("10000")
    private Double price;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @JsonProperty("zone")
    public void setZone(Long id) {
        if (id != null) {
            zone = new Zone();
            zone.setId(id);
        }
    }

}
