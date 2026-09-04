package org.example

import deleteInAppDirectory
import fileExistsInAppDirectory
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import license.SignedLicense
import loadFromAppDirectory
import storeToAppDirectory
import java.nio.file.Path

class LicenseStorage(
    private val licenseFileName: String = "license.lic"
)
{
    private val json = Json {
        prettyPrint = true
    }
    /**
     * Liest eine beliebige Lizenzdatei (z.B. aus einem Dateidialog).
     */
    fun load(path: Path): SignedLicense {
        val text = loadFromAppDirectory(licenseFileName)
        return json.decodeFromString(text)
    }
    /**
     * Speichert eine gültige Lizenz dauerhaft im Benutzerordner.
     */
    fun store(license: SignedLicense) {
        val text = json.encodeToString(license)
        storeToAppDirectory(licenseFileName, text)
    }
    /**
     * Lädt automatisch die gespeicherte Lizenz.
     * Gibt null zurück, wenn keine vorhanden ist.
     */
    fun autoLoad(): SignedLicense? {
        if (!exists())
            return null

        val text = loadFromAppDirectory(licenseFileName)
        return json.decodeFromString(text)
    }
    /**
     * Löscht die gespeicherte Lizenz.
     */
    fun delete() {
        deleteInAppDirectory(licenseFileName)
    }
    /**
     * Prüft, ob bereits eine Lizenz gespeichert wurde.
     */
    fun exists(): Boolean =
        fileExistsInAppDirectory(licenseFileName)
}