# Clean Architecture with Jetpack Compose and Koin

This project demonstrates **Clean Architecture** combined with **Jetpack Compose**, **Koin** for
dependency injection and **MVI** pattern. It showcases separation of concerns, modularity, and best
practices for building scalable and maintainable Android apps.

---

## Best Practices Highlighted

This project emphasizes the following best practices for building robust and maintainable Android
applications:

#### Separation of Concerns:

Clear separation between data, domain, and presentation layers. Feature modules focus solely on UI
and navigation, while business logic resides in the `domain`
module. This promotes modularity, testability, and maintainability.

#### Centralized Business Logic:

Use cases in the `domain` module ensure consistency and reusability. This prevents code duplication
and makes it easier to manage and update business rules.

#### Reactive Programming:

Uses Kotlin Flow and Coroutines for asynchronous operations, enabling
efficient handling of data streams and UI updates. This improves responsiveness and user
experience.

#### Type-Safe Navigation:

Sealed classes for navigation ensure compile-time safety, preventing
runtime errors related to incorrect navigation destinations.

#### Error Handling:

Uses a `Result` and `Resource` sealed class to handle success, failure, and loading states
uniformly, providing a consistent way to manage and display errors to the user.

#### Dependency Injection:

Koin is used for dependency injection, promoting loose coupling and
making the code more testable and maintainable. This allows for easy swapping of implementations
and simplifies unit testing.

#### Single Source of Truth:

The `domain` layer acts as the single source of truth for business
logic and data models (entities). This ensures consistency and prevents data inconsistencies
across the application.

#### Testability:

The architecture is designed for testability, with clear separation of concerns
and the use of dependency injection. This makes it easier to write unit tests for individual
components and integration tests for different layers.

#### Modularization:

The project is modularized into feature modules, allowing for independent
development, testing, and deployment of features. This improves build times and reduces the impact
of changes on other parts of the application.

#### MVI (Model-View-Intent) Pattern:

The presentation layer utilizes the MVI pattern,
promoting
unidirectional data flow and making it easier to manage UI state and handle user interactions.
This leads to more predictable and maintainable UI code.

---

## Project Structure

The project is organized into the following modules:

### 1. **App Module (`:app`)**

- **Entry Point**: Contains the `MainActivity` and `MainNavigation` for the app.
- **Dependency Injection**: Aggregates all Koin modules (`AppModule.kt`).
- **Navigation**: Manages navigation between feature modules using Jetpack Compose Navigation.

### 2. **Core Module (`:core`)**

- **Theme**: App-wide theme (colors, typography, shapes).
- **Navigation**: `Navigator` interface and implementation (`NavigatorImpl.kt`).
- **Utilities**: Shared utilities like `SnackbarManager` and extensions.
- **Dependency Injection**: `CoreModule.kt` for shared dependencies.

### 3. **Data Module (`:data`)**

- **Local**: Room database setup and DAOs (`LocalAuthManager`, `LocalAuthManagerImpl`).
- **Remote**: Firebase Auth integration (`RemoteAuthManager`, `RemoteAuthManagerImpl`).
- **Repository**: Combines local and remote data sources (`AuthRepositoryImpl`).
- **Dependency Injection**: `DataModule.kt` for data-layer dependencies.

### 4. **Domain Module (`:domain`)**

- **Entities**: Data models (e.g., `User`, `Resource`).
- **Repository Interfaces**: Contracts for data access (e.g., `AuthRepository`).
- **Exceptions**: Custom exceptions (e.g., `AuthException`).

### 5. **Feature Modules**

Each feature module contains its own **presentation** and **navigation** logic, and depends on the *
*`:domain`** module for use cases and entities.

#### **a. Auth Module (`:feature-auth`)**

- **Presentation**:
    - `AuthScreen`: Jetpack Compose UI for authentication.
    - `AuthContract`: Contract for communication between the UI and the ViewModel.
    - `AuthViewModel`: Manages state and logic for authentication.
    - UI Components: Reusable components like `AuthButton`, `AuthTextField`, etc.
- **AuthRoute**: Defines navigation routes for the feature.
- **Dependency Injection**: `AuthModule.kt` for feature-specific dependencies.

---

## Modular Dependency Flow

```
:app → :feature-auth → :data → :domain → :core
       :feature-home → 
```

---

## Setup

1. Clone the repository:
    ```bash
    git clone https://github.com/Helmy2/CleanArch.git
    ```
2. Open the project in Android Studio.
3. Add your `google-services.json` file to the `app` module for Firebase integration.
4. Build and run the app on an emulator or physical device.

---

## Dependencies

- [Jetpack Compose](https://developer.android.com/jetpack/compose): Modern UI toolkit.
- [Koin](https://insert-koin.io/): Dependency injection framework.
- [Firebase Auth](https://firebase.google.com/docs/auth): Authentication service.
- [Room](https://developer.android.com/training/data-storage/room): Local database.
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): Asynchronous
  programming.
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html): Reactive streams.