package lilty.infra.infoseclab1.core.repository;

import lilty.infra.infoseclab1.core.entity.LabPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<LabPost, UUID> {
    List<LabPost> findAllByOrderByCreatedAtAsc();
}
