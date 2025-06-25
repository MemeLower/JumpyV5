package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class LeaderboardManager {
    // BESTENLISTEN-EINSTELLUNGEN: Konfiguration für das Score-System
    private static final String SCORES_FILE = "highscores.json";  // Datei für Score-Speicherung
    private static final int MAX_SCORES = 10;                     // Maximale Anzahl Top-Scores
    private Array<ScoreEntry> scores;                             // Liste aller Scores
    private Json json;                                            // JSON-Parser für Datei-Operationen

    public LeaderboardManager() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        scores = new Array<>();
        loadScores();
    }

    public void addScore(String username, float score) {
        // SCORE-HINZUFÜGEN: Füge neuen Score hinzu oder aktualisiere bestehenden
        
        // BENUTZER PRÜFEN: Schaue ob Benutzername bereits existiert
        ScoreEntry existingEntry = null;
        for (ScoreEntry entry : scores) {
            if (entry.getUsername().equals(username)) {
                existingEntry = entry;
                break;
            }
        }

        if (existingEntry != null) {
            // SCORE AKTUALISIEREN: Überschreibe alten Score falls neuer besser ist
            if (score > existingEntry.getScore()) {
                existingEntry.setScore(score);
            }
        } else {
            // NEUEN SCORE HINZUFÜGEN: Erstelle neuen Eintrag für neuen Benutzer
            scores.add(new ScoreEntry(username, score));
        }

        // SORTIEREN: Sortiere Scores nach Höhe (beste zuerst)
        scores.sort((a, b) -> Float.compare(b.getScore(), a.getScore()));
        
        // BEGRENZEN: Behalte nur die besten MAX_SCORES
        if (scores.size > MAX_SCORES) {
            scores.truncate(MAX_SCORES);
        }
        
        // SPEICHERN: Schreibe Änderungen in Datei
        saveScores();
    }

    public Array<ScoreEntry> getScores() {
        return scores;
    }

    public Array<String> getUniqueUsernames() {
        Array<String> usernames = new Array<>();
        for (ScoreEntry entry : scores) {
            if (!usernames.contains(entry.getUsername(), false)) {
                usernames.add(entry.getUsername());
            }
        }
        return usernames;
    }

    private void saveScores() {
        // SCORES SPEICHERN: Schreibe alle Scores in JSON-Datei
        FileHandle file = Gdx.files.local(SCORES_FILE);
        file.writeString(json.toJson(scores), false);
    }

    private void loadScores() {
        // SCORES LADEN: Lade gespeicherte Scores beim Programmstart
        FileHandle file = Gdx.files.local(SCORES_FILE);
        if (file.exists()) {
            try {
                // JSON PARSE: Konvertiere Datei-Inhalt zurück zu Score-Objekten
                scores = json.fromJson(Array.class, ScoreEntry.class, file);
            } catch (Exception e) {
                Gdx.app.error("LeaderboardManager", "Error loading scores", e);
                scores = new Array<>();
            }
        }
    }
} 