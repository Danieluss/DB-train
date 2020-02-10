package db.train.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

@Service
public class TrainUserService {

    private final TrainUserRepository trainUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TrainUserService(TrainUserRepository trainUserRepository, PasswordEncoder passwordEncoder) {
        this.trainUserRepository = trainUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TrainUser upsertEntity(TrainUser user) {
        TrainUser fetched = trainUserRepository.findById(user.getId()).orElse(null);
        if (fetched != null) {
            user.setId(fetched.getId());
        }
        if (user.getPassword().isEmpty()) {
           if (fetched != null) {
               user.setPassword(fetched.getPassword());
               return save(user);
           }
        }
        return addEntity(user);
    }

    public TrainUser addEntity(@Valid TrainUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return save(user);
    }

    private TrainUser save(TrainUser user) {
        TrainUser returnValue = trainUserRepository.save(user);
        returnValue.setPassword("");
        return returnValue;
    }
}
