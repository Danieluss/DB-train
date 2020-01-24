package db.train.persistence.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "train_user_gen", sequenceName = "train_user_seq", initialValue = 1000)
public class TrainUser {

    private static final Map<String, String> TOOLTIPS = ImmutableMap.<String, String>builder()
            .put("id", "")
            .put("username", "Nazwa użytkownika")
            .put("email", "Email")
            .put("name", "Nazwa")
            .put("surname", "Nazwisko")
            .put("tickets", "Bilety")
            .build();

    public static Map<String, String> getTooltips() {
        return TOOLTIPS;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_user_gen")
    @Id
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;
    private String name;
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
