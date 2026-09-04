package license

import kotlinx.serialization.json.Json
import org.apache.commons.net.ntp.NTPUDPClient
import java.net.InetAddress
import java.security.*
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.*

class LicenseValidator {
    private val json =
        Json {
            ignoreUnknownKeys = true
            prettyPrint = false
            encodeDefaults = true
        }
    /**
     * @return 0 Lizenz gültig;
     * 3 Lizenz ungültig;
     * 1 Lizenz abgelaufen;
     * 2 Kein Netzwerk
     */
    fun validate(
        signedLicense: SignedLicense
    ): LicenseStatus {
        // 1. Kryptografische Prüfung
        if (!verifySignature(signedLicense)) {
            return LicenseStatus.invalid
        }
        // 2. Ablaufdatum prüfen über Google Time Server
        val startDate = try {
            Date.from(
                Instant.parse(
                    signedLicense.payload.validFrom
                )
            )
        } catch (e: Exception) {
            return LicenseStatus.invalid
        }
        val endDate = try {
            Date.from(
                Instant.parse(
                    signedLicense.payload.expiresAt
                )
            )
        } catch (e: Exception) {
            return LicenseStatus.invalid
        }
        return licenseDateCheck(startDate, endDate)
    }
    private fun licenseDateCheck(
        licenseStartDate: Date,
        licenseEndDate: Date
    ): LicenseStatus {
        val timeServer = "time.google.com"
        val client = NTPUDPClient()
        client.defaultTimeout = 5000
        return try {
            val hostAddr =
                InetAddress.getByName(timeServer)
            val info =
                client.getTime(hostAddr)
            info.computeDetails()
            val googleTime =
                Date(
                    info.message
                        .transmitTimeStamp
                        .time
                )
            if (licenseStartDate.after(googleTime)) {
                LicenseStatus.future
            } else if (licenseEndDate.before(googleTime)) {
                LicenseStatus.expired
            } else {
                LicenseStatus.valid
            }
        } catch (e: Exception) {
            LicenseStatus.network
        } finally {
            client.close()
        }
    }
    private fun verifySignature(
        signedLicense: SignedLicense
    ): Boolean {
        val publicKey =
            loadPublicKey()
        val payload =
            json.encodeToString(
                License.serializer(),
                signedLicense.payload
            )
        val verifier =
            Signature.getInstance("Ed25519")
        verifier.initVerify(publicKey)

        verifier.update(
            payload.toByteArray()
        )
        return verifier.verify(
            Base64.getDecoder()
                .decode(
                    signedLicense.signature
                )
        )
    }
    private fun loadPublicKey(): java.security.PublicKey {
        val bytes =
            Base64.getDecoder()
                .decode(
                    PublicKey.PUBLIC_KEY
                )
        val spec =
            X509EncodedKeySpec(bytes)
        return KeyFactory
            .getInstance("Ed25519")
            .generatePublic(spec)
    }
}