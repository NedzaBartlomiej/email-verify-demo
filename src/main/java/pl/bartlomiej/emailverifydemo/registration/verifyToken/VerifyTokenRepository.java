package pl.bartlomiej.emailverifydemo.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {

    VerifyToken findByToken(String verifyToken);

    List<VerifyToken> getVerifyTokens();
}
