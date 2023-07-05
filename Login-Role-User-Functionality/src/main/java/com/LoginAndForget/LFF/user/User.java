package com.LoginAndForget.LFF.user;

import com.LoginAndForget.LFF.user.role.Roles;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId(mutable = true)
    private String email;
    private String password;
    private String partnerId;
    private LocalDateTime lastLogin=LocalDateTime.now();
    private boolean isEnabled = false;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
        private Set<Roles> roles;

    public User() {
        this.roles = new HashSet<>();
    }
}
