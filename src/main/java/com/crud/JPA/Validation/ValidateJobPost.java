package com.crud.JPA.Validation;

import com.crud.JPA.Model.ApiResponse;
import com.crud.JPA.Model.JobPost;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;



@Component
public class ValidateJobPost {

    public  String validateJobPost(JobPost jobPost){

        if(jobPost.getPostProfile() != null && jobPost.getPostProfile().isEmpty()){
            return "Post Profile can not be null or empty";
        }
        if(jobPost.getPostDesc() != null && jobPost.getPostDesc().isEmpty()){
            return "Post Desc can not be null or empty";
        }
        if(jobPost.getPostExperience() != null){
            return "Post Experience can not be null";
        }
        if(jobPost.getMobileNumber() != null && jobPost.getMobileNumber().isEmpty()){
            return "Mobile number cannot be null";
        }
        if(jobPost.getEmailId() != null && jobPost.getEmailId().isEmpty()){
            return  "Email id cannot be null or empty";
        }
        return null;
    }



    public String postJobPost(JobPost jobPost) {

        if (jobPost.getPostProfile() == null || jobPost.getPostProfile().isEmpty()) {
            return "Post Profile cannot be null or empty";
        }
        if (jobPost.getPostDesc() == null || jobPost.getPostDesc().isEmpty()) {
            return "Post Desc cannot be null or empty";
        }
        if (jobPost.getPostExperience() == null) {
            return "Post Experience cannot be null";
        }
        if (jobPost.getMobileNumber() == null || jobPost.getMobileNumber().isEmpty()) {
            return "Mobile number cannot be null or empty";
        }
        if (jobPost.getEmailId() == null || jobPost.getEmailId().isEmpty()) {
            return "Email id cannot be null or empty";
        }
        return null;
    }

    public ApiResponse createOkResponse(Object data, String message) {
        return new ApiResponse(data, true, HttpStatus.OK.value(), message, null);
    }

    public ApiResponse createBadRequestResponse(String errorMessage) {
        return new ApiResponse(null, false, HttpStatus.BAD_REQUEST.value(), errorMessage, null);
    }

    public ApiResponse createConflictResponse(String errorMessage, String details) {
        return new ApiResponse(null, false, HttpStatus.CONFLICT.value(), errorMessage, details);
    }

    public ApiResponse createErrorResponse(HttpStatus status, String errorMessage, String details) {
        return new ApiResponse(null, false, status.value(), errorMessage, details);
    }

    public ApiResponse createNotFoundResponse(String jobPostNotFound) {
        return new ApiResponse(null, false, HttpStatus.NOT_FOUND.value(), "JobPost not found", jobPostNotFound);
    }



}
