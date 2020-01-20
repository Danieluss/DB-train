package db.train.persistence.model.type;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import db.train.persistence.model.Zone;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "commutation_ticket_type_gen", sequenceName = "commutation_ticket_type_seq", initialValue = 1000)
public class CommutationTicketType {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commutation_ticket_type_gen")
    @Id
    private Long id;
    @NotBlank
    private String name;
    @Column(nullable = false)
    private Double price;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @JsonProperty("zone")
    public void setZone(Long id) {
        zone = new Zone();
        zone.setId(id);
    }

}
