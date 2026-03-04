package feature.onboarding.data.datasource.local

import com.russhwolf.settings.Settings
import feature.onboarding.data.model.entity.UserEntity
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

// Fake implementation of Settings interface
class FakeSettings : Settings {
    private val map = mutableMapOf<String, Any>()
    
    override val keys: Set<String> get() = map.keys
    override val size: Int get() = map.size
    
    override fun clear() { map.clear() }
    override fun remove(key: String) { map.remove(key) }
    override fun hasKey(key: String): Boolean = map.containsKey(key)
    
    override fun putInt(key: String, value: Int) { map[key] = value }
    override fun getInt(key: String, defaultValue: Int): Int = map[key] as? Int ?: defaultValue
    override fun getIntOrNull(key: String): Int? = map[key] as? Int
    
    override fun putLong(key: String, value: Long) { map[key] = value }
    override fun getLong(key: String, defaultValue: Long): Long = map[key] as? Long ?: defaultValue
    override fun getLongOrNull(key: String): Long? = map[key] as? Long
    
    override fun putString(key: String, value: String) { map[key] = value }
    override fun getString(key: String, defaultValue: String): String = map[key] as? String ?: defaultValue
    override fun getStringOrNull(key: String): String? = map[key] as? String
    
    override fun putFloat(key: String, value: Float) { map[key] = value }
    override fun getFloat(key: String, defaultValue: Float): Float = map[key] as? Float ?: defaultValue
    override fun getFloatOrNull(key: String): Float? = map[key] as? Float
    
    override fun putDouble(key: String, value: Double) { map[key] = value }
    override fun getDouble(key: String, defaultValue: Double): Double = map[key] as? Double ?: defaultValue
    override fun getDoubleOrNull(key: String): Double? = map[key] as? Double
    
    override fun putBoolean(key: String, value: Boolean) { map[key] = value }
    override fun getBoolean(key: String, defaultValue: Boolean): Boolean = map[key] as? Boolean ?: defaultValue
    override fun getBooleanOrNull(key: String): Boolean? = map[key] as? Boolean
}

class AuthLocalDataSourceImplTest {

    @Test
    fun `saveUser saves user data and token`() = runTest {
        val settings = FakeSettings()
        val dataSource = AuthLocalDataSourceImpl(settings)
        val user = UserEntity(123, "test@example.com", "valid_token")

        dataSource.saveUser(user)

        assertEquals("valid_token", settings.getStringOrNull(AuthLocalDataSourceImpl.KEY_USER_TOKEN))
        // Verify JSON string is saved
        assertEquals(true, settings.hasKey(AuthLocalDataSourceImpl.KEY_USER_DATA))
    }

    @Test
    fun `getUser returns saved user`() = runTest {
        val settings = FakeSettings()
        val dataSource = AuthLocalDataSourceImpl(settings)
        val user = UserEntity(123, "test@example.com", "valid_token")

        dataSource.saveUser(user)
        val retrievedUser = dataSource.getUser()

        assertEquals(user, retrievedUser)
    }

    @Test
    fun `getToken returns saved token`() = runTest {
        val settings = FakeSettings()
        val dataSource = AuthLocalDataSourceImpl(settings)
        settings.putString(AuthLocalDataSourceImpl.KEY_USER_TOKEN, "existing_token")

        val token = dataSource.getToken()

        assertEquals("existing_token", token)
    }

    @Test
    fun `clearUser removes all data`() = runTest {
        val settings = FakeSettings()
        val dataSource = AuthLocalDataSourceImpl(settings)
        val user = UserEntity(123, "test@example.com", "valid_token")
        dataSource.saveUser(user)
        dataSource.saveRefreshToken("refresh_token")

        dataSource.clearUser()

        assertNull(dataSource.getUser())
        assertNull(dataSource.getToken())
        assertNull(dataSource.getRefreshToken())
        assertEquals(false, settings.hasKey(AuthLocalDataSourceImpl.KEY_USER_DATA))
        assertEquals(false, settings.hasKey(AuthLocalDataSourceImpl.KEY_USER_TOKEN))
        assertEquals(false, settings.hasKey(AuthLocalDataSourceImpl.KEY_USER_REFRESH_TOKEN))
    }

    @Test
    fun `getRefreshToken returns saved token`() = runTest {
        val settings = FakeSettings()
        val dataSource = AuthLocalDataSourceImpl(settings)
        dataSource.saveRefreshToken("refresh_token_123")

        val token = dataSource.getRefreshToken()

        assertEquals("refresh_token_123", token)
    }

    @Test
    fun `clearRefreshToken removes refresh token`() = runTest {
        val settings = FakeSettings()
        val dataSource = AuthLocalDataSourceImpl(settings)
        dataSource.saveRefreshToken("refresh_token_123")

        dataSource.clearRefreshToken()

        assertNull(dataSource.getRefreshToken())
        assertEquals(false, settings.hasKey(AuthLocalDataSourceImpl.KEY_USER_REFRESH_TOKEN))
    }
}
