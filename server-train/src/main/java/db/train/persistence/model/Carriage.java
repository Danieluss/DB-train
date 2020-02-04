package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.type.CarriageType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "carriage_gen", sequenceName = "carriage_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class Carriage {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("type", "Carriage type")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }


    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carriage_gen")
    @Id
    private Long id;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "train_id", insertable = false, updatable = false)
    private Train train;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    private CarriageType type;

    @JsonProperty("type")
    public void setType(Long id) {
        type = new CarriageType();
        type.setId(id);
    }

}
