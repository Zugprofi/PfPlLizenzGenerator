import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

fun generateKeys() {

    val generator =
        KeyPairGenerator.getInstance("Ed25519")

    val keyPair =
        generator.generateKeyPair()

    println(
        "PRIVATE:"
    )
    println(
        Base64.getEncoder()
            .encodeToString(
                keyPair.private.encoded
            )
    )


    println(
        "PUBLIC:"
    )
    println(
        Base64.getEncoder()
            .encodeToString(
                keyPair.public.encoded
            )
    )
}

fun loadPrivateKey(
    base64: String
): PrivateKey {

    val bytes =
        Base64.getDecoder()
            .decode(base64)


    val spec =
        PKCS8EncodedKeySpec(bytes)


    return KeyFactory
        .getInstance("Ed25519")
        .generatePrivate(spec)
}