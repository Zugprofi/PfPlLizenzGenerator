package license

enum class Plan {
    Standard,
    Erweitert,
    Unlimited
}
enum class Feature {
    Excel,
}
enum class LicenseStatus {
    valid,
    invalid,
    future,
    expired,
    network
}