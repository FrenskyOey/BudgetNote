---
trigger: always_on
description: Security best practices for Kotlin Multiplatform mobile development
---

# Security Rules

## Sensitive Data Storage

### Never Hardcode Secrets

```kotlin
// WRONG - Hardcoded secrets
object ApiConfig {
    const val API_KEY = "sk-123456789"
    const val SECRET_TOKEN = "my_secret_token"
}

// CORRECT - Use BuildConfig or secure storage
expect fun getApiKey(): String
expect fun getSecretToken(): String
```

### Use Encrypted Storage

```kotlin
// WRONG - Plain SharedPreferences for sensitive data
fun saveToken(token: String) {
    sharedPrefs.edit().putString("auth_token", token).apply()
}

// CORRECT - Use platform-specific encrypted storage
expect class SecureStorage {
    suspend fun saveSecurely(key: String, value: String)
    suspend fun getSecurely(key: String): String?
    suspend fun delete(key: String)
}

// Android: EncryptedSharedPreferences
// iOS: Keychain Services
```

### Never Log Sensitive Data

```kotlin
// WRONG - Logging sensitive information
fun login(email: String, password: String) {
    Logger.d("Login attempt: email=$email, password=$password")
}

// CORRECT - Sanitize logs
fun login(email: String, password: String) {
    Logger.d("Login attempt: email=${email.take(3)}***")
}
```

---

## Network Security

### Always Use HTTPS

```kotlin
// WRONG - HTTP endpoints
const val BASE_URL = "http://api.example.com"

// CORRECT - HTTPS only
const val BASE_URL = "https://api.example.com"
```

### Request/Response Validation

```kotlin
// CORRECT - Validate API responses
suspend fun parseResponse(response: HttpResponse): Result<Data> {
    return try {
        val data = response.body<DataDto>()
        if (data.isValid()) {
            Result.Success(data.toDomain())
        } else {
            Result.Error(AppException.ValidationError("Invalid data"))
        }
    } catch (e: Exception) {
        Result.Error(ApiErrorHandler.handleError(e))
    }
}
```

---

## Authentication & Authorization

### Secure Token Management

```kotlin
// CORRECT - Token refresh with secure storage
class AuthTokenManager(
    private val secureStorage: SecureStorage,
    private val authApi: AuthApi
) {
    private var accessToken: String? = null
    
    suspend fun getValidToken(): String? {
        // Check expiry and refresh if needed
        val token = accessToken ?: secureStorage.getSecurely(ACCESS_TOKEN_KEY)
        return if (token.isExpired()) {
            refreshToken()
        } else {
            token
        }
    }
    
    suspend fun clearTokens() {
        accessToken = null
        secureStorage.delete(ACCESS_TOKEN_KEY)
        secureStorage.delete(REFRESH_TOKEN_KEY)
    }
}
```

### Session Timeout

```kotlin
// CORRECT - Implement session timeout
class SessionManager {
    private var lastActivityTime: Long = 0
    private val sessionTimeout = 15 * 60 * 1000L // 15 minutes
    
    fun updateActivity() {
        lastActivityTime = getCurrentTimeMillis()
    }
    
    fun isSessionValid(): Boolean {
        return getCurrentTimeMillis() - lastActivityTime < sessionTimeout
    }
}
```

---

## Input Validation

### Sanitize All User Input

```kotlin
// WRONG - Direct use of user input
fun search(query: String) {
    database.rawQuery("SELECT * FROM items WHERE name = '$query'")
}

// CORRECT - Parameterized queries
fun search(query: String) {
    database.query(
        "SELECT * FROM items WHERE name = ?",
        arrayOf(query.sanitize())
    )
}

// CORRECT - Input sanitization helper
fun String.sanitize(): String {
    return this
        .trim()
        .replace(Regex("[<>\"']"), "")
        .take(MAX_INPUT_LENGTH)
}
```

### Validate File Paths

```kotlin
// WRONG - Unvalidated file paths (path traversal vulnerability)
fun readFile(filename: String): ByteArray {
    return File(baseDir, filename).readBytes()
}

// CORRECT - Validate and normalize paths
fun readFile(filename: String): ByteArray? {
    val sanitizedName = filename.replace(Regex("[/\\\\]"), "")
    val file = File(baseDir, sanitizedName)
    return if (file.canonicalPath.startsWith(baseDir.canonicalPath)) {
        file.readBytes()
    } else {
        null // Attempted path traversal
    }
}
```

---

## Data Protection

### Clear Sensitive Data from Memory

```kotlin
// CORRECT - Clear sensitive data after use
fun processPassword(password: CharArray) {
    try {
        // Use password
        authenticator.authenticate(password)
    } finally {
        // Clear from memory
        password.fill('\u0000')
    }
}
```

### Obfuscation & Build Security

```kotlin
// CORRECT - Enable ProGuard/R8 for release builds
// build.gradle.kts
android {
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

---

## Biometric Authentication

### Secure Biometric Implementation

```kotlin
// CORRECT - Platform-specific biometric authentication
expect class BiometricAuthenticator {
    suspend fun authenticate(): BiometricResult
    fun canAuthenticate(): Boolean
}

sealed class BiometricResult {
    object Success : BiometricResult()
    data class Error(val message: String) : BiometricResult()
    object Cancelled : BiometricResult()
}
```

---

## Deep Link Security

### Validate Deep Link Parameters

```kotlin
// WRONG - Unvalidated deep link handling
fun handleDeepLink(uri: Uri) {
    val userId = uri.getQueryParameter("userId")
    navigateToProfile(userId!!)
}

// CORRECT - Validate deep link parameters
fun handleDeepLink(uri: Uri): Boolean {
    val host = uri.host ?: return false
    if (!ALLOWED_HOSTS.contains(host)) return false
    
    val userId = uri.getQueryParameter("userId")?.takeIf { 
        it.matches(Regex("^[a-zA-Z0-9]{8,36}$")) 
    } ?: return false
    
    navigateToProfile(userId)
    return true
}
```

---

## Clipboard Security

### Handle Clipboard Data Carefully

```kotlin
// WRONG - Sensitive data in clipboard indefinitely
fun copyApiKey(key: String) {
    clipboardManager.setPrimaryClip(ClipData.newPlainText("API Key", key))
}

// CORRECT - Clear clipboard after timeout or use sensitive flag
fun copyApiKey(key: String) {
    val clip = ClipData.newPlainText("API Key", key).apply {
        description.extras = PersistableBundle().apply {
            putBoolean("android.content.extra.IS_SENSITIVE", true)
        }
    }
    clipboardManager.setPrimaryClip(clip)
    
    // Schedule clipboard clear
    handler.postDelayed({ clearClipboard() }, CLIPBOARD_TIMEOUT)
}
```

---

## Debug Protection

### Disable Debug Features in Release

```kotlin
// CORRECT - Debug detection
expect fun isDebuggerAttached(): Boolean
expect fun isRunningInEmulator(): Boolean

fun checkSecurityEnvironment(): SecurityStatus {
    if (isDebuggerAttached()) {
        return SecurityStatus.DebuggerDetected
    }
    if (isRunningInEmulator() && !BuildConfig.ALLOW_EMULATOR) {
        return SecurityStatus.EmulatorDetected
    }
    return SecurityStatus.Secure
}
```

---

# Security Checklist

Before accepting code, verify:

### Data Protection:
- [ ] No hardcoded secrets, API keys, or credentials
- [ ] Sensitive data stored in encrypted storage (Keychain/EncryptedSharedPreferences)
- [ ] Sensitive data cleared from memory after use
- [ ] No sensitive data in logs

### Network:
- [ ] HTTPS used for all network calls
- [ ] Certificate pinning for sensitive APIs
- [ ] API responses validated before use

### Authentication:
- [ ] Tokens stored securely
- [ ] Session timeout implemented
- [ ] Proper token refresh mechanism

### Input Handling:
- [ ] All user input sanitized
- [ ] SQL injection prevented (parameterized queries)
- [ ] Path traversal attacks prevented
- [ ] Deep link parameters validated

### Build Security:
- [ ] ProGuard/R8 enabled for release
- [ ] Debug features disabled in release
- [ ] No test credentials in production builds