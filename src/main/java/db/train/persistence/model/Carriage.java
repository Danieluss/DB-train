package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import db.train.persistence.model.type.CarriageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "carriage_gen", sequenceName = "carriage_seq", initialValue = 1000)
public class Carriage {
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carriage_gen")
    @Id
    private Long id;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(optional = false)
    @JoinColumn(name = "type_id")
    private CarriageType type;

    @JsonProperty("type")
    public void setType(Long id) {
        type = new CarriageType();
        type.setId(id);
    }

}
