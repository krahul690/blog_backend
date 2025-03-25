package com.blog.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.blog.service.entity.BlogEntity;

public interface IBlogRepository extends JpaRepository<BlogEntity, Long> {

}
