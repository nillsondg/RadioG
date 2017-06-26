package ru.guu.radiog.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.guu.radiog.network.model.UserData

/**
 * Created by dmitry on 22.06.17.
 */

interface GuuApiService {

    @GET("/auth.php")
    fun authGuu(
            @Query("login") login: String,
            @Query("password") password: String
    ): Observable<ResponseObject<UserData>>
}
