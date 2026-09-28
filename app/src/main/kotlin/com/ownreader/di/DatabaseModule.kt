package com.ownreader.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Database bindings live in data.di.DatabaseModule. */
@Module
@InstallIn(SingletonComponent::class)
object LegacyDatabaseModule
