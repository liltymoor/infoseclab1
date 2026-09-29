package lilty.infra.infoseclab1.core.service;

import lilty.infra.infoseclab1.core.entity.LabPost;
import lilty.infra.infoseclab1.core.repository.PostRepository;
import lilty.infra.infoseclab1.io.dto.post.CreatePostRequest;
import lilty.infra.infoseclab1.io.dto.post.PostResponse;
import lilty.infra.infoseclab1.utility.OutputSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public List<PostResponse> list() {
        return postRepository.findAllByOrderByCreatedAtAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    public PostResponse create(String authorUsername, CreatePostRequest request) {
        if (request == null
                || request.title() == null
                || request.title().isBlank()
                || request.body() == null
                || request.body().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title and body are required");
        }

        LabPost post = LabPost.builder()
                .title(request.title())
                .body(request.body())
                .authorUsername(authorUsername)
                .createdAt(Instant.now())
                .build();
        return toResponse(postRepository.save(post));
    }

    private PostResponse toResponse(LabPost post) {
        return new PostResponse(
                post.getId().toString(),
                OutputSanitizer.escape(post.getTitle()),
                OutputSanitizer.escape(post.getBody()),
                OutputSanitizer.escape(post.getAuthorUsername()));
    }
}
