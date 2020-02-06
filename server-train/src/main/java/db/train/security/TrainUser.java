package db.train.security;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.ImmutableMap;
import db.train.persistence.model.Ticket;
import lombok.Getter;
import lombok.Setter;
import org.webrepogen.annotations.ExcludedEntity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@SequenceGenerator(name = "train_user_gen", sequenceName = "train_user_seq", initialValue = 1000)
@Table(indexes = {
    @Index(columnList = "id", name = "id"),
    @Index(columnList = "email", name = "email")
})
@ExcludedEntity
public class TrainUser {

    public static TrainUser from(TrainUserDTO trainUserDTO) {
        TrainUser trainUser = new TrainUser();
        trainUser.setRole("ROLE_USER");
        trainUser.setTickets(new LinkedList<>());

        trainUser.setUsername(trainUserDTO.getUsername());
        trainUser.setPassword(trainUserDTO.getPassword());
        trainUser.setEmail(trainUserDTO.getEmail());
        trainUser.setName(trainUserDTO.getName());
        trainUser.setSurname(trainUserDTO.getSurname());

        return trainUser;
    }

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "train_user_gen")
    @Id
    private Long id;
    @NotBlank
    @Column(nullable = false, unique = true)
    private String username;
    @NotBlank
    @Column(nullable = false)
    private String password;
    @NotBlank
    @Column(nullable = false)
    private String role;
    @Email
    @Column(nullable = false, unique = true)
    private String email;
    @NotBlank
    @Column(nullable = false)
    private String name;
    @NotBlank
    @Column(nullable = false)
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
