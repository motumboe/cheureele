# Creare Un APK

Questa guida copre i casi pratici piu semplici per generare un APK di `CheUreEle`.

## Prerequisiti

- SDK Android configurata
- JDK compatibile
- Gradle wrapper funzionante

Se `./gradlew` non e eseguibile nel clone corrente, usa:

```bash
chmod +x gradlew
```

## Build debug

Per generare un APK debug:

```bash
./gradlew assembleDebug
```

Output atteso:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Per installarlo su un device collegato:

```bash
./gradlew installDebug
```

## Build release

Per generare una build release non firmata:

```bash
./gradlew assembleRelease
```

Output atteso:

```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

Nel repository attuale non c'e ancora una configurazione di signing in `app/build.gradle.kts`, quindi la release generata da Gradle non e pronta per distribuzione diretta.

## APK firmato

Per distribuire davvero l'app serve aggiungere:

1. un keystore
2. una `signingConfig`
3. credenziali non versionate

Schema tipico:

```kotlin
android {
    signingConfigs {
        create("release") {
            storeFile = file("release.keystore")
            storePassword = providers.gradleProperty("RELEASE_STORE_PASSWORD").get()
            keyAlias = providers.gradleProperty("RELEASE_KEY_ALIAS").get()
            keyPassword = providers.gradleProperty("RELEASE_KEY_PASSWORD").get()
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

Le password non vanno committate: meglio `~/.gradle/gradle.properties`, variabili d'ambiente o un file locale escluso da Git.

## Verifiche consigliate prima di usare un APK

```bash
./gradlew test
./gradlew assembleDebug
```

Se vuoi preparare una release piu seria, aggiungi anche:

```bash
./gradlew assembleRelease
```

## Problemi comuni

`SDK location not found`

- configura `local.properties`
- oppure esporta `ANDROID_HOME` / `ANDROID_SDK_ROOT`

`Permission denied` su `./gradlew`

- fai `chmod +x gradlew`

`assembleRelease` produce un APK non installabile come release finale

- manca la firma
- va aggiunta una `signingConfig`
