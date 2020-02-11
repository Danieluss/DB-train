package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.security.TrainUser;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Setter
@Getter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(indexes = {@Index(columnList = "uuid", name = "uuid")})
public class Ticket {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("uuid", "")
            .put("discount", "Discount")
            .put("price", "Price")
            .put("trainUser", "Owner")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }


    @GeneratedValue(generator = "UUID", strategy = GenerationType.AUTO)
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Id
    @Column(columnDefinition = "UUID")
    private UUID uuid;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private TrainUser trainUser;

    @JsonProperty("discount")
    public void setDiscount(Long id) {
        if(id != null) {
            discount = new Discount();
            discount.setId(id);
        }
    }

    @JsonProperty("trainUser")
    public void setTrainUser(Long id) {
        if(id != null) {
            trainUser = new TrainUser();
            trainUser.setId(id);
        }
    }
}
