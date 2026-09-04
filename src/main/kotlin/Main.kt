import license.Feature
import license.License
import license.Plan
import license.LicenseValidator
import org.example.LicenseStorage
import java.util.UUID

fun main() {
    val privateKey =
        loadPrivateKey(
            PrivateKeyStore.PRIVATE_KEY
        )
    val storage = LicenseStorage()
    val generator = LicenseGenerator(privateKey)
    val validator = LicenseValidator()
    val mode = 1
    if (mode == 1) {
        val license =
            generator.createLicense(
                License(
                    licenseId = UUID.randomUUID().toString(),
                    licenseHolder = "Heinrich-Schliemann-Oberschule",
                    email = "till@wetzlich.me",
                    plan = Plan.Standard,
                    validFrom = "2026-07-01T00:00:00Z", //YYYY-MM-DD
                    expiresAt = "2026-09-01T00:00:00Z",
                    features = listOf(Feature.Excel)
                )
            )
        storage.store(license)
        println("Speichern erfolgreich!")
        println("Beginne laden der Lizenz...")
        val signedLicense = storage.autoLoad()
        if (signedLicense != null) {
            println(validator.validate(signedLicense))
        }
    }
    else if (mode == 3) {

    }
    else {
        generateKeys()
    }
}