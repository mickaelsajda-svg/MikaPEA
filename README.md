# Mika PEA (Android)

Application Android (Kotlin + Jetpack Compose) pour suivre un portefeuille boursier.

## Générer l'APK (debug)
- Android Studio : **Build → Build APK(s)** puis récupérer `app/build/outputs/apk/debug/app-debug.apk`.

## CI GitHub (build automatique)
- Fichier: `.github/workflows/build-apk.yml` (inclus)
- Poussez le projet sur GitHub, allez dans l'onglet **Actions**, lancez "Build APK" et téléchargez l'artefact.

## Clé API Alpha Vantage (optionnelle)
- Ajoutez une clé via *Paramètres* dans l'app pour rafraîchir les cours.
