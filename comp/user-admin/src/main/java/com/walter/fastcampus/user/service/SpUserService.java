package com.walter.fastcampus.user.service;

import com.walter.fastcampus.user.domain.SpAuthority;
import com.walter.fastcampus.user.domain.SpUser;
import com.walter.fastcampus.user.repository.SpUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class SpUserService implements UserDetailsService {

    private final SpUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SpUser spUser = userRepository.findSpUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return spUser;
    }

    public Optional<SpUser> findUser(String email){
        return userRepository.findSpUserByEmail(email);
    }

    public SpUser save(SpUser user){
        return userRepository.save(user);
    }

    public void addAuthority(Long userId, String authority) {
        userRepository.findById(userId).ifPresent(
                user -> {
                    SpAuthority newRole = new SpAuthority(user.getUserId(), authority);

                    if(user.getAuthorities() == null){
                        HashSet<SpAuthority> authorities = new HashSet<>();
                        authorities.add(newRole);
                        user.setAuthorities(authorities);
                        save(user);
                    }
                    else if (!user.getAuthorities().contains(newRole)){
                        HashSet<SpAuthority> authorities = new HashSet<>();
                        authorities.addAll(user.getAuthorities());
                        authorities.add(newRole);
                        user.setAuthorities(authorities);
                        save(user);
                    }
                }
        );
    }

    public void removeAuthority(Long userId, String authority){
        userRepository.findById(userId).ifPresent(
                user->{
                    if(user.getAuthorities()==null) return;
                    SpAuthority targetRole = new SpAuthority(user.getUserId(), authority);
                    if (user.getAuthorities().contains(targetRole)) {
                        user.setAuthorities(
                                user.getAuthorities().stream().filter(auth->!equals(targetRole)).collect(Collectors.toSet())
                        );

                        save(user);
                    }
                }
        );
    }
}
