package com.blog.service.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blog.service.dto.BlogDTO;
import com.blog.service.entity.BlogEntity;
import com.blog.service.service.IBlogService;
import com.blog.service.service.OpenAIService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

	private final OpenAIService openAIService;
    private final IBlogService blogService;


    @PostMapping
    public ResponseEntity<BlogEntity> createBlog(@RequestBody BlogDTO blogDTO) {
        BlogEntity createdBlog = blogService.createBlog(blogDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBlog);
    }
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBlogs(
            @RequestParam(name="page", defaultValue = "0") int page,
            @RequestParam(name="size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogEntity> blogPage = blogService.getAllBlogs(pageable);

        List<BlogDTO> blogDTOs = blogPage.getContent().stream()
                .map(this::convertToDTO)
                .toList(); 
        // return pagination details
        Map<String, Object> response = new HashMap<>();
        response.put("blogs", blogDTOs);
        response.put("currentPage", blogPage.getNumber());
        response.put("totalPages", blogPage.getTotalPages());
        response.put("totalElements", blogPage.getTotalElements());

        return ResponseEntity.ok(response);
    }

    // BlogEntity to BlogDTO
    private BlogDTO convertToDTO(BlogEntity entity) {
        return BlogDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .author(entity.getAuthor())
                .createdAt(entity.getCreatedAt())
                .build();
    }



    @GetMapping("/{id}")
    public ResponseEntity<BlogEntity> getBlogById(@PathVariable("id") Long id) {
        BlogEntity blog = blogService.getBlogById(id);
        return ResponseEntity.ok(blog);
    }


    @PutMapping("/{id}")
    public ResponseEntity<BlogEntity> updateBlog(@PathVariable("id") Long id, @RequestBody BlogDTO blogDTO) {
        BlogEntity updatedBlog = blogService.updateBlog(id, blogDTO);
        return ResponseEntity.ok(updatedBlog);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.noContent().build();
    }
    
    //for summary
    @PostMapping("/summarize/{id}")
    public ResponseEntity<String> summarizeBlog(@PathVariable("id") Long id) {
    	BlogEntity blogById = blogService.getBlogById(id);
    	String blogContent = blogById.getContent();
        String summary = openAIService.summarizeBlog(blogContent);
        return ResponseEntity.ok(summary);
    }
    
}
