import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    jvmToolchain(17)
    
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm("desktop") {
        compilations.all {
            compilerOptions.configure {
                jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.compose.uiTooling)
            implementation(libs.koin.android)
            
            // Fix Supabase auth pulling browser 1.9.0 which needs Android API 36
            implementation("androidx.browser:browser:1.8.0")
            
            // From shared androidMain
            implementation(libs.ktor.client.android)
            implementation(libs.kotlinx.coroutines.android)

            // Security
            implementation(libs.androidx.security.crypto)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                
                // From shared desktopMain
                implementation(libs.ktor.client.android) // Using Android client for JVM
                implementation(libs.kotlinx.coroutines.swing) // Main dispatcher for desktop
            }
        }
        commonMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            
            // Navigation
            implementation(libs.androidx.navigation.compose)
            
            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            
            // Koin
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.koin.core)
            
            // --- DEPENDENCIES FROM SHARED ---
            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
            
            // Serialization
            implementation(libs.kotlinx.serialization.json)
            
            // DateTime
            implementation(libs.kotlinx.datetime)
            
            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.auth)
            
            // Room
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
            
            // DataStore
            implementation(libs.androidx.datastore.preferences)

            // Okio
            implementation(libs.okio)

            // Secure Storage
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.no.arg)

            // Supabase
            implementation(libs.supabase.auth)
            implementation(libs.supabase.functions)
        }
        
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            // From shared commonTest
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.client.mock)
            implementation(libs.multiplatform.settings.test)
        }
    }
}

android {
    namespace = "com.app.budgetnote"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.app.budgetnote"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    flavorDimensions += "environment"
    productFlavors {
        create("staging") {
            dimension = "environment"
            applicationIdSuffix = ".staging"
            resValue("string", "app_name", "BudgetNote (Staging)")
        }
        create("production") {
            dimension = "environment"
            resValue("string", "app_name", "BudgetNote")
        }
    }
    buildFeatures {
        buildConfig = true
    }
    sourceSets {
        getByName("debug") {
            resources.srcDirs("src/commonMain")
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    // KSP for Room
    add("kspAndroid", libs.androidx.room.compiler)
    // kspIosX64 removed as target not present
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspDesktop", libs.androidx.room.compiler)
}

compose.desktop {
    application {
        mainClass = "MainKt"
        
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "BudgetNote"
            packageVersion = "1.0.0"
            
            macOS {
                iconFile.set(project.file("src/desktopMain/resources/icon.icns"))
            }
            windows {
                iconFile.set(project.file("src/desktopMain/resources/icon.ico"))
            }
            linux {
                iconFile.set(project.file("src/desktopMain/resources/icon.png"))
            }
        }
    }
}

// Force androidx.browser to 1.8.0 because Supabase auth pulls 1.9.0 which requires compileSdk 36
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "androidx.browser" && requested.name == "browser") {
            useVersion("1.8.0")
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
}

buildkonfig {
    packageName = "com.app.budgetnote.core.config"
    
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { localProperties.load(it) }
    }

    // Default configuration (statically acts as Staging)
    defaultConfigs {
        buildConfigField(STRING, "BASE_API_URL", localProperties.getProperty("staging.BASE_API_URL") ?: "")
        buildConfigField(STRING, "FLAVOR_NAME", localProperties.getProperty("staging.FLAVOR_NAME") ?: "staging")
        buildConfigField(STRING, "SUPABASE_KEY", localProperties.getProperty("staging.SUPABASE_KEY") ?: "")
        buildConfigField(STRING, "GOOGLE_SIGNIN_SERVER_ID", localProperties.getProperty("staging.GOOGLE_SIGNIN_SERVER_ID") ?: "")
        buildConfigField(STRING, "FIREBASE_ADMIN_API_KEY", localProperties.getProperty("staging.FIREBASE_ADMIN_API_KEY") ?: "")
        buildConfigField(STRING, "PLATFORM", "Unknown")
    }

    // Explicit Staging Config (Fallback if explicitly configured with flavor `staging`)
    defaultConfigs("staging") {
        buildConfigField(STRING, "BASE_API_URL", localProperties.getProperty("staging.BASE_API_URL") ?: "")
        buildConfigField(STRING, "FLAVOR_NAME", localProperties.getProperty("staging.FLAVOR_NAME") ?: "staging")
        buildConfigField(STRING, "SUPABASE_KEY", localProperties.getProperty("staging.SUPABASE_KEY") ?: "")
        buildConfigField(STRING, "GOOGLE_SIGNIN_SERVER_ID", localProperties.getProperty("staging.GOOGLE_SIGNIN_SERVER_ID") ?: "")
        buildConfigField(STRING, "FIREBASE_ADMIN_API_KEY", localProperties.getProperty("staging.FIREBASE_ADMIN_API_KEY") ?: "")
        buildConfigField(STRING, "PLATFORM", "Unknown")
    }

    // Explicit Production Config
    defaultConfigs("production") {
        buildConfigField(STRING, "BASE_API_URL", localProperties.getProperty("production.BASE_API_URL") ?: "")
        buildConfigField(STRING, "FLAVOR_NAME", localProperties.getProperty("production.FLAVOR_NAME") ?: "production")
        buildConfigField(STRING, "SUPABASE_KEY", localProperties.getProperty("production.SUPABASE_KEY") ?: "")
        buildConfigField(STRING, "GOOGLE_SIGNIN_SERVER_ID", localProperties.getProperty("production.GOOGLE_SIGNIN_SERVER_ID") ?: "")
        buildConfigField(STRING, "FIREBASE_ADMIN_API_KEY", localProperties.getProperty("production.FIREBASE_ADMIN_API_KEY") ?: "")
        buildConfigField(STRING, "PLATFORM", "Unknown")
    }

    // Android Target Overrides
    targetConfigs {
        create("android") {
            buildConfigField(STRING, "PLATFORM", "Android")
        }
    }

    // iOS Target Overrides
    targetConfigs {
        create("iosArm64") {
            buildConfigField(STRING, "PLATFORM", "Ios")
        }
        create("iosSimulatorArm64") {
            buildConfigField(STRING, "PLATFORM", "Ios")
        }
    }

    // Desktop/JVM Target Overrides
    targetConfigs {
        create("desktop") {
            buildConfigField(STRING, "PLATFORM", "Desktop")
        }
    }
}
