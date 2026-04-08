# Creare Un APK

Questa guida copre i casi pratici per generare APK debug e release di `CheUreEle`.

## Prerequisiti

- SDK Android configurata
- JDK compatibile
- Gradle wrapper funzionante
- `adb` disponibile se vuoi installare da terminale

Se `./gradlew` non e eseguibile nel clone corrente, usa:

```bash
chmod +x gradlew
```

## Build debug

Per generare un APK debug:

```bash
./gradlew assembleDebug
```

Output:

```text
app/build/outputs/apk/debug/app-debug.apk
```

Per installarlo su un device collegato:

```bash
./gradlew installDebug
```

## Build release

La release usa una firma locale opzionale. Il repository legge i valori da uno di questi punti:

1. `keystore.properties` nella root del progetto
2. variabili ambiente con gli stessi nomi

Chiavi richieste:

```properties
RELEASE_STORE_FILE=/percorso/al/keystore.jks
RELEASE_STORE_PASSWORD=...
RELEASE_KEY_ALIAS=...
RELEASE_KEY_PASSWORD=...
```

Nel repository trovi un template sicuro da copiare:

```text
keystore.properties.example
```

Il file reale `keystore.properties` e ignorato da Git.

## Generare una release firmata

Una volta creato `keystore.properties` e il keystore locale:

```bash
./gradlew assembleRelease
```

Output atteso:

```text
app/build/outputs/apk/release/app-release.apk
```

Se la configurazione di signing manca o punta a un file inesistente, Gradle torna a produrre una release non firmata:

```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

## Creare il keystore una volta sola

Esempio pratico:

```bash
keytool -genkeypair \
  -keystore ~/.android/cheureele-release.jks \
  -alias cheureele \
  -storetype PKCS12 \
  -keyalg RSA \
  -keysize 4096 \
  -validity 3650
```

Poi compila `keystore.properties` con il path e le password reali.

## Installazione su telefono

Debug:

```bash
./gradlew installDebug
```

Release:

```bash
adb install -r app/build/outputs/apk/release/app-release.apk
```

Nota pratica: se sul telefono hai gia la build `debug`, una `release` firmata con un certificato diverso non la puo aggiornare sopra. In quel caso devi prima disinstallare la versione esistente oppure installare la stessa variante firmata con la stessa chiave.

## Verifiche consigliate

```bash
./gradlew test
./gradlew assembleDebug
./gradlew assembleRelease
```

## Problemi comuni

`SDK location not found`

- configura `local.properties`
- oppure esporta `ANDROID_HOME` / `ANDROID_SDK_ROOT`

`Permission denied` su `./gradlew`

- fai `chmod +x gradlew`

`INSTALL_PARSE_FAILED_NO_CERTIFICATES`

- stai provando a installare un APK release non firmato

`INSTALL_FAILED_UPDATE_INCOMPATIBLE`

- sul telefono c'e gia la stessa app firmata con una chiave diversa
