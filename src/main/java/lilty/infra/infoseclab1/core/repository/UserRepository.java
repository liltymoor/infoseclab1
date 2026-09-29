package lilty.infra.infoseclab1.core.repository;

import lilty.infra.infoseclab1.core.entity.LabUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<LabUser, UUID> {
    @Query("select u from LabUser u where u.username = :username")
    Optional<LabUser> findByUsername(@Param("username") String username);

    boolean existsByUsername(String username);
}
