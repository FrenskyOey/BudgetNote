---
name: UseCase Implementation Strategy
description: Standards for implementing Domain UseCases, enforcing feature-based package structure derived from Repository names.
---

# UseCase Implementation Strategy

## Objective
Enforce a consistent folder structure for UseCases in the Domain Layer by grouping them into feature-specific packages derived from the Repository they reference.

## Trigger
This skill should be triggered when:
- You are implementing a new UseCase.
- You are refactoring existing UseCases.
- You are planning the Domain Layer structure.
- You encounter a request to "implement usecase" or "add usecase".

## Rules

### 1. Single Repository Dependency
**Rule**: If a UseCase interacts with **only one** Repository, you MUST place it in a sub-package derived from that Repository's name.
**Process**:
1.  Identify the single Repository (e.g., `NewsDetailRepository`).
2.  Extract the feature name (e.g., `newsdetail`).
3.  Create/Use sub-package: `domain/usecase/newsdetail/`.
4.  Place UseCase and Test there.

### 2. Multiple Repository Dependencies
**Rule**: If a UseCase interacts with **two or more** Repositories (e.g., `NewsRepository` and `UserRepository`), you MUST place it directly in the `domain/usecase/` package (root of usecases), OR a `common`/`shared` sub-package if one exists.
**Process**:
1.  Identify that multiple Repositories are used.
2.  Do NOT create a specific feature sub-package based on just one of them.
3.  Place the UseCase file directly in `domain/usecase/`.
4.  Example: `domain/usecase/SyncUserDataUseCase.kt` (uses `UserRepository` and `SettingsRepository`).

### 3. Example Structure

**Scenario A (Single Repo)**:
- `GetNewsDetailUseCase` uses `NewsDetailRepository`.
- Result: `domain/usecase/newsdetail/GetNewsDetailUseCase.kt`

**Scenario B (Multiple Repos)**:
- `RefreshNewsAndProfileUseCase` uses `NewsRepository` and `ProfileRepository`.
- Result: `domain/usecase/RefreshNewsAndProfileUseCase.kt`

### 4. Verification
- [ ] Check if the UseCase is in a sub-package of `usecase`.
- [ ] Check if the sub-package name is derived from the Repository name.
- [ ] Check if the Test file is in the same relative path in `commonTest`.
