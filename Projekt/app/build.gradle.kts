// Sekcja konfiguracji pluginów
plugins {
    alias(libs.plugins.android.application) // Plugin do budowania aplikacji Android
    alias(libs.plugins.kotlin.android) // Plugin do obsługi Kotlina w Androidzie
    alias(libs.plugins.kotlin.serialization) // Plugin do serializacji danych w Kotlinie
}

// Sekcja konfiguracji Android
android {
    namespace = "com.example.lista6" // Przestrzeń nazw aplikacji
    compileSdk = 35 // Wersja SDK używana do kompilacji

    defaultConfig {
        applicationId = "com.example.lista6" // Identyfikator aplikacji
        minSdk = 28 // Minimalna wersja SDK obsługiwana przez aplikację
        targetSdk = 35 // Docelowa wersja SDK
        versionCode = 1 // Wersja kodu aplikacji
        versionName = "1.0" // Wersja nazwy aplikacji

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Runner do testów
        vectorDrawables {
            useSupportLibrary = true // Wsparcie dla wektorowych rysunków
        }
    }

    // Konfiguracja typów buildów
    buildTypes {
        release {
            isMinifyEnabled = false // Wyłączenie minifikacji w wersji release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            ) // Pliki konfiguracyjne ProGuard
        }
    }

    // Opcje kompilacji Java
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // Kompatybilność źródła z Java 8
        targetCompatibility = JavaVersion.VERSION_1_8 // Kompatybilność celu z Java 8
    }

    // Opcje Kotlina
    kotlinOptions {
        jvmTarget = "1.8" // Cel JVM dla Kotlina
    }

    // Funkcje budowania
    buildFeatures {
        compose = true // Włączenie Jetpack Compose
    }

    // Opcje Jetpack Compose
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1" // Wersja kompilatora Kotlin dla Compose
    }

    // Konfiguracja pakowania zasobów
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}" // Wykluczenie niepotrzebnych plików
        }
    }
}

// Sekcja zależności
dependencies {
    // Biblioteki podstawowe
    implementation(libs.androidx.core.ktx) // Kotlin Extensions dla AndroidX Core
    implementation(libs.androidx.lifecycle.runtime.ktx) // Lifecycle Runtime z Kotlin Extensions
    implementation(libs.androidx.activity.compose) // Obsługa Activity z Jetpack Compose
    implementation(libs.kotlinx.serialization) // Biblioteka do serializacji danych w Kotlinie

    // Biblioteki Jetpack Compose
    implementation(platform(libs.androidx.compose.bom)) // Zarządzanie wersjami Compose przez BOM
    implementation(libs.androidx.ui) // Podstawowe komponenty UI Compose
    implementation(libs.androidx.ui.graphics) // Obsługa grafiki w Compose
    implementation(libs.androidx.ui.tooling.preview) // Narzędzia do podglądu UI w Compose
    implementation(libs.androidx.material3) // Komponenty Material Design 3 dla Compose

    // Nawigacja w Jetpack Compose
    implementation(libs.androidx.navigation.compose) // Biblioteka do nawigacji w Compose

    // Biblioteki do gestów, układów i podstawowych funkcji
    implementation("androidx.compose.foundation:foundation:1.5.0") // Podstawowe funkcje Compose
    implementation("androidx.compose.foundation:foundation-layout:1.5.0") // Zarządzanie układami w Compose

    // Serializacja JSON w Kotlinie
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0") // Serializacja JSON
    implementation("androidx.datastore:datastore-preferences:1.0.0") // Przechowywanie danych w DataStore
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2") // Lifecycle z Kotlin Extensions

    // Obsługa zadań w tle
    implementation("androidx.work:work-runtime-ktx:2.8.1") // Biblioteka do zarządzania zadaniami w tle

    // Testy jednostkowe i testy UI
    testImplementation(libs.junit) // Biblioteka do testów jednostkowych
    androidTestImplementation(libs.androidx.junit) // Testy JUnit dla Android
    androidTestImplementation(libs.androidx.espresso.core) // Testy UI z Espresso
    androidTestImplementation(platform(libs.androidx.compose.bom)) // Testy UI dla Compose
    androidTestImplementation(libs.androidx.ui.test.junit4) // Testy UI dla Compose z JUnit4

    // Debugowanie
    debugImplementation(libs.androidx.ui.tooling) // Narzędzia do debugowania UI w Compose
    debugImplementation(libs.androidx.ui.test.manifest) // Manifest do testów UI w Compose
}