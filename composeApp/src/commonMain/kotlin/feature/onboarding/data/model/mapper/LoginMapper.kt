package feature.onboarding.data.model.mapper

import feature.onboarding.data.model.entity.UserEntity
import feature.onboarding.data.model.response.UserData
import feature.onboarding.domain.model.User

// UserData (API) -> User (Domain)
fun UserData.toDomain(): User {
    return User(
        userId = this.userId,
        userName = this.userName,
        token = this.token
    )
}

// User (Domain) -> UserEntity (Storage)
fun User.toEntity(): UserEntity {
    return UserEntity(
        userId = this.userId,
        userName = this.userName,
        token = this.token
    )
}

// UserEntity (Storage) -> User (Domain)
fun UserEntity.toDomain(): User {
    return User(
        userId = this.userId,
        userName = this.userName,
        token = this.token
    )
}
