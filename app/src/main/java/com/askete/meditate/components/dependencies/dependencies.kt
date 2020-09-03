package com.askete.meditate.components.dependencies

import android.content.Context
import com.askete.meditate.components.factory.ViewModelFactory
import com.askete.meditate.components.mvvm.viewmodel.ViewModelProvider
import com.askete.meditate.tools.database.AppDatabase
import com.askete.meditate.tools.database.repository.DatabaseRepository
import com.askete.meditate.tools.database.repository.DatabaseRepositoryImpl
import com.askete.meditate.tools.prefs.PreferenceProvider
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

val prefs: PreferenceProvider
    get() = safeDependency { Dependencies.preferenceProvider }

val database: AppDatabase
    get() = safeDependency { Dependencies.database }

val repository: DatabaseRepository
    get() = safeDependency { Dependencies.databaseRepository }


fun initDependencies(onContext: () -> Context) {
    Dependencies.context = onContext()

    ViewModelProvider.set(ViewModelFactory(
        Dependencies.databaseRepository
    ))
}

private object Dependencies {
    lateinit var context: Context

    var preferenceProviderSingleton: PreferenceProvider? = null

    var databaseSingleton: AppDatabase? = null

    var databaseRepositorySingleton: DatabaseRepository? = null

    val preferenceProvider: PreferenceProvider
        get() = preferenceProviderSingleton ?: PreferenceProvider(context)
            .also { preferenceProviderSingleton = it }

    val database: AppDatabase
        get() = databaseSingleton ?: AppDatabase.getAppDataBase(context)
            .also { databaseSingleton = it }

    val databaseRepository: DatabaseRepository
        get() = databaseRepositorySingleton ?: DatabaseRepositoryImpl(
            database.dao
        ).also { databaseRepositorySingleton = it }

    val isContextInitialized: Boolean
        get() = ::context.isInitialized



}

fun requireContextFromDependencies() =
    if (Dependencies.isContextInitialized) Dependencies.context
    else throw NullPointerException("Context is null")

private fun <T> safeDependency(onTry: () -> T): T {
    if (!Dependencies.isContextInitialized) {
        throw IllegalArgumentException("Init dependencies in your App class using: initDependencies { this }")
    } else return onTry()

}
