package com.example.navermovieapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.navermovieapp.api.MovieApi
import com.example.navermovieapp.data.MovieRepository
import com.example.navermovieapp.data.MovieRepositoryImpl
import com.example.navermovieapp.db.HistoryDatabase
import com.example.navermovieapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideMovieApi(): MovieApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging).build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): HistoryDatabase =
        Room.databaseBuilder(
            app,
            HistoryDatabase::class.java,
            "saved_history_keywords.db"
        ).build()

    @Provides
    @Singleton
    fun provideMovieRepository(api: MovieApi, db: HistoryDatabase): MovieRepository =
        MovieRepositoryImpl(api, db)



}