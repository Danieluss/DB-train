package db.train.persistence.model.type;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@SequenceGenerator(name = "carriage_type_gen", sequenceName = "carriage_type_seq", initialValue = 1000)
public class CarriageType {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("seats", "Liczba miejsc siedzących")
            .put("cabin", "Wagon jest przedziałowy")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "carriage_type_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    private Integer seats;
    private Boolean cabin;

}
