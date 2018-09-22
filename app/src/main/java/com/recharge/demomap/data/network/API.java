package com.recharge.demomap.data.network;



import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface API {

   /* @FormUrlEncoded
    @POST("user/login")
    Single<User> login(@FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("user/forgetpass")
    Single<Response> forgotPassword(@Field("email_address") String email);

    @FormUrlEncoded
    @POST("user/changepassword")
    Single<Response> changePassword(@FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("user/deleteaccount")
    Single<Response> deleteAccount(@FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("user/{profile_path}")
    Single<User> profile(@FieldMap HashMap<String, String> postData, @Path("profile_path") String path);

    @Headers({"enctype:multipart/form-data"})
    @Multipart
    @POST("user/signup")
    Single<User> signUp(@PartMap Map<String, RequestBody> multipartBody);


    @Multipart
    @POST("user/dropboxUpload")
    Single<Response> updateCv(@PartMap Map<String, RequestBody> multipartBody, @Part MultipartBody.Part file);


    //@Headers({"enctype:multipart/form-data"})
    @Multipart
    @POST("user/fileUpload")
    Single<Response> uploadDocumentCv(@PartMap Map<String, RequestBody> multipartBody);

    @Multipart
    @POST("user/fileUpload")
    Single<Response> uploadDocumentCv(@PartMap Map<String, RequestBody> multipartBody, @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("user/videoupload")
    Single<Response> updateVideoCv(@FieldMap Map<String, String> postbody);


    @Multipart
    @POST("jobs/jobapplysave")
    Single<Response> jobApply(@PartMap Map<String, RequestBody> multipartBody);

    @Multipart
    @POST("user/{edit_profile}")
    Single<User> saveProfileInfo(
            @Part MultipartBody.Part[] part,
            @PartMap Map<String, RequestBody> multipartBody,
            @Path("edit_profile") String path
    );

    @Multipart
    @POST("jobs/post_job")
    Single<Response> postJob2(
            @Part MultipartBody.Part part,
            @PartMap Map<String, RequestBody> multipartBody
    );

    @Headers({"enctype:multipart/form-data"})
    @Multipart
    @POST("jobs/post_job")
    Single<Response> postJob(@PartMap Map<String, RequestBody> postParams);

    @Headers({"enctype:multipart/form-data"})
    @Multipart
    @POST("jobs/editjob")
    Single<Response> editJob(@PartMap Map<String, RequestBody> postParams);

    @GET("jobs/joblist")
    Single<Jobs> jobList ();

    @FormUrlEncoded
    @POST("jobs/{joblist_type}")
    Single<Jobs> jobList(@FieldMap HashMap<String, String> postData, @Path("joblist_type") String jobType);

    @FormUrlEncoded
    @POST("user/myplan_list")
    Single<Plans> getMyplans(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("jobs/jobsearch")
    Single<Jobs> searchJobs(@FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("jobs/jobalert")
    Single<Jobs> alerts(@Field("candidate_id") String candidateId);

    @FormUrlEncoded
    @POST("jobs/bookmark")
    Single<Response> addToBookmark(@FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("jobs/removebookmark")
    Single<Response> removeFromBookmark(@FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("jobs/bookmarklist")
    Single<Jobs> bookmarkList(@Field("user_id") String userId);

    @FormUrlEncoded
    @POST("jobs/jobdetails")
    Single<Jobs> jobDetails(@Field("job_id") String jobId, @Field("user_id") String userId);

    @FormUrlEncoded
    @POST("jobs/viewjob")
    Single<Jobs> jobDetails(@Field("job_id") String jobId);

    @FormUrlEncoded
    @POST("jobs/appliedjoblist")
    Single<Jobs> getAppliedJobs(@Field("candidate_id") String candidate_id);

    @FormUrlEncoded
    @POST("jobs/deletejob")
    Single<Response> deleteJob(@Field("job_id") String jobId);


    @GET("api/countrylist")
    Single<CommonList> countries ();

    @GET("api/job_type_list")
    Single<CommonList> jobType ();

    @GET("api/job_category_list")
    Single<CommonList> jobCategory ();

    @FormUrlEncoded
    @POST("api/statelist")
    Single<CommonList> states(@Field("country_id") String countryId);

    @FormUrlEncoded
    @POST("jobs/getcitybystate")
    Single<CommonList> cities(@Field("state") String stateId);

    @FormUrlEncoded
    @POST("api/cmscontactussubmit")
    Single<Response> contactUs(@FieldMap HashMap<String, String> postData);

    @GET("api/home")
    Single<Home> home ();

    @GET("api/cmsaboutus")
    Single<AboutUs> aboutUs ();

    @FormUrlEncoded
    @POST("api/dropbox_video_link")
    Single<Response> getDropboxDocumentLink(@Field("name") String name);

    @Headers({"enctype:multipart/form-data"})
    @Multipart
    @POST("user/{edit_profile}")
    Single<User> saveProfileInfo(@PartMap Map<String, RequestBody> multipartBody, @Path("edit_profile") String path);
    //@Headers({"enctype:multipart/form-data"})

    @POST("jobs/advanceseachmenu")
    Single<Filter> getAdavancedSearchMenu ();

    @FormUrlEncoded
    @POST("jobs/advancesearch")
    Single<Jobs> getAdvanceSearchJobFiler(@FieldMap HashMap<String, String> postData);

    @FormUrlEncoded
    @POST("user/profile_complete")
    Single<Response> profileComapletPercentage(@Field("user_id") String user_id);

    @Headers({"enctype:multipart/form-data"})
    @Multipart
    @POST("user/employersignup")
    Single<Response> signUpEmployee(@PartMap Map<String, RequestBody> postData);


    @GET("api/blog")
    Single<Blog> getBlogs ();

    @FormUrlEncoded
    @POST("api/specific_blog")
    Single<Blog> getParticularBlogs(@Field("blog_id") String blog_id);


    @FormUrlEncoded
    @POST("user/candidatePaymentlist")
    Single<User> getPaymentHistry(@Field("candidate_id") String user_id);


     @FormUrlEncoded
     @POST("user/{paymenttype}")
     Single<User> getAllPaymentHistoryEmployee(@Field("user_id") String user_id, @Path("paymenttype") String paymentType);



    @GET("api/onlinetraining")
    Single<TrainingCourse> getTrainingCourses ();

    @GET("api/content_training")
    Single<TrainingCourse> getTrainingContent ();

    @FormUrlEncoded
    @POST("api/training_details")
    Single<TrainingCourse> getTrainingCourseDetails(@Field("course_id") String course_id);


    @FormUrlEncoded
    @POST("api/course_search")
    Single<TrainingCourse> getAdvancedTraniningCourses(@FieldMap HashMap<String, String> postdata);


    @FormUrlEncoded
    @POST("jobs/viewapplicant")
    Single<Jobs> getApplicants(@Field("job_id") String job_id);

    @FormUrlEncoded
    @POST("jobs/jobapplications")
    Single<Jobs> getJobApplications(@Field("user_id") String userId);


    @FormUrlEncoded
    @POST("api/addComment")
    Single<Response> addBlogComment(@FieldMap HashMap<String, String> postParam);

    @GET("api/citylist")
    Single<CommonList> getCityList ();

    @FormUrlEncoded
    @POST("user/resumeSearch")
    Single<User> resumeSearch(@FieldMap HashMap<String, String> postParam);

    @GET("api/membershipplan_list")
    Single<Plans> getALLPlans ();


    @GET("user/new_membership_list")
    Single<MembershipPlanList> getMembershipList ();


    @FormUrlEncoded
    @POST("jobs/fileshow")
    Single<Response> getZigeoToken(@FieldMap HashMap<String, String> postParam);

    @GET("api/job_category_list")
    Single<CategoryList> categoryItems ();

    @FormUrlEncoded
    @POST("user/social_login")
    Single<User> socialLogin(@FieldMap HashMap<String, String> postdata);


    @POST("jobs/jobhomepage")
    Single<HomeJobMenu> getHomeJobs ();

*/

}
