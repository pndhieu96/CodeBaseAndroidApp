package com.example.codebaseandroidapp.di

import com.example.codebaseandroidapp.ui.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Qualifier

@Module
@InstallIn(FragmentComponent::class)
class FragmentModule {

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class EmailController

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PhoneController

    @Provides
    fun provideEmailSender() : EmailSender {
        return EmailSender()
    }

    @Provides
    fun providePhoneSender() : PhoneSender {
        return PhoneSender()
    }

    @EmailController
    @Provides
    fun provideEmailController(
        sender: PhoneSender
    ) : SenderController {
        return SenderController(sender)
    }

    @PhoneController
    @Provides
    fun providePhoneController(
        sender: EmailSender
    ) : SenderController {
        return SenderController(sender)
    }

    @Provides
    fun provideUserController(): UserController {
        return UserController()
    }
}