package acc.oauth.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name="USER")
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    private String username;
    private String password;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, MANAGER, USER

    private String provider; // 어떤 OAuth인지(google, naver, kakao 등)
    private String providerId; // 해당 OAuth의 key(id)

    private LocalDateTime createdAt;

    @Builder
    public User(String username, String password, String email, Role role, String provider, String providerId, LocalDateTime createdAt){
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
        this.createdAt = createdAt;
    }
}
