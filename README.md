# CheUreEle

CheUreEle e una piccola app Android con widget homescreen che mostra l ora in forma numerica e in forma testuale dialettale.

Il repository resta leggero, ma ora ha una schermata principale utile: mostra una anteprima dell ora corrente, spiega come usare il widget e permette un refresh immediato.

## Stack

- Kotlin
- Android SDK
- App Widget con AppWidgetProvider e RemoteViews
- Jetpack Compose per la schermata MainActivity
- Gradle Kotlin DSL

## Requisiti

- Android Studio recente oppure Android SDK installato localmente
- JDK compatibile con Android Gradle Plugin 8.2.x
- adb disponibile se vuoi provare la app su smartphone da terminale

## Setup locale

Il progetto richiede una SDK Android configurata.

Puoi configurarla in uno di questi modi:

1. creare un file local.properties non versionato con:

    sdk.dir=/percorso/alla/tua/Android/Sdk

2. oppure esportare ANDROID_HOME o ANDROID_SDK_ROOT

Se dopo una copia manuale del repository gradlew perde il bit eseguibile, ripristinalo con:

    chmod +x gradlew

## Comandi utili

Test unitari:

    ./gradlew test

Build APK debug:

    ./gradlew assembleDebug

Installazione rapida su device collegato:

    ./gradlew installDebug

Build release firmata, se hai configurato `keystore.properties` locale o le variabili ambiente di signing:

    ./gradlew assembleRelease

## Struttura essenziale

- app/src/main/java/com/example/cheureele/MainActivity.kt
  Schermata launcher della app.
- app/src/main/java/com/example/cheureele/YourWidgetProvider.kt
  Provider del widget e logica di rendering orario.
- app/src/main/res/layout/your_widget_provider.xml
  Layout completo del widget.
- app/src/main/res/layout/your_widget_provider_compact.xml
  Layout testuale per spazi ridotti.
- app/src/main/res/layout/your_widget_provider_mini.xml
  Layout mini per widget ridotti fino a 1x1.
- app/src/main/res/xml/your_widget_provider_info.xml
  Metadati Android del widget.

## Documentazione operativa

- docs/debug-su-smartphone.md
- docs/creare-apk.md

## Stato attuale

- Il widget mostra ora numerica e ora testuale.
- Il widget passa automaticamente a una resa testuale quando viene ridotto, per preservare il lato giocoso del widget anche in spazi molto piccoli.
- In modalita mini e compatta mostra solo la resa testuale; in modalita full mostra anche l ora numerica.
- L aggiornamento del widget passa da AlarmManager con schedulazione al minuto successivo.
- La schermata principale mostra anteprima, istruzioni rapide e un pulsante per forzare il refresh del widget.
- Il repository include test unitari reali sulla formattazione dell ora e sulla selezione del layout widget.

## Verifica eseguita su questa macchina

Ho eseguito con successo:

- ./gradlew test
- ./gradlew assembleDebug
- ./gradlew installDebug

La app debug e stata installata e avviata su un device Android fisico collegato via adb.
