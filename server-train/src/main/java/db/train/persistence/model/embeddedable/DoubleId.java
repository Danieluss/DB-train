package db.train.persistence.model.embeddedable;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
public class DoubleId implements Serializable {
    private Long id1;
    private Long id2;
}
