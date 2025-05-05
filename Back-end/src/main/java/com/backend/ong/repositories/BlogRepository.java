package com.backend.ong.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.backend.ong.entity.Blog;

@Repository
public interface BlogRepository extends JpaRepository <Blog, Long>{

}
