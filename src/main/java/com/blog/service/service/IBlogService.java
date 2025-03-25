package com.blog.service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.blog.service.dto.BlogDTO;
import com.blog.service.entity.BlogEntity;

public interface IBlogService {
	
	public BlogEntity createBlog(BlogDTO blogDTO);
	public Page<BlogEntity> getAllBlogs(Pageable pageable);
	public BlogEntity getBlogById(Long id);
	public BlogEntity updateBlog(Long id, BlogDTO blogDTO);
	public void deleteBlog(Long id);
}
