package com.crud.JPA.Repository;

import com.crud.JPA.Model.JobPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepo extends JpaRepository<JobPost,Integer> {

    List<JobPost> findByPostProfileContainingOrPostDescContaining(String postProfile, String postDesc);

    List<JobPost> findByDeletedTrue();


    boolean existsByMobileNumber(String mobileNumber);

    boolean existsByEmailId(String emailId);
}
