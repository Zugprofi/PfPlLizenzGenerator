import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Erstellt einen Pfad für eine Datei im App-Verzeichnis.
 * Gibt diesen Pfad zurück zur Verarbeitung.
 */
fun directoryPath(fileName: String): Path {
    val filePath: Path = Paths.get(
        System.getProperty("user.home"),
        ".pruefungsplaner",
        fileName
    ).also {
        Files.createDirectories(it.parent)
    }
    return filePath
}
/**
 * Lädt den Inhalt einer Datei aus dem App-Verzeichnis.
 * @param fileName Der Name der Datei, die geladen werden soll.
 * @return Gibt den Inhalt als String zurück.
 */
fun loadFromAppDirectory(fileName: String): String {
    val text = Files.readString(directoryPath(fileName))
    return text
}
/**
 * Speichert einen String in einer Datei im App-Verzeichnis.
 * @param fileName Der Name der Datei, in die der String gespeichert werden soll.
 */
fun storeToAppDirectory(fileName: String, text: String) {
    Files.writeString(directoryPath(fileName), text)
    println(directoryPath(fileName))
}
/**
 * Prüft, ob eine Datei im App-Verzeichnis existiert.
 * @param fileName Der Name der Datei, die gesucht wird.
 */
fun fileExistsInAppDirectory(fileName: String): Boolean {
    return Files.exists(directoryPath(fileName))
}
/**
 * Löscht eine Datei im App-Verzeichnis.
 * @param fileName Der Name der Datei, die gelöscht werden soll.
 */
fun deleteInAppDirectory(fileName: String) {
    Files.deleteIfExists(directoryPath(fileName))
}