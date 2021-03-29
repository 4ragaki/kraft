package `fun`.aragaki.kraft.data

sealed class CredentialException(msg: String) : Exception(msg) {
    object PixivCredentialEmptyException : CredentialException("Enter pixiv id and/or password.")
    object SauceNaoApiKeyEmptyException : CredentialException("Enter sauceNao ApiKey.")
}