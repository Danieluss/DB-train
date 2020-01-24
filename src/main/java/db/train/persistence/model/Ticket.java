package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
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
public class Ticket {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("uuid", "")
            .put("discount", "Przecena")
            .put("price", "Cena")
            .put("trainUser", "Właściciel biletu")
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
    @Column(nullable = false)
    private Double price;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne
    @JoinColumn(name = "discount_id")
    private Discount discount;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private TrainUser trainUser;

    @JsonProperty("discount")
    public void setDiscount(Long id) {
        discount = new Discount();
        discount.setId(id);
    }

    @JsonProperty("trainUser")
    public void setTrainUser(Long id) {
        trainUser = new TrainUser();
        trainUser.setId(id);
    }
}
