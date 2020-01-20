package db.train.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Primary
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository repository;

    @Autowired
    public UserDetailsServiceImpl(AppUserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String alias) {
        AppUser user = repository.findByAlias(alias);
        if (user == null) {
            throw new RuntimeException(String.format("User, identified by '%s', not found", alias));
        }
        return new User(
                user.getAlias(), user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole()));
    }
}
