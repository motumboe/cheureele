# Review Tecnica

Backlog tecnico emerso dalla review del progetto `CheUreEle`.

## Priorita alta

- [x] Rimuovere `android.permission.BIND_APPWIDGET` dal manifest.
  Motivo: e una permission protetta riservata alle system app e oggi blocca `lintDebug`.

- [x] Separare `debug` e `release` con package installabili in parallelo.
  Motivo: con lo stesso `applicationId` le due varianti entrano in conflitto e la `release` non si installa sopra la `debug` firmata con chiave diversa.

- [x] Far fallire la build `release` quando la signing config manca o e incompleta.
  Motivo: la generazione silenziosa di un APK unsigned porta a una build apparentemente riuscita ma non installabile.

- [ ] Rivalutare il refresh al minuto del widget sotto Doze e device idle.
  Motivo: `AlarmManager.set()` non garantisce puntualita al minuto su Android moderno.

## Priorita media

- [ ] Migliorare l'accessibilita del widget.
  Motivo: il layout full usa una `contentDescription` statica che non rappresenta il contenuto reale mostrato.

- [ ] Ridurre la fragilita della logica testuale dell'ora.
  Motivo: la tabella manuale dei quarti e soggetta a typo e drift tra codice, preview e test.

- [ ] Aggiungere test strumentali reali per widget, resize e lifecycle.
  Motivo: gli androidTest attuali sono ancora quelli di template.

- [ ] Estrarre il testo hardcoded dai layout XML di widget e preview.
  Motivo: lint segnala stringhe fisse nei layout e conviene centralizzarle nelle resources.

- [ ] Ripulire risorse e configurazioni residue del template Android.
  Motivo: ci sono colori, dimens, label e cartelle `-v21` / `-v26` non piu necessarie con `minSdk = 26`.

- [ ] Aggiungere il layer monochrome alle adaptive icon.
  Motivo: lint segnala l'assenza dell'icona monocromatica per launcher moderni.

## Priorita bassa

- [ ] Aggiornare dipendenze e target SDK dopo una verifica di compatibilita.
  Motivo: lint segnala versioni datate, ma e un intervento da fare con test funzionali.
