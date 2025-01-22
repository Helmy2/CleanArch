# Clean Architecture with MVI Demo

This project is a demonstration of**Clean Architecture**combined with the**MVI (Model-View-Intent)**
pattern in an Android application. It showcases separation of concerns, modularity, and best
practices for building scalable and maintainable apps.

---

## Features

- **Clean Architecture Layers**:
    - **Data Layer**: Simulated local and remote data sources.
    - **Domain Layer**: Use cases, entities, and business logic.
    - **Presentation Layer**: Jetpack Compose UI with MVI pattern.

- **MVI Pattern**:
    - **Model**:`HomeState`represents the UI state.
    - **View**:`HomeRoute`observes the state and renders the UI.
    - **Intent**:`HomeEvent`represents user actions.

- **Navigation**:
    - Type-safe navigation using a sealed class (`AppDestination`).
    - Jetpack Compose Navigation for seamless screen transitions.

- **Dependency Injection**:
    - Koin for managing dependencies.

- **Error Handling**:
    - Snackbar for displaying errors.
    - `DomainResult`sealed class for handling success, failure, and loading states.

---

## Project Structure

The project is organized into the following modules:

### 1.**Data Layer**

- **Local Manager**: Simulates local data storage (e.g., caching).
- **Remote Manager**: Simulates remote data fetching (e.g., network calls).
- **Repository**: Combines local and remote data sources (`UserRepositoryImpl`).

### 2.**Domain Layer**

- **Entities**: Data models (e.g.,`User`).
- **Use Cases**: Business logic (e.g.,`GetUserUseCase`).
- **Exceptions**: Custom exceptions (e.g.,`UserNotFoundException`).

### 3.**Presentation Layer**

- **UI Components**: Jetpack Compose screens (`HomeRoute`,`DetailsRoute`).
- **State Management**:`HomeViewModel`with`StateFlow`for state management.
- **Navigation**: Type-safe navigation using`AppDestination`and`Navigator`.

---

## Code Examples

### Data Layer

#### `UserRepositoryImpl.kt`

```kotlin
class UserRepositoryImpl(
    private val remoteManager: RemoteUserManager,
    private val localManager: LocalUserManager
) : UserRepository {
    override fun getUser(userId: Int): Flow<User?> {
        return flow {
            val localUser = localManager.getUser(userId)
            if (localUser != null) {
                emit(localUser)
            } else {
                val remoteUser = remoteManager.fetchUser(userId)
                if (remoteUser != null) {
                    localManager.saveUser(remoteUser)
                }
                emit(remoteUser)
            }
        }.onStart { emit(null) } // Emit null to show loading state
    }
}
```

### Domain Layer

#### `GetUserUseCase.kt`

```kotlin
class GetUserUseCase(private val userRepository: UserRepository) {
    operator fun invoke(userId: Int): Flow<DomainResult<User>> {
        return userRepository.getUser(userId).map { userData ->
            if (userData != null) {
                DomainResult.Success(userData)
            } else {
                DomainResult.Failure(UserNotFoundException(userId))
            }
        }.onStart { emit(DomainResult.Loading) }.catch { e ->
            emit(DomainResult.Failure(e))
        }
    }
}
```

### Presentation Layer

#### `HomeViewModel.kt`

```kotlin
class HomeViewModel(
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager,
    private val getUserUseCase: GetUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.onStart { loadUserData(0) }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000), HomeState()
    )

    private fun loadUserData(id: Int) {
        viewModelScope.launch {
            getUserUseCase(id).catch { error ->
                _state.update { it.copy(isLoading = false) }
                snackbarManager.showSnackbar(error.localizedMessage ?: "Unknown error")
            }.collect { result ->
                when (result) {
                    DomainResult.Loading -> _state.update { it.copy(isLoading = true) }
                    is DomainResult.Success -> _state.update {
                        it.copy(
                            isLoading = false,
                            user = result.data
                        )
                    }
                    is DomainResult.Failure -> {
                        _state.update { it.copy(isLoading = false) }
                        snackbarManager.showSnackbar(
                            result.exception.localizedMessage ?: "Unknown error"
                        )
                    }
                }
            }
        }
    }

    fun handleEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.NavigateToDetails -> navigator.navigateToDetails(
                _state.value.user?.id ?: -1
            )
            is HomeEvent.LoadUser -> loadUserData(event.id)
        }
    }
}
```

---

## Setup

1. Clone the repository:
    ```bash    
    git clone https://github.com/Helmy2/CleanArch.git
    ```
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

---

## Dependencies

- [Jetpack Compose](https://developer.android.com/jetpack/compose): Modern UI toolkit.
- [Koin](https://insert-koin.io/): Dependency injection framework.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): Asynchronous
  programming.
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html): Reactive streams.

---

## Future Plans

- Add unit and UI tests for all components.
- Integrate a real data source (e.g., REST API or local database).
- Explore Kotlin Multiplatform for cross-platform support.