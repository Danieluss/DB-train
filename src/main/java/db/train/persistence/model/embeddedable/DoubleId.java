package db.train.persistence.model.embeddedable;

import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class DoubleId implements Serializable {
    private Long id1;
    private Long id2;
}
