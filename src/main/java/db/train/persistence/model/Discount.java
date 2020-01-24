package db.train.persistence.model;

import com.google.common.collect.ImmutableMap;
import lombok.Data;

import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@SequenceGenerator(name = "discount_gen", sequenceName = "discount_seq", initialValue = 1000)
public class Discount {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("name", "Nazwa przeceny")
            .put("percentOff", "Procent obniżki ceny")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_gen")
    @Id
    private Long id;
    private String name;
    @Column(nullable = false)
    private Double percentOff;
}
