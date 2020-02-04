package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "train_user_gen", sequenceName = "train_user_seq", initialValue = 1000)
@Table(indexes = {@Index(columnList = "id", name = "id")})
public class TrainUser {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("username", "Username")
            .put("email", "Email address")
            .put("name", "Name")
            .put("surname", "Surname")
            .put("tickets", "Owned tickets")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_user_gen")
    @Id
    private Long id;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String surname;
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "uuid")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trainUser", cascade = CascadeType.REMOVE)
    private List<Ticket> tickets;

    @JsonProperty("tickets")
    public void setTickets(List<UUID> uuids) {
        tickets = uuids.stream().map(uuid -> {
            Ticket tickets = new Ticket();
            tickets.setUuid(uuid);
            return tickets;
        }).collect(Collectors.toList());
    }

}
