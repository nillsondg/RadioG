package ru.guu.radiog.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.guu.radiog.network.model.LastFmApiResult

/**
 * Created by dmitry on 26.07.17.
 */
interface LastFmApiService {
    val method: String
        get() = "track.getInfo"
    val format: String
        get() = "json"

    @GET("/2.0/?")
    fun getSongs(
            @Query("method") method: String,
            @Query("api_key") apiKey: String,
            @Query("artist") artist: String,
            @Query("track") track: String,
            @Query("format") format: String
    ): Observable<LastFmApiResult>

}
