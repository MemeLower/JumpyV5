# Jumpy - Ein LibGDX Plattformspiel

## Über das Spiel
Jumpy ist ein spannendes Plattformspiel, entwickelt mit dem LibGDX Framework. Das Spiel kombiniert klassische Plattform-Mechaniken mit modernen Spielelementen und bietet sowohl einen Level-basierten Modus als auch einen Endlos-Modus.

## Hauptfunktionen

### Spielmodi
- **Level-Modus**: 5 sorgfältig gestaltete Level mit steigendem Schwierigkeitsgrad
- **Endlos-Modus**: Ein endloser Spielmodus, bei dem der Spieler so weit wie möglich kommen muss
- **Bestenliste**: Speichern Sie Ihre Highscores und vergleichen Sie sich mit anderen Spielern

### Steuerung
- **A/D oder Pfeiltasten**: Bewegung nach links/rechts
- **Leertaste**: Springen
- **M**: Zurück zum Hauptmenü
- **R**: Level neu starten

### Spielmechaniken
- **Bewegung**: Flüssige Bewegungssteuerung mit Beschleunigung und Abbremsen
- **Springen**: Präzises Springen mit variabler Sprunghöhe
- **Kollisionen**: Realistische Kollisionserkennung mit Plattformen
- **Ziele**: Erreichen Sie das Ziel-Flagge, um das Level abzuschließen

## Technische Details

### LibGDX Framework
Das Spiel wurde mit LibGDX entwickelt, einem leistungsstarken Java-Spielentwicklungsframework. LibGDX bietet:
- Cross-Platform-Unterstützung
- Effiziente 2D-Grafikrendering
- Eingebaute Physik-Engine
- Einfache Asset-Verwaltung

### Projektstruktur
```
core/               # Hauptspiellogik
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── mygdx/
│                   └── game/
│                       ├── MainGame.java
│                       ├── Player.java
│                       ├── Platform.java
│                       └── ...
lwjgl3/            # Desktop-Version
└── assets/        # Spielelemente
    ├── images/
    └── sounds/
```

## Screenshots
[Hier Screenshots einfügen]
- Hauptmenü
- Level 1
- Endlos-Modus
- Bestenliste

## Installation und Ausführung

### Voraussetzungen
- Java Development Kit (JDK) 8 oder höher
- Gradle Build System

### Kompilieren und Ausführen
```bash
./gradlew desktop:run
```

## Entwicklung
Das Spiel wurde entwickelt mit:
- LibGDX 1.12.0
- Java 8
- Gradle 7.0

## Lizenz
[Lizenzinformationen hier einfügen]

## Credits
- Entwickelt von [Ihr Name]
- LibGDX Framework: https://libgdx.com/
- Assets: [Quellenangaben hier einfügen]

---

## Derzeitige Funktionen

- **Springen**  
- **Bewegung nach links und rechts**  
- **Automatisches Reset, wenn du von einer Plattform fällst**  
- **Manuelles Reset mit der Taste „R“**

- **Hauptmenü**
- **Mehrere wählbare Levels**
- **Endless Mode**


---

## Implementiert

- */*

---

## notes

- **Besiegen**

---

## Offene Punkte

  
- **ziel/level ende**   
- **verbessertes design für character, level und menu (Tiled Maps support?)**
- **Highscore/Distance counter & Username eingabe für Leaderboard (endless mode)**

- **Power ups?**

- **sound effects + background music(menü,level,endless mode)**

- **more levels?**

- **healthbar, fullscreen, pause button?**

---
