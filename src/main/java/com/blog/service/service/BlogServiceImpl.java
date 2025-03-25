package com.blog.service.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.blog.service.dto.BlogDTO;
import com.blog.service.entity.BlogEntity;
import com.blog.service.exception.ResourceNotFoundException;
import com.blog.service.repository.IBlogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements IBlogService {

	private final IBlogRepository blogrepo;
	
	@Override
	public BlogEntity createBlog(BlogDTO blogDTO) {
		BlogEntity blog = BlogEntity.builder()
				.title(blogDTO.getTitle())
				.content(blogDTO.getContent())
				.author(blogDTO.getAuthor())
				.createdAt(new Date()).build();
		return blogrepo.save(blog);
	}

	@Override
    public Page<BlogEntity> getAllBlogs(Pageable pageable) {
        return blogrepo.findAll(pageable);
    }

    @Override
    public BlogEntity getBlogById(Long id) {
        return blogrepo.findById(id)
        		.orElseThrow(()->new ResourceNotFoundException("Blog not found"));
    }

    @Override
    public BlogEntity updateBlog(Long id, BlogDTO blogDTO) {
        BlogEntity blog = blogrepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Blog not found with ID: " + id));

        blog.setTitle(blogDTO.getTitle());
        blog.setContent(blogDTO.getContent());
        blog.setAuthor(blogDTO.getAuthor());

        return blogrepo.save(blog);
    }


    @Override
    public void deleteBlog(Long id) {
        if (!blogrepo.existsById(id)) {
            throw new ResourceNotFoundException("Blog not found");
        }
        blogrepo.deleteById(id);
    }

}
