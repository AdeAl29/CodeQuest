# CodeQuest

CodeQuest adalah prototype aplikasi Android pembelajaran coding dasar berbasis gamifikasi dan adaptive learning. Aplikasi berjalan lokal tanpa backend/Firebase, memakai dummy data internal, Room untuk progress belajar, dan DataStore untuk profil user sederhana.

## Fitur

- Onboarding tujuan belajar dan level awal.
- Dashboard XP, level, streak, progress, dan rekomendasi adaptif.
- Learning path 8 modul coding dasar dengan status terkunci, tersedia, dipelajari, selesai.
- Detail lesson berisi materi singkat, konsep penting, dan contoh kode.
- Quiz/challenge pilihan ganda, benar/salah, dan lengkapi kode.
- XP, level up, badge achievement, streak harian.
- AdaptiveLearningEngine dengan mastery score:
  `masteryScore = (quizScore * 0.5) + (accuracy * 0.3) + (streakBonus * 0.1) + (completionRate * 0.1)`.

## Struktur Folder

```text
app/src/main/java/com/codequest
|-- data
|   |-- local          # Room database, DAO, entity, DataStore, dummy content
|   |-- model          # Model domain/UI
|   `-- repository     # LearningRepository sebagai single source of truth
|-- domain
|   |-- algorithm      # AdaptiveLearningEngine
|   `-- usecase        # Use case dashboard dan submit quiz
|-- presentation
|   |-- component      # Komponen Compose reusable
|   |-- navigation     # Navigation Compose dan bottom navigation
|   |-- screen         # Onboarding, Dashboard, Learn, Lesson, Quiz, Achievement, Profile
|   `-- viewmodel      # ViewModel MVVM per screen
`-- ui
    `-- theme          # Material 3 light/dark theme
```

## Menjalankan

```powershell
$env:JAVA_HOME='C:\Program Files\Android\Android Studio\jbr'
.\gradlew.bat :app:assembleDebug
```

APK debug akan dibuat di:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Project juga bisa langsung dibuka dari Android Studio melalui folder root `CodeQuest`.
