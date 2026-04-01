# CheUreEle

CheUreEle e una piccola app Android con widget homescreen che mostra l'ora in forma numerica e in forma testuale dialettale.

Il repository resta leggero, ma ora ha una schermata principale utile: mostra un'anteprima dell'ora corrente, spiega come usare il widget e permette un refresh immediato.

## Stack

- Kotlin
- Android SDK
- App Widget (`AppWidgetProvider` + `RemoteViews`)
- Jetpack Compose per la schermata `MainActivity`
- Gradle Kotlin DSL

## Requisiti

- Android Studio recente oppure Android SDK installato localmente
- JDK compatibile con Android Gradle Plugin 8.2.x
- `adb` disponibile se vuoi provare l'app su smartphone da terminale

## Setup locale

Il progetto richiede una SDK Android configurata. Senza questa configurazione, i task Gradle falliscono con errore `SDK location not found`.

Puoi configurarla in uno di questi modi:

1. creare un file `local.properties` non versionato con:

```properties
sdk.dir=/percorso/alla/tua/Android/Sdk
```

2. oppure esportare `ANDROID_HOME` o `ANDROID_SDK_ROOT`

Se dopo una copia manuale del repository `gradlew` perde il bit eseguibile, ripristinalo con:

```bash
chmod +x gradlew
```

## Comandi utili

Test unitari:

```bash
./gradlew test
```

Build APK debug:

```bash
./gradlew assembleDebug
```

Installazione rapida su device collegato:

```bash
./gradlew installDebug
```

## Struttura essenziale

- `app/src/main/java/com/example/cheureele/MainActivity.kt`
  Schermata launcher dell'app.
- `app/src/main/java/com/example/cheureele/YourWidgetProvider.kt`
  Provider del widget e logica di calcolo del testo orario.
- `app/src/main/res/layout/your_widget_provider.xml`
  Layout `RemoteViews` del widget.
- `app/src/main/res/xml/your_widget_provider_info.xml`
  Metadati Android del widget.

## Documentazione operativa

- [Debug su smartphone](docs/debug-su-smartphone.md)
- [Creare un APK](docs/creare-apk.md)

## Stato attuale

- Il widget mostra ora numerica e ora testuale.
- L'aggiornamento del widget passa da `AlarmManager` con schedulazione al minuto successivo.
- La schermata principale mostra anteprima, istruzioni rapide e un pulsante per forzare il refresh del widget.
- Il repository include test unitari reali sulla formattazione dell'ora.

## Verifica eseguita su questa macchina

Ho provato a lanciare:

- `./gradlew test`
- `./gradlew assembleDebug`

Entrambi falliscono al momento per assenza di configurazione SDK Android locale (`local.properties`, `ANDROID_HOME` o `ANDROID_SDK_ROOT`).
