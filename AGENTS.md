# AGENTS.md

## Obiettivo

Mantenere e far evolvere `CheUreEle` come app Android con widget homescreen che mostra l'ora in forma testuale e numerica, con attenzione alla leggibilita del widget, alla correttezza del calcolo orario e a patch piccole e verificabili.

## Vincoli

- Non versionare segreti, chiavi, file locali di Android Studio o credenziali di signing.
- Preferire modifiche piccole, locali e facili da verificare.
- Non cambiare package name, namespace o configurazione Gradle senza un motivo esplicito.
- Se tocchi risorse testuali o logica oraria, preserva il lessico dialettale gia presente salvo richiesta contraria.
- Se modifichi il comportamento del widget, aggiorna anche le risorse e il manifest se necessario.
- Evitare dipendenze nuove se il risultato si puo ottenere con Android SDK o librerie gia presenti.

## Commit automatici

- Dopo ogni modifica completata, creare subito un commit.
- Non aspettare conferma dell'utente per eseguire il commit, salvo richiesta esplicita contraria.
- Usare sempre messaggi di commit in italiano in stile Conventional Commits.
- Accorpare piu modifiche nello stesso commit solo se fanno parte della stessa modifica logica o se l'utente lo chiede esplicitamente.

Formato:

```text
tipo(scope): descrizione
```

Tipi consigliati:

- `feat`
- `fix`
- `refactor`
- `docs`
- `test`
- `build`
- `chore`

Esempi:

- `feat(widget): aggiorna la resa dell'ora testuale`
- `fix(widget): corregge il refresh periodico del widget`
- `docs(repo): aggiunge istruzioni operative per gli agenti`

## Flusso operativo

1. Identificare il piu piccolo step utile e verificabile.
2. Limitare le modifiche ai file strettamente necessari.
3. Eseguire una verifica locale coerente con la modifica.
4. Creare subito il commit con messaggio Conventional Commit in italiano.

## Verifiche minime

- Per modifiche Kotlin o Compose: eseguire almeno `./gradlew test` se sostenibile.
- Per modifiche di build o risorse Android: preferire anche `./gradlew assembleDebug`.
- Se una verifica non e stata eseguita, dichiararlo esplicitamente nel resoconto finale.

## Note repository

- App module principale: `app/`
- Codice Kotlin: `app/src/main/java/com/example/cheureele/`
- Risorse widget: `app/src/main/res/layout/` e `app/src/main/res/xml/`
- Test locali: `app/src/test/`
- Test strumentali: `app/src/androidTest/`
