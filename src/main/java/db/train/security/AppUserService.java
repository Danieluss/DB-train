package db.train.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;

    //---- non-prod code, keeping admin password inside your app is generally a bad idea
    @PostConstruct
    public void init() {
        AppUser admin = new AppUser();
        admin.setId(1000L);
        admin.setAlias("admin");
        admin.setRole("ROLE_ADMIN");
        admin.setLogin("admin");
        admin.setPassword("admin");
        addEntity(admin);
    }
    //----

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser addEntity(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return appUserRepository.save(user);
    }
}
