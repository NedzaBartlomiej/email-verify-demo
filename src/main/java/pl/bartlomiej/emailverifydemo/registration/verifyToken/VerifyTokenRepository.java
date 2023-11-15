package pl.bartlomiej.emailverifydemo.registration.verifyToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerifyTokenRepository extends JpaRepository<VerifyToken, Long> {

    VerifyToken findByToken(String verifyToken);

    @Query("SELECT vt FROM VerifyToken vt WHERE vt.expirationTime <= CURRENT_TIMESTAMP")
    List<VerifyToken> findExpiredVerifyTokens();

    @Query("SELECT vt FROM VerifyToken vt WHERE vt.isUsed = true")
    List<VerifyToken> findUsedVerifyTokens();
}
