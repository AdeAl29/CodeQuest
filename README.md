# CodeQuest

**CodeQuest** is a gamified, adaptive coding education Android application built with **Kotlin** and **Jetpack Compose**. It runs 100% offline using an internal curriculum dataset, **Room** for learning progress and quiz history persistence, and **Jetpack DataStore** for user profile and gamification state.

---

## Key Features

- **Personalized Onboarding**: Configure your adventurer name, learning goal (*Learn from Zero*, *College Prep*, *Logic Practice*, or *Kotlin Basics*), and starting proficiency level (*Beginner*, *Intermediate*, *Advanced*).
- **Adaptive Learning Engine**: Dynamically calculates a real-time **Mastery Score** and recommends the next module, remedial topic review, and quiz difficulty (*Easy*, *Medium*, *Hard*):
  $$\text{masteryScore} = (\text{quizScore} \times 0.5) + (\text{accuracy} \times 0.3) + (\text{streakBonus} \times 0.1) + (\text{completionRate} \times 0.1)$$
- **Structured Learning Path**: 8 foundational programming & Kotlin modules (*Algorithms*, *Variables & Data Types*, *Operators*, *If-Else Branching*, *Loops*, *Functions*, *Lists/Arrays*, and *Object-Oriented Programming*) with progressive unlocking (`LOCKED`, `AVAILABLE`, `IN_PROGRESS`, `COMPLETED`).
- **Interactive Lesson & Code Playground**: Concise concept breakdowns, highlighted key takeaways, and a macOS/iOS-inspired Kotlin code viewer featuring syntax highlighting, line numbers, one-tap copy, and simulated console output execution.
- **Adaptive Quiz & Challenge Room**: 40 curated questions (5 per module across *Easy*, *Medium*, and *Hard* difficulties) supporting Multiple Choice, True/False, and Complete-the-Code formats with instant explanations and retry support.
- **Gamification & Achievements**: Earn XP from lessons and quizzes, level up every 250 XP, maintain daily study streaks, and unlock 8 achievement badges.
- **Full Profile Management**: View detailed learning statistics, recent quiz attempt history, active badges, edit profile preferences, or reset learning progress at any time.
- **iOS Liquid Glass UI/UX**: Modern edge-to-edge Material 3 interface featuring a floating iOS-style frosted glass bottom navigation dock with fluid spring-animated pill indicators, specular highlights, ambient aura glow, and haptic micro-interactions.

---

## Tech Stack & Architecture

- **Language**: Kotlin (JVM Target 17)
- **UI Framework**: Jetpack Compose (Material 3, Custom iOS Liquid Glass Components, Edge-to-Edge)
- **Architecture**: Clean MVVM (Data, Domain, Presentation layers)
- **Navigation**: Jetpack Navigation Compose with custom animated transitions
- **Local Persistence**:
  - **Room Database**: Stores module progress, quiz attempt logs, and unlocked badges
  - **Preferences DataStore**: Stores user profile, XP, level, and daily streak state

```text
app/src/main/java/com/codequest
|-- data
|   |-- local          # Room database, DAO, entities, DataStore, curriculum & quiz bank
|   |-- model          # Domain and UI models
|   `-- repository     # LearningRepository (single source of truth)
|-- domain
|   |-- algorithm      # AdaptiveLearningEngine (mastery formula & difficulty routing)
|   `-- usecase        # Dashboard observation and quiz submission use cases
|-- presentation
|   |-- component      # Reusable Compose UI components & syntax-highlighted CodeBlock
|   |-- navigation     # Navigation graph & iOS Liquid Glass bottom dock
|   |-- screen         # Onboarding, Dashboard, Learn, Challenge, Lesson, Quiz, Achievement, Profile
|   `-- viewmodel      # StateFlow-driven ViewModels per screen
`-- ui
    `-- theme          # Material 3 Light/Dark color schemes & typography
```

---

## Building & Running

### Prerequisites

- **JDK 17** or newer (e.g., Eclipse Temurin 17 or Android Studio bundled JBR)
- **Android SDK** (Compile SDK 36, Min SDK 26)

### Build Debug APK via CLI

```powershell
.\gradlew.bat :app:assembleDebug
```

The compiled debug APK will be generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

You can also open the root `CodeQuest` directory directly in **Android Studio** and run the `app` configuration on an emulator or physical device.
