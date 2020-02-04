package db.train.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TrainUserService {

    private final TrainUserRepository trainUserRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public TrainUserService(TrainUserRepository trainUserRepository, PasswordEncoder passwordEncoder) {
        this.trainUserRepository = trainUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TrainUser addEntity(TrainUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return trainUserRepository.save(user);
    }
}
