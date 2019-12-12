package db.train.persistence.model.embeddedable;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class DoubleId implements Serializable {
    private Long id1;
    private Long id2;
}
