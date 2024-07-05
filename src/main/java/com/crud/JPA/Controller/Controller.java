package com.crud.JPA.Controller;

import com.crud.JPA.Model.ApiResponse;
import com.crud.JPA.Model.JobPost;
import com.crud.JPA.Service.JobService;
import com.crud.JPA.Validation.ValidateJobPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    JobService service;

    @Autowired
    ValidateJobPost validate;

    @GetMapping("jobPosts")
    public ResponseEntity<ApiResponse> getAllJobs() {
        ApiResponse response;
        try {
            List<JobPost> jobs = service.getAllJobs();

            if (jobs != null && !jobs.isEmpty()) {
                response = new ApiResponse<>(jobs, true, HttpStatus.OK.value(), "successfully", null);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response = new ApiResponse<>(null, false, HttpStatus.NOT_FOUND.value(), "failed", "No Job post found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            response = new ApiResponse<>(null, false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


// with proper response

    //    view job by id
//
    @GetMapping("jobPost/{postId}")
    public ResponseEntity<ApiResponse> getJob(@PathVariable("postId") int postId) {
        JobPost job = service.getJob(postId);
        ApiResponse response;

        if (job != null && job.getPostId() != 0) {
            response = new ApiResponse<>(job, true, HttpStatus.OK.value(), "successfully", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse<>(null, false, HttpStatus.NOT_FOUND.value(), "failed", "No Job Post for the given ID");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    //
//
//// try to get a good reponse
//
    @PostMapping("/jobPost")
    public ResponseEntity<ApiResponse> addJob(@RequestBody JobPost jobPost) {
        ApiResponse response = service.addJob(jobPost);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    //
//
//
//    //    post a new job in jobPortal with a proper response "Improvement"
//
    @PutMapping("/jobPost/{postId}")
    public ResponseEntity<ApiResponse> updateJobPost(@PathVariable("postId") int postId, @RequestBody JobPost jobPost) {
        ApiResponse response = service.updateJob(postId, jobPost);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    //
//    //   delete a existing job in jobPortal without a proper response
//
    @DeleteMapping("jobPost/hard/{postId}")
    public ResponseEntity<ApiResponse> hardDeleteJob(@PathVariable("postId") int postId) {
        boolean exists = service.jobExists(postId);

        if (exists) {
            service.hardDeleteJob(postId);
            ApiResponse response = new ApiResponse(null, true, HttpStatus.OK.value(), "Deletion successful for id " + postId, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            ApiResponse response = new ApiResponse(null, false, HttpStatus.NOT_FOUND.value(), "Deletion failed", "JobPost with id " + postId + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    //
//
    @DeleteMapping("jobPost/{postId}")
    public ResponseEntity<ApiResponse> deleteJob(@PathVariable("postId") int postId) {
        boolean exist = service.jobExists(postId);
        ApiResponse response;
        if (exist) {
            service.deleteJob(postId);

            response = new ApiResponse<>(null, true, HttpStatus.OK.value(), "Deletion successful of id " + postId, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse<>(null, false, HttpStatus.NOT_FOUND.value(), "Deletion failed", "JobPost with id " + postId + " is not deleted");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    //
//
    @GetMapping("deleted")
    public ResponseEntity<ApiResponse> getDeletedJobs() {
        List<JobPost> deletedJobs = service.getDeletedJobs();
        ApiResponse response;
        if (!deletedJobs.isEmpty()) {
            response = new ApiResponse(deletedJobs, true, HttpStatus.OK.value(), "Successfully retrieved deleted jobs", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse(null, false, HttpStatus.NOT_FOUND.value(), "No deleted jobs found", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    //
//
////     get all data including deleted also
//
    @GetMapping("all")
    public ResponseEntity<ApiResponse> getAllJobsIncludingDeleted() {
        List<JobPost> jobs = service.getAllJobsIncludingDeleted();
        ApiResponse response;
        if (!jobs.isEmpty()) {
            response = new ApiResponse(jobs, true, HttpStatus.OK.value(), "Successfully retrieved all jobs including deleted", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse(null, false, HttpStatus.NOT_FOUND.value(), "No jobs found, including deleted ones", null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


    //
// fetching a data by keyword and generating a proper response
//
    @GetMapping("jobPosts/keyword/{keyword}")
    public ResponseEntity<ApiResponse> searchByKeyword(@PathVariable("keyword") String keyword) {
        List<JobPost> job = service.search(keyword);
        ApiResponse response;

        if (!job.isEmpty()) {
            response = new ApiResponse(job, true, HttpStatus.OK.value(), "Available keyword in JobPost is " + keyword, null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response = new ApiResponse(null, false, HttpStatus.NOT_FOUND.value(), "Keyword matching is not processed", "Error occurred in finding keyword");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
