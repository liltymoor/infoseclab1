package lilty.infra.infoseclab1.io.controller;

import lilty.infra.infoseclab1.core.service.PostService;
import lilty.infra.infoseclab1.io.dto.post.CreatePostRequest;
import lilty.infra.infoseclab1.io.dto.post.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {
    private final PostService postService;

    @GetMapping("/data")
    public List<PostResponse> data() {
        return postService.list();
    }

    @PostMapping("/posts")
    public ResponseEntity<PostResponse> createPost(@RequestBody CreatePostRequest request) {
        String author = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.create(author, request));
    }
}
