package it.fulminazzo.kjason

class ParserException private constructor(message: String) : RuntimeException(message) {

    companion object {

        fun expected(tokenType: TokenType, token: Token): ParserException {
            return ParserException("Expected $tokenType but got ${token.value}")
        }

    }

}
