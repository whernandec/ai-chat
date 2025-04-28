package com.example.appgeneratorcursoria.di

import androidx.room.Room
import com.example.appgeneratorcursoria.R
import com.example.appgeneratorcursoria.data.local.database.ChatDatabase
import com.example.appgeneratorcursoria.data.repository.ChatRepositoryImpl
import com.example.appgeneratorcursoria.domain.repository.ChatRepository
import com.example.appgeneratorcursoria.domain.usecase.*
import com.example.appgeneratorcursoria.presentation.chat.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ChatDatabase::class.java,
            "chat_database"
        ).build()
    }

    single { get<ChatDatabase>().chatMessageDao() }

    single<ChatRepository> { ChatRepositoryImpl(get(), get(), get()) }

    // API Key configuration
    single { 
        androidContext().resources.getString(R.string.deepseek_api_key)
    }

    // Use cases
    single { GetAllMessagesUseCase(get()) }
    single { InsertMessageUseCase(get()) }
    single { SendMessageUseCase(get()) }
    single { DeleteAllMessagesUseCase(get()) }
    single { DeleteMessageUseCase(get()) }

    viewModel {
        ChatViewModel(
            getAllMessagesUseCase = get(),
            insertMessageUseCase = get(),
            sendMessageUseCase = get(),
            deleteAllMessagesUseCase = get(),
            deleteMessageUseCase = get()
        )
    }
} 