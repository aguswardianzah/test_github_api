package com.agusw.test_github_api.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MoshiModules {

    @Provides
    @Reusable
    fun instance(): Moshi = Moshi.Builder().build()
}