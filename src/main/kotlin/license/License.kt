package license

import kotlinx.serialization.Serializable

@Serializable
data class License(
    val licenseId: String,
    val licenseHolder: String,
    val email: String,
    val plan: Plan,
    val validFrom: String,
    val expiresAt: String,
    val features: List<Feature>
)