import kotlinx.serialization.json.Json
import license.License
import license.SignedLicense
import java.security.PrivateKey
import java.security.Signature
import java.util.Base64
import kotlin.uuid.ExperimentalUuidApi

class LicenseGenerator(
    private val privateKey: PrivateKey
) {

    private val json = Json {
        prettyPrint = false
        encodeDefaults = true
    }
    @OptIn(ExperimentalUuidApi::class)
    fun createLicense(
        license: License
    ): SignedLicense {
        val payload =
            json.encodeToString(
                License.serializer(),
                license
            )
        val signature =
            Signature.getInstance("Ed25519")
        signature.initSign(privateKey)
        signature.update(payload.toByteArray())
        val signed =
            signature.sign()
        return SignedLicense(
            license,
            Base64.getEncoder()
                .encodeToString(signed)
        )
    }
}