package com.cian.orderbook.di

import com.cian.orderbook.data.repository.OrderBookRepositoryImpl
import com.cian.orderbook.domain.repository.OrderBookRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOrderBookRepository(impl: OrderBookRepositoryImpl): OrderBookRepository
}
