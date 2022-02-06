package ru.uzaretskaya.todo.auth.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Setter
@Table(name = "USER_DATA")
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Email
    private String email;

    private String password;

    private String username;

    @OneToOne(mappedBy = "user", fetch = LAZY)
    private Activity activity;

    @ManyToMany(fetch = LAZY)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof User user) {
            return email.equals(user.email);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
