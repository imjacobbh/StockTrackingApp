package com.example.stocktrackingapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.stocktrackingapp.BuildConfig
import com.example.stocktrackingapp.data.repository.callback.StockRepository
import com.example.stocktrackingapp.data.repository.helper.StocksRepositoryHelper
import com.example.stocktrackingapp.data.repository.implementations.StockRepositoryImpl
import com.example.stocktrackingapp.data.repository.mapper.StocksMapper
import com.example.stocktrackingapp.data.source.remote.ApiService
import com.example.stocktrackingapp.data.source.utils.HttpConnectionUtil
import com.example.stocktrackingapp.data.source.utils.NetworkStatusUtil
import com.example.stocktrackingapp.domain.interactor.SearchStocksInteractor
import com.example.stocktrackingapp.domain.interactor.StocksInteractor
import com.example.stocktrackingapp.domain.viewmodels.SearchStocksViewModel
import com.example.stocktrackingapp.domain.viewmodels.StocksViewModel
import com.google.gson.Gson
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
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideURL(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
    ): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideAPIServices(
        baseUrl: String,
        gson: Gson,
        okHttpClient: OkHttpClient
    ): ApiService {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(baseUrl)
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideHttpConnectionUtil(): HttpConnectionUtil = HttpConnectionUtil()

    @Provides
    @Singleton
    fun provideNetworkUtil(
        @ApplicationContext context: Context
    ): NetworkStatusUtil = NetworkStatusUtil(context)

    @Provides
    internal fun provideStockRepository(
        networkUtil: NetworkStatusUtil,
        httpConnectionUtil: HttpConnectionUtil,
        helper: StocksRepositoryHelper
    ): StockRepository = StockRepositoryImpl(networkUtil, httpConnectionUtil, helper)

    @Provides
    internal fun provideStocksRepositoryHelper(
        retrofitService: ApiService,
        mapper: StocksMapper
    ): StocksRepositoryHelper = StocksRepositoryHelper(retrofitService, mapper)

    @Provides
    internal fun provideSearchStocksViewModel(
        interactor: SearchStocksInteractor
    ): ViewModel {
        return SearchStocksViewModel(interactor)
    }

    @Provides
    internal fun provideStocksViewModel(
        interactor: StocksInteractor
    ): ViewModel {
        return StocksViewModel(interactor)
    }


    @Provides
    @Singleton
    fun provideStocksMapper(): StocksMapper {
        return StocksMapper()
    }
}
