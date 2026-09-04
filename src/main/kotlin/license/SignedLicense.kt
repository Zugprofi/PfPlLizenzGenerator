package license

import kotlinx.serialization.Serializable

@Serializable
data class SignedLicense(
    val payload: License,
    val signature: String
)