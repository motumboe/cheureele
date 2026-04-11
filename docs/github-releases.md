# GitHub Releases

Questa guida descrive un flusso leggero e ripetibile per distribuire `CheUreEle` a pochi amici tramite GitHub Releases.

## Strategia consigliata

- se vuoi condividere gli APK senza login GitHub, rendi pubblico il repository
- usa GitHub Releases come canale di distribuzione dell'APK
- allega sempre solo la build `release`
- conserva sempre lo stesso keystore, altrimenti gli aggiornamenti smettono di funzionare

Se il repository e privato, gli asset delle release sono comodi solo per chi ha accesso al repository. Per amici non tecnici, la soluzione piu semplice e usare un repository pubblico separato solo per le release, oppure condividere direttamente l'APK con un link stabile.

## Link consigliati

Per il `README` e per la condivisione generale, non e una buona pratica aggiornare ogni volta un link puntato al singolo asset versionato.

Il riferimento a manutenzione minima e:

- pagina ultima release: `https://github.com/motumboe/cheureele/releases/latest`

Questo URL resta stabile e reindirizza sempre all'ultima release pubblicata.

Per il download diretto di un singolo APK, invece, GitHub usa URL che includono il tag della release, per esempio:

- `https://github.com/motumboe/cheureele/releases/download/v1.1.0/CheUreEle-v1.1.0.apk`
- `https://github.com/motumboe/cheureele/releases/download/v1.1.0/CheUreEle-latest.apk`

Anche se il file si chiama `CheUreEle-latest.apk`, l'URL cambia comunque a ogni tag. Con GitHub Releases da solo non hai quindi un URL diretto all'APK davvero immutabile.

Se vuoi il minimo impatto possibile:

- nel `README` usa sempre `releases/latest`
- nella release allega sia l'APK versionato sia `CheUreEle-latest.apk`
- non aggiornare il `README` a ogni nuova versione

Se un domani vorrai un link diretto all'APK davvero stabile, serve un livello in piu, per esempio GitHub Pages con redirect o un dominio/file hosting esterno.

## Versioning consigliato

Nel progetto usiamo due valori distinti:

- `APP_VERSION_NAME`
  SemVer visibile all'utente, per esempio `1.1.0`
- `APP_VERSION_CODE`
  intero Android monotono crescente, per esempio `2`

Regola pratica:

- patch: `1.1.0 -> 1.1.1`
- minor: `1.1.0 -> 1.2.0`
- major: `1.1.0 -> 2.0.0`
- a ogni release pubblicata, incrementa sempre anche `APP_VERSION_CODE`

Questi valori stanno in `gradle.properties`.

## Asset generati

Gli APK standard restano quelli di Gradle:

- `app/build/outputs/apk/release/app-release.apk`
- `app/build/outputs/apk/debug/app-debug.apk`

Per preparare gli asset con nome adatto alla distribuzione:

```bash
./gradlew stageReleaseAssets
./gradlew stageDebugApk
```

Output:

- `app/build/outputs/github-release/CheUreEle-v1.1.0.apk`
- `app/build/outputs/github-release/CheUreEle-latest.apk`
- `app/build/outputs/github-release/CheUreEle-v1.1.0-debug.apk`

## Procedura di rilascio

1. aggiorna `APP_VERSION_NAME` e `APP_VERSION_CODE` in `gradle.properties`
2. verifica tutto:

```bash
./gradlew test lintDebug assembleRelease
```

3. genera e prepara l'APK da pubblicare:

```bash
./gradlew stageReleaseAssets
```

4. crea un tag Git:

```bash
git tag v1.1.0
git push origin v1.1.0
```

5. crea la GitHub Release con titolo `v1.1.0`
6. allega almeno questi due asset generati in `app/build/outputs/github-release/`:
   - `CheUreEle-v1.1.0.apk`
   - `CheUreEle-latest.apk`
7. inserisci note brevi con cambiamenti e bugfix principali

Se il repository e pubblico, i tuoi amici possono aprire senza login:

- pagina release corrente: `https://github.com/motumboe/cheureele/releases/latest`

## Uso con GitHub CLI

Se usi `gh`, il flusso minimo e:

```bash
gh release create v1.1.0 \
  app/build/outputs/github-release/CheUreEle-v1.1.0.apk \
  app/build/outputs/github-release/CheUreEle-latest.apk \
  --title "v1.1.0" \
  --notes "..."
```

Se il tag esiste gia e la release esiste gia, carica o aggiorna gli asset con:

```bash
gh release upload v1.1.0 \
  app/build/outputs/github-release/CheUreEle-v1.1.0.apk \
  app/build/outputs/github-release/CheUreEle-latest.apk \
  --clobber
```

`--clobber` sovrascrive un asset con lo stesso nome gia presente nella release.

## Pre-release

Per build non definitive:

- usa versioni tipo `1.2.0-beta.1`
- marca la release GitHub come pre-release
- tieni `APP_VERSION_CODE` crescente anche per beta e RC

## Cose da non fare

- non distribuire la build `debug`
- non cambiare keystore dopo aver iniziato a distribuire l'app
- non riusare lo stesso `APP_VERSION_CODE`
