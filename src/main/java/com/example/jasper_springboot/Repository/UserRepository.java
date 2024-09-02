package com.example.jasper_springboot.Repository;

import com.example.jasper_springboot.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("select u from User u where lower(u.username) like lower(concat('%',:searchText,'%')) or lower(u.fullname) like lower(concat('%',:searchText,'%') ) or lower(u.email) like lower(concat('%',:searchText,'%'))")
    Page<User> findUsersBySearchText(@Param("searchText") String searchText, Pageable pageable);
}
