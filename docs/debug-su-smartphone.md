# Debug Su Smartphone

Questa guida serve per installare e debuggare `CheUreEle` su un telefono Android fisico.

## Prerequisiti

- Android Studio installato oppure Android SDK + `adb`
- cavo USB dati oppure debug wireless gia configurato
- SDK Android configurata nel progetto

Se il progetto non vede la SDK, crea `local.properties`:

```properties
sdk.dir=/percorso/alla/tua/Android/Sdk
```

Oppure esporta una variabile d'ambiente:

```bash
export ANDROID_HOME="$HOME/Android/Sdk"
export ANDROID_SDK_ROOT="$HOME/Android/Sdk"
```

Se `./gradlew` fallisce con `Permission denied`, ripristina il bit eseguibile:

```bash
chmod +x gradlew
```

## Abilitare il debug USB

Sul telefono:

1. apri `Impostazioni`
2. abilita le `Opzioni sviluppatore`
3. attiva `Debug USB`
4. collega il device e accetta la chiave RSA del computer

Verifica da terminale:

```bash
adb devices
```

Il device deve comparire come `device`, non come `unauthorized`.

## Installare la build debug

Da terminale:

```bash
./gradlew installDebug
```

La variante `debug` viene installata come package `com.example.cheureele.debug`, quindi puo convivere con la `release` sullo stesso telefono.

In alternativa, da Android Studio:

1. apri il progetto
2. aspetta la sync Gradle
3. seleziona il device fisico
4. premi `Run`

## Test del widget

Dopo l'installazione:

1. apri l'app almeno una volta
2. torna alla home
3. aggiungi il widget `CheUreEle`
4. osserva se il widget mostra la resa corretta per la dimensione: numerica piu testuale da grande, solo testuale quando e ridotto

Nel codice attuale, aprire `MainActivity` forza anche un broadcast di aggiornamento del widget e mostra un pulsante `Aggiorna widget ora`, quindi hai due modi rapidi per provocare un refresh mentre stai facendo debug.

## Log utili

Logcat filtrato dai tag gia presenti:

```bash
adb logcat | grep -E 'widget'
```

Se usi Android Studio, apri `Logcat` e filtra per package `com.example.cheureele.debug` mentre stai provando la build debug.

## Controlli utili per widget e allarmi

Stato widget:

```bash
adb shell dumpsys appwidget | grep -A 20 cheureele
```

Allarmi schedulati:

```bash
adb shell dumpsys alarm | grep -i cheureele
```

Questi due comandi sono utili per capire se il widget e registrato correttamente e se `AlarmManager` sta davvero schedulando gli aggiornamenti.

## Problemi comuni

`SDK location not found`

- manca `local.properties`
- manca `ANDROID_HOME`
- Android Studio non ha ancora scaricato le piattaforme richieste

`Permission denied` su `./gradlew`

- il file ha perso il bit eseguibile sul filesystem locale
- ripristinalo con `chmod +x gradlew`

Il telefono non compare in `adb devices`

- cambia cavo USB
- cambia modalita USB sul device
- revoca e riaccetta le autorizzazioni debug
- verifica che `adb` veda davvero il device con `adb kill-server && adb start-server`

Il widget non si aggiorna

- apri l'app per forzare un update iniziale
- controlla `adb logcat`
- controlla `dumpsys alarm`
- verifica che il widget sia stato davvero aggiunto alla home
