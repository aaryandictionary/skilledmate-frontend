package com.app.miniproject.Database;

import com.app.miniproject.Database.CollegeList.CollegeListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommonInterface {

    @GET("skilledmate/public/api/collegeslist")
    Call<CollegeListResponse> getCollegesList();
}
