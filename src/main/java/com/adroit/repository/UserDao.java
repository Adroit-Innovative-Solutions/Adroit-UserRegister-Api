package com.adroit.repository;

import com.adroit.model.UserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface    UserDao extends JpaRepository<UserDetails, Integer> {

    UserDetails findByEmail(String email);
    UserDetails findByUserId(String userId);
    UserDetails findByUserName(String userName);
    List<UserDetails> findByRolesId(Long id);

    @Query("SELECT u FROM UserDetails u WHERE u.personalEmail = :personalEmail")
    UserDetails findByPersonalEmail(@Param("personalEmail") String personalEmail);


    @Query(value = "SELECT * FROM user_details WHERE joining_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<UserDetails> findEmployeesByJoiningDateRange(@Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);


    Page<UserDetails> findAll(Pageable pageable);

}
