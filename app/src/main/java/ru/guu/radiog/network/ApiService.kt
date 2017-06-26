package ru.guu.radiog.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import ru.guu.radiog.network.model.Event

/**
 * Created by dmitry on 22.06.17.
 */

interface ApiService {

    @GET("/api/v1/events")
    fun getEvents(
            @Header("Authorization") authorization: String,
            @Query("organization_id") organizationId: Int,
            @Query("date") date: String,
            @Query("fields") fields: String,
            @Query("order_by") orderBy: String,
            @Query("length") length: Int,
            @Query("offset") offset: Int
    ): Observable<ResponseArray<Event>>

}
