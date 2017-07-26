package ru.guu.radiog.network

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import ru.guu.radiog.network.model.ItunesSearchResult

/**
 * Created by dmitry on 26.07.17.
 */

//queryURL = String(format: "https://itunes.apple.com/search?term=%@+%@&entity=song", track.artist, track.title)

interface ItunesApiService {

    @GET("/search?")
    fun getSongs(
            @Query("term") term: String,
            @Query("entity") entity: String
    ): Observable<ItunesSearchResult>

}
