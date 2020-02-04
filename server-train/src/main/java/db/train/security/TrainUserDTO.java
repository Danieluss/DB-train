package db.train.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TrainUserDTO {

    private String username;
    private String password;
    private String email;
    private String name;
    private String surname;

}
