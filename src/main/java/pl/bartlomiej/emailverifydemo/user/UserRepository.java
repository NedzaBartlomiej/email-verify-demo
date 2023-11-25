package pl.bartlomiej.emailverifydemo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.role=?1 WHERE u.id=?2")
    void updateRoleById(String role, Long userId);
}
