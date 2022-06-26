package forNews.api



import forNews.util.Constants.Companion.API_KEY
import models.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsAPI { // with the help of this interface we request data from api

    @GET("v2/everything")
    suspend fun getBreakingNews(
        @Query("q")
        searchQuery:String = "fitness",
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ):Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String ,
        @Query("page")
        pageNumber:Int =1,
        @Query("apiKey")
        apiKey:String = API_KEY
    ):Response<NewsResponse>
}