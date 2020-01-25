package db.train.persistence.model;

import com.google.common.collect.ImmutableMap;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Entity
@SequenceGenerator(name = "discount_gen", sequenceName = "discount_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class Discount {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("name", "Name of the discount")
            .put("percentOff", "Percentage off")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discount_gen")
    @Id
    private Long id;
    @Column(nullable = false)
    @NotBlank
    private String name;
    @Column(nullable = false)
    @DecimalMin("0")
    @DecimalMax("100")
    private Double percentOff;
}
