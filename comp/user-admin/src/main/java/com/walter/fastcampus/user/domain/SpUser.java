package com.walter.fastcampus.user.domain;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_user")
public class SpUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name="user_id"))
    private Set<SpAuthority> authorities;

    private String email;

    private String password;

    private boolean enabled;

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

}
