# Clean Architecture with Jetpack Compose and Koin
This project demonstrates **Clean Architecture** combined with **Jetpack Compose**, **Koin** for dependency injection and **MVI** pattern. It showcases separation of concerns, modularity, and best practices for building scalable and maintainable Android apps.

---
## Features
- **Clean Architecture Layers**:
  - **Data Layer**: Handles local (Room) and remote (Firebase Auth) data sources.
  - **Domain Layer**: Contains entities and repository interfaces (in the`:domain`module).
  - **Presentation Layer**: Jetpack Compose UI with MVI pattern.
- **Modular Structure**:
  - **Core**: Shared utilities, theme, and navigation.
  - **Data**: Room database and Firebase Auth integration.
  - **Domain**: Entities and repository interfaces.
  - **Feature Modules**:`auth`,`home`, and`profile`for specific app features, each containing its  
    own**use cases**in a`domain`package.
  - **MVI Pattern**:
    - Model: ScreenState represents the UI state.
    - View: ScreenRoute observes the state and renders the UI.
    - Intent: ScreenEvent represents user actions.
- **Navigation**:
  - Jetpack Compose Navigation for seamless screen transitions.
  - Type-safe navigation using a sealed class (`AppDestination`).
  - Centralized navigation using a`Navigator`interface.
- **Dependency Injection**:
  - Koin for managing dependencies across modules.
- **Error Handling**:
  - Snackbar for displaying errors.
  - `Resource`sealed class for handling success, failure, and loading states.
  - `Result` for handling success and failure.
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
Each feature module contains its own **presentation**, **domain**, and **navigation** logic.
#### **a. Auth Module (`:feature-auth`)**
- **Presentation**:
  - `AuthScreen`: Jetpack Compose UI for authentication.
  - `AuthViewModel`: Manages state and logic for authentication.
  - UI Components: Reusable components like `AuthButton`, `AuthTextField`, etc.
- **Domain**:
  - Use Cases: `LoginUseCase`, `RegisterUseCase`, `SignInAnonymouslyUseCase`.
- **Navigation**: `AuthNavigation` for feature-specific navigation.
#### **b. Home Module (`:feature-home`)**
- **Presentation**:
  - `HomeScreen`: Jetpack Compose UI for the home screen.
  - `HomeViewModel`: Manages state and logic for the home screen.
- **Domain**:
  - Use Cases: `GetUserUseCase`.
- **Navigation**: `HomeNavigation` for feature-specific navigation.

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
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html): Asynchronous programming.
- [Kotlin Flow](https://kotlinlang.org/docs/flow.html): Reactive streams.

---

## License

This project is licensed under the MIT License. See the [LICENSE](https://chat.deepseek.com/a/chat/s/LICENSE) file for details.