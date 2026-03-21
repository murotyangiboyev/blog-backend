package com.myblog.repository;

import com.myblog.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepositoryCustom{

    List<Blog> findAllBlogs();
}
