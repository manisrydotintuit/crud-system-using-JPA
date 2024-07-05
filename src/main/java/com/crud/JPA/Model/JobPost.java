package com.crud.JPA.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
@Entity


public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int postId;
    private String postProfile;
    private String postDesc;
    private Integer postExperience;
    private boolean deleted = false;
    private String mobileNumber;
    private String emailId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    mobile :should be atleast 10 digit
//    email: should be in email format
//    createdAt: this createdAt will be added by default
//    updatedAt : if update that datetime
}
