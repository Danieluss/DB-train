package db.train.security;

import lombok.Getter;
import lombok.Setter;
import org.webrepogen.annotations.ExcludedEntity;

import javax.persistence.*;

@ExcludedEntity
@Getter
@Setter
@Entity
@SequenceGenerator(name = "user_gen", sequenceName = "user_seq", initialValue = 1000)
public class AppUser {

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_gen")
    @Id
    private Long id;

    @Column(unique = true)
    private String alias;
    private String role;

    @Column(unique = true)
    private String login;
    @Column(unique = true)
    private String password;

}
