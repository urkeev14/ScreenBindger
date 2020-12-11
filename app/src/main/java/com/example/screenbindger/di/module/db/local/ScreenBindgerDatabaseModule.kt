package com.example.screenbindger.di.module.db.local

import androidx.room.Room
import androidx.room.Room.databaseBuilder
import com.example.screenbindger.app.ScreenBindger
import com.example.screenbindger.db.local.db.ScreenBindgerDatabase
import com.example.screenbindger.util.constants.LOCAL_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ScreenBindgerDatabaseModule {

    @Singleton
    @Provides
    fun provideScreenBindgerDatabase(): ScreenBindgerDatabase{
        return databaseBuilder(
            ScreenBindger.context(),
            ScreenBindgerDatabase::class.java,
            LOCAL_DATABASE_NAME
        ).build()
    }


}