package pl.bartlomiej.emailverifydemo.registration.token;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import pl.bartlomiej.emailverifydemo.user.User;

import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerifyToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NaturalId(mutable = false)
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 10;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public VerifyToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getCalculatedExpirationTime();
    }

    private Date getCalculatedExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return calendar.getTime();
    }
}
