package core.domain.model

data class PaginatedData<T>(
    val items: List<T>,
    val currentPage: Int,
    val totalPages: Int,
    val hasNextPage: Boolean
)

data class PageRequest(
    val page: Int = 1,
    val pageSize: Int = 20
)
