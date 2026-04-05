package com.myblog.repository;

import com.myblog.model.Blog;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import java.util.List;

public class BlogRepositoryCustomImpl implements BlogRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Blog> findAllBlogs(){
        StringBuilder sb = new StringBuilder();
        sb.append("select b from Blog b");
        Query query = entityManager.createQuery(sb.toString());
        query.setFirstResult(0);
        query.setMaxResults(10);
        List<Blog> blogs = query.getResultList();

        return  blogs;
    }

}
