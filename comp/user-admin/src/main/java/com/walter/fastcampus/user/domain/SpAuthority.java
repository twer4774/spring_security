package com.walter.fastcampus.user.domain;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;

import javax.persistence.*;

/**
 * {@link org.springframework.security.core.GrantedAuthority} 구현 객체
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="sp_user_authority")
@IdClass(SpAuthority.class)
public class SpAuthority implements GrantedAuthority {

    @Id
    @Column(name="user_id")
    private Long userId;

    @Id
    private String authority;
}
