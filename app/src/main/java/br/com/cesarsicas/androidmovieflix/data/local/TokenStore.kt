package br.com.cesarsicas.androidmovieflix.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private object Keys {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val ADMIN_TOKEN = stringPreferencesKey("admin_token")
    }

    val userToken: Flow<String?> = dataStore.data.map { it[Keys.USER_TOKEN] }
    val adminToken: Flow<String?> = dataStore.data.map { it[Keys.ADMIN_TOKEN] }

    suspend fun saveUserToken(token: String) = dataStore.edit { it[Keys.USER_TOKEN] = token }
    suspend fun clearUserToken() = dataStore.edit { it.remove(Keys.USER_TOKEN) }

    suspend fun saveAdminToken(token: String) = dataStore.edit { it[Keys.ADMIN_TOKEN] = token }
    suspend fun clearAdminToken() = dataStore.edit { it.remove(Keys.ADMIN_TOKEN) }
}
