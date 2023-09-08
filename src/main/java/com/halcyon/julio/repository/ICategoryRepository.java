package com.halcyon.julio.repository;

import com.halcyon.julio.model.Category;
import com.halcyon.julio.model.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findCategoriesByChannel(Channel channel);
}