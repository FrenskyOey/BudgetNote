---
name: repository_testing
description: Standardizes Repository testing patterns using fake repositories without mocking libraries. Use when creating tests for Repository implementations.
---

# Repository Testing Skill

Use this skill when creating tests for Repository implementations to ensure consistent fake repository patterns and avoid naming conflicts.

## When to Use
- Creating tests for Repository implementations
- Creating tests for UseCases that depend on Repositories
- Need fake repositories without mocking libraries

## Fake Repository Naming Convention

### Pattern
`Fake[Operation][Feature][Component]`

### Examples
- `FakeGetNewsDetailRepository`
- `FakeRefreshNewsDetailRepository`
- `FakeLoginRepository`
- `FakeGetNewsFeedRepository`

### Rules
- **One fake per test file** to avoid class name conflicts
- **Name after the operation** being tested (e.g., Get, Refresh, Login)
- **Never use generic names** like `FakeNewsDetailRepository` in multiple files

---

## CQS Repository Test Template

For Repositories using Command-Query Separation pattern (DB as source of truth):

### Query Operation Test (Observation)

```kotlin
// File: GetNewsDetailUseCaseTest.kt
package feature.news.domain.usecase.newsdetail

import feature.news.domain.model.NewsDetail
import feature.news.domain.repository.NewsDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import core.domain.model.Result

class GetNewsDetailUseCaseTest {

    private val fakeRepository = FakeGetNewsDetailRepository()
    private val useCase = GetNewsDetailUseCase(fakeRepository)

    @Test
    fun `invoke returns flow from repository`() = runTest {
        // Given
        val newsDetail = NewsDetail(...)
        fakeRepository.setData(newsDetail)

        // When
        val result = useCase(1).single()

        // Then
        assertEquals(newsDetail, result)
    }
}

class FakeGetNewsDetailRepository : NewsDetailRepository {
    private var data: NewsDetail? = null

    fun setData(newsDetail: NewsDetail?) {
        this.data = newsDetail
    }

    override fun getNewsDetail(id: Int): Flow<NewsDetail?> {
        return flowOf(data)
    }

    override suspend fun refreshNewsDetail(id: Int): Result<Unit> {
        return Result.Success(Unit) // Not tested in this fake
    }
}
```

### Command Operation Test (Action)

```kotlin
// File: RefreshNewsDetailUseCaseTest.kt
package feature.news.domain.usecase.newsdetail

import core.domain.model.AppException
import core.domain.model.Result
import feature.news.domain.model.NewsDetail
import feature.news.domain.repository.NewsDetailRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RefreshNewsDetailUseCaseTest {

    private val fakeRepository = FakeRefreshNewsDetailRepository()
    private val useCase = RefreshNewsDetailUseCase(fakeRepository)

    @Test
    fun `invoke calls repository refreshNewsDetail`() = runTest {
        // Given
        fakeRepository.setResult(Result.Success(Unit))

        // When
        val result = useCase(1)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(1, fakeRepository.lastRefreshedId)
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        // Given
        val error = Result.Error(AppException.NetworkError("Error"))
        fakeRepository.setResult(error)

        // When
        val result = useCase(1)

        // Then
        assertEquals(error, result)
    }
}

class FakeRefreshNewsDetailRepository : NewsDetailRepository {
    var lastRefreshedId: Int? = null
    private var result: Result<Unit> = Result.Success(Unit)

    fun setResult(newResult: Result<Unit>) {
        result = newResult
    }

    override fun getNewsDetail(id: Int): Flow<NewsDetail?> = emptyFlow()

    override suspend fun refreshNewsDetail(id: Int): Result<Unit> {
        lastRefreshedId = id
        return result
    }
}
```

---

## Standard Repository Test Template

For Repositories using standard pattern (direct API calls):

```kotlin
// File: LoginUseCaseTest.kt
package feature.auth.domain.usecase

import core.domain.model.AppException
import core.domain.model.Result
import feature.auth.domain.model.LoginModel
import feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LoginUseCaseTest {

    private val fakeRepository = FakeLoginRepository()
    private val useCase = LoginUseCase(fakeRepository)

    @Test
    fun `invoke returns success from repository`() = runTest {
        // Given
        val loginModel = LoginModel(...)
        fakeRepository.result = Result.Success(loginModel)

        // When
        val result = useCase("email@test.com", "password")

        // Then
        assertTrue(result is Result.Success)
        assertEquals(loginModel, (result as Result.Success).data)
    }

    @Test
    fun `invoke returns error from repository`() = runTest {
        // Given
        val error = Result.Error(AppException.NetworkError("Error"))
        fakeRepository.result = error

        // When
        val result = useCase("email@test.com", "password")

        // Then
        assertEquals(error, result)
    }
}

class FakeLoginRepository : AuthRepository {
    var result: Result<LoginModel> = Result.Success(LoginModel(...))

    override suspend fun login(email: String, password: String): Result<LoginModel> {
        return result
    }
}
```

---

## Repository Implementation Test Template

For testing Repository implementations (Data Layer):

```kotlin
// File: NewsDetailRepositoryImplTest.kt
package feature.news.data.repository

import core.domain.model.AppException
import core.domain.model.Result
import feature.news.data.datasource.NewsDetailDataSource
import feature.news.data.model.entity.ArticleDetailEntity
import feature.news.data.model.response.ArticleDetailResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class NewsDetailRepositoryImplTest {

    private lateinit var repository: NewsDetailRepositoryImpl
    private lateinit var fakeRemoteDataSource: FakeNewsDetailRemoteDataSource
    private lateinit var fakeLocalDataSource: FakeNewsDetailLocalDataSource

    @BeforeTest
    fun setup() {
        fakeRemoteDataSource = FakeNewsDetailRemoteDataSource()
        fakeLocalDataSource = FakeNewsDetailLocalDataSource()
        repository = NewsDetailRepositoryImpl(fakeRemoteDataSource, fakeLocalDataSource)
    }

    @Test
    fun `getNewsDetail emits local data`() = runTest {
        // Given
        val localData = createEntity(id = 1, title = "Local Title")
        fakeLocalDataSource.flow.value = localData

        // When
        val result = repository.getNewsDetail(1).first()

        // Then
        assertEquals("Local Title", result?.title)
    }

    @Test
    fun `refreshNewsDetail fetches remote and updates local`() = runTest {
        // Given
        val remoteData = createResponse(id = 1, title = "Remote Title")
        fakeRemoteDataSource.response = remoteData

        // When
        val result = repository.refreshNewsDetail(1)

        // Then
        assertIs<Result.Success<*>>(result)
        assertEquals(remoteData.toEntity(), fakeLocalDataSource.storage[1])
    }

    @Test
    fun `refreshNewsDetail returns error when remote fails`() = runTest {
        // Given
        fakeRemoteDataSource.exception = AppException.NetworkError("No connection")

        // When
        val result = repository.refreshNewsDetail(1)

        // Then
        assertIs<Result.Error>(result)
        assertIs<AppException.NetworkError>(result.exception)
    }

    // Fake Data Sources
    class FakeNewsDetailRemoteDataSource : NewsDetailDataSource.Remote {
        var response: ArticleDetailResponse? = null
        var exception: Exception? = null

        override suspend fun getNewsDetail(id: Int): ArticleDetailResponse {
            exception?.let { throw it }
            return response!!
        }
    }

    class FakeNewsDetailLocalDataSource : NewsDetailDataSource.Local {
        val storage = mutableMapOf<Int, ArticleDetailEntity>()
        val flow = MutableStateFlow<ArticleDetailEntity?>(null)

        override fun getNewsDetail(id: Int): Flow<ArticleDetailEntity?> {
            return flow
        }

        override suspend fun upsertNewsDetail(article: ArticleDetailEntity) {
            storage[article.id] = article
            flow.value = article
        }
    }

    // Helper functions
    private fun createEntity(id: Int, title: String) = ArticleDetailEntity(...)
    private fun createResponse(id: Int, title: String) = ArticleDetailResponse(...)
}
```

---

## Key Principles

1. **No Mocking Libraries**: Use simple fake implementations
2. **One Fake Per Test File**: Prevents naming conflicts
3. **Operation-Specific Names**: Makes purpose clear
4. **Minimal Implementation**: Only implement what's needed for the test
5. **Mutable State**: Use `var` for test data setup
6. **Clear Test Data**: Use helper functions for test data creation
