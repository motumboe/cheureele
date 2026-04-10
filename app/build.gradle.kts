import org.gradle.api.GradleException
import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keystoreProperties = Properties()
val keystorePropertiesFile = rootProject.file("keystore.properties")

if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use(keystoreProperties::load)
}

fun getReleaseSigningValue(name: String): String? =
    keystoreProperties.getProperty(name)
        ?: providers.environmentVariable(name).orNull

val releaseStoreFilePath = getReleaseSigningValue("RELEASE_STORE_FILE")
val releaseStoreFile =
    releaseStoreFilePath
        ?.takeIf(String::isNotBlank)
        ?.let(rootProject::file)
val releaseStorePassword = getReleaseSigningValue("RELEASE_STORE_PASSWORD")
val releaseKeyAlias = getReleaseSigningValue("RELEASE_KEY_ALIAS")
val releaseKeyPassword = getReleaseSigningValue("RELEASE_KEY_PASSWORD")
val releaseSigningProblems = buildList {
    when {
        releaseStoreFilePath.isNullOrBlank() -> add("RELEASE_STORE_FILE mancante")
        releaseStoreFile?.exists() != true -> add("RELEASE_STORE_FILE punta a un file inesistente")
    }

    if (releaseStorePassword.isNullOrBlank()) add("RELEASE_STORE_PASSWORD mancante")
    if (releaseKeyAlias.isNullOrBlank()) add("RELEASE_KEY_ALIAS mancante")
    if (releaseKeyPassword.isNullOrBlank()) add("RELEASE_KEY_PASSWORD mancante")
}
val hasReleaseSigning =
    releaseSigningProblems.isEmpty()

fun requiresSignedReleaseArtifact(taskPath: String): Boolean {
    val taskName = taskPath.substringAfterLast(':')
    return taskName in setOf(
        "assemble",
        "assembleRelease",
        "bundle",
        "bundleRelease",
        "installRelease",
        "packageRelease"
    )
}

gradle.taskGraph.whenReady {
    val wantsSignedReleaseArtifact = allTasks.any { requiresSignedReleaseArtifact(it.path) }

    if (wantsSignedReleaseArtifact && !hasReleaseSigning) {
        throw GradleException(
            "Signing release mancante o incompleta: " +
                releaseSigningProblems.joinToString(", ") +
                ". Copia keystore.properties.example in keystore.properties e inserisci i valori reali."
        )
    }
}

val appVersionCode = providers.gradleProperty("APP_VERSION_CODE").get().toInt()
val appVersionName = providers.gradleProperty("APP_VERSION_NAME").get()

android {
    namespace = "com.example.cheureele"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.cheureele"
        minSdk = 26
        targetSdk = 34
        versionCode = appVersionCode
        versionName = appVersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        if (hasReleaseSigning) {
            create("release") {
                storeFile = releaseStoreFile
                storePassword = releaseStorePassword
                keyAlias = releaseKeyAlias
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }

        release {
            if (hasReleaseSigning) {
                signingConfig = signingConfigs.getByName("release")
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

val githubReleaseDir = layout.buildDirectory.dir("outputs/github-release")

tasks.register<Copy>("stageReleaseApk") {
    dependsOn("assembleRelease")
    from(layout.buildDirectory.file("outputs/apk/release/app-release.apk"))
    into(githubReleaseDir)
    rename { "CheUreEle-v$appVersionName.apk" }
}

tasks.register<Copy>("stageLatestReleaseApk") {
    dependsOn("assembleRelease")
    from(layout.buildDirectory.file("outputs/apk/release/app-release.apk"))
    into(githubReleaseDir)
    rename { "CheUreEle-latest.apk" }
}

tasks.register<Copy>("stageDebugApk") {
    dependsOn("assembleDebug")
    from(layout.buildDirectory.file("outputs/apk/debug/app-debug.apk"))
    into(githubReleaseDir)
    rename { "CheUreEle-v$appVersionName-debug.apk" }
}

tasks.register("stageReleaseAssets") {
    dependsOn("stageReleaseApk", "stageLatestReleaseApk")
}
