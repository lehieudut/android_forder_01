package com.framgia.forder.data.source.remote.api.service;

import com.framgia.forder.data.model.UserReponse;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by le.quang.dao on 10/03/2017.
 */

public interface FOrderApi {

    // TODO: 11/04/2017 remove later
    @GET("/search/users")
    Observable<Void> searchGithubUsers(@Query("per_page") int limit, @Query("q") String searchTerm);

    @GET("v1/authen_user_tokens")
    Observable<UserReponse> login(@Query("user_email") String userEmail,
            @Query("password") String passWord);
}
