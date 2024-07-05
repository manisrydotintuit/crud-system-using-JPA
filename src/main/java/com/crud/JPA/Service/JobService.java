package com.crud.JPA.Service;

import com.crud.JPA.Model.ApiResponse;
import com.crud.JPA.Model.JobPost;
import com.crud.JPA.Repository.JobRepo;
import com.crud.JPA.Validation.ValidateJobPost;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class JobService {

    @Autowired
    JobRepo repo;

    @Autowired
    ValidateJobPost validate;
//    find all jobs

    public List<JobPost> getAllJobs() {
        return repo.findAll()
                .stream()
                .filter(jobPost -> !jobPost.isDeleted())
                .collect(Collectors.toList());
    }


    //    find jobs by an id
    public JobPost getJob(int postId) {
        return repo.findById(postId).orElse(null);
    }


    //    if job is already stored in the database
    public boolean jobExists(int postId) {
        return repo.existsById(postId);
    }

    //    add jobs in a database
    public ApiResponse addJob(JobPost jobPost) {
        // Validate job post
        String validateError = validate.postJobPost(jobPost);
        if (validateError != null) {
            return validate.createBadRequestResponse("Validation error: " + validateError);
        }

        // Validate mobile number
        String mobileNumberStr = jobPost.getMobileNumber();
        if (mobileNumberStr != null && !mobileNumberStr.isEmpty()) {
            if (!mobileNumberStr.matches("\\d{10}")) {
                String errorMessage = mobileNumberStr.length() != 10 ?
                        "Mobile No should be exactly 10 digits" :
                        "Invalid Mobile Number. Mobile Number must be numeric";
                return validate.createBadRequestResponse(errorMessage);
            }
        } else {
            return validate.createBadRequestResponse("Mobile Number is required");
        }

        // Validate email address
        String emailValidator = jobPost.getEmailId();
        if (emailValidator != null && !emailValidator.isEmpty()) {
            // Regular expression for email validation
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-    ]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(emailValidator);

            if (!matcher.matches()) {
                return validate.createBadRequestResponse("Invalid Email Address");
            }
        }

        // Check if mobile number already exists
        if (existsByMobileNumber(jobPost.getMobileNumber())) {
            return validate.createBadRequestResponse("Mobile number is already existed for any other JobPost");
        }

        // Check if email ID already exists
        if (existsByEmailId(jobPost.getEmailId())) {
            return validate.createBadRequestResponse("Email ID is already existed by any JobPost");
        }

        // Check if job post already exists
        if (jobExists(jobPost.getPostId())) {
            return validate.createConflictResponse("Data is already stored for given id", "JobPost with id " + jobPost.getPostId() + " already exists");
        }

        // Add job post
        jobPost.setCreatedAt(LocalDateTime.now());
        JobPost savedJobPost = repo.save(jobPost);
        return validate.createOkResponse(savedJobPost, "JobPost is uploaded successfully");
    }

    //
//
    public ApiResponse updateJob(int postId, JobPost jobPost) {
        JobPost existingJobPost = repo.findById(postId).orElse(null);
        if (existingJobPost == null) {
            return validate.createNotFoundResponse("JobPost not found");
        }

        // Validate and update job post
        ApiResponse validationResponse = validateAndUpdateJob(existingJobPost, jobPost);
        if (!validationResponse.isSuccess()) {
            return validationResponse;
        }

        JobPost updatedJobPost = updateJobLogic(existingJobPost);
        return validate.createOkResponse(updatedJobPost, "JobPost updated successfully");
    }


    private ApiResponse<JobPost> validateAndUpdateJob(JobPost existingJobPost, JobPost jobPost) {
        // Validate job post fields and update if validation passes
        if (jobPost.getPostProfile() != null && !jobPost.getPostProfile().isEmpty()) {
            existingJobPost.setPostProfile(jobPost.getPostProfile());
        }

        if (jobPost.getPostDesc() != null && !jobPost.getPostDesc().isEmpty()) {
            existingJobPost.setPostDesc(jobPost.getPostDesc());
        }

        if (jobPost.getPostExperience() != null) {
            Integer postExperience = jobPost.getPostExperience();
            if (postExperience <= 0 || postExperience >= 40) {
                return validate.createBadRequestResponse("Post Experience must be between 1 and 39");
            }
            existingJobPost.setPostExperience(postExperience);
        }

        String mobileNumberStr = jobPost.getMobileNumber();
        if (mobileNumberStr != null && !mobileNumberStr.isEmpty()) {
            if (!mobileNumberStr.matches("\\d{10}")) {
                String errorMessage = mobileNumberStr.length() != 10 ?
                        "Mobile No should be exactly 10 digits" :
                        "Invalid Mobile Number. Mobile Number must be numeric";
                return validate.createBadRequestResponse(errorMessage);
            }
            existingJobPost.setMobileNumber(mobileNumberStr);
        }

        String emailValidator = jobPost.getEmailId();
        if (emailValidator != null && !emailValidator.isEmpty()) {
            // Regular expression for email validation
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

            Pattern pattern = Pattern.compile(emailRegex);
            Matcher matcher = pattern.matcher(emailValidator);

            if (!matcher.matches()) {
                return validate.createBadRequestResponse("Invalid Email Address");
            }

            existingJobPost.setEmailId(emailValidator);
        }

        return validate.createOkResponse(existingJobPost, "Validation successful");
    }

    private JobPost updateJobLogic(JobPost existingJobPost) {
        existingJobPost.setUpdatedAt(LocalDateTime.now());
        return repo.save(existingJobPost);
    }


    public void hardDeleteJob(int postId) {
        repo.deleteById(postId);
    }

    @Transactional
    public void deleteJob(int postId) {
        Optional<JobPost> optionalJobPost = repo.findById(postId);
        optionalJobPost.ifPresent(jobPost -> {
            jobPost.setDeleted(true);
            repo.save(jobPost);
        });
    }


    public List<JobPost> getDeletedJobs() {
        return repo.findByDeletedTrue();
    }

//    load data into the database when database is empty

    //    search by keyword from our database
    public List<JobPost> search(String keyword) {
        return repo.findByPostProfileContainingOrPostDescContaining(keyword, keyword);
    }

    public List<JobPost> getAllJobsIncludingDeleted() {
        return repo.findAll();
    }


    public boolean existsByMobileNumber(String mobileNumber) {
        return repo.existsByMobileNumber(mobileNumber);
    }

    public boolean existsByEmailId(String emailId) {
        return repo.existsByEmailId(emailId);
    }
}
