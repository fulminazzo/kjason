package it.fulminazzo.kjason

import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class KJasonParser internal constructor(private val input: InputStream) {
    private var lastRead: Token = TokenType.eof()

    internal constructor(raw: String) : this(ByteArrayInputStream(raw.toByteArray()))

    internal constructor(file: File) : this(file.inputStream())

    internal fun nextTokenSpaceless(): Token {
        var token: Token
        do token = nextToken()
        while (token.type == TokenType.SPACE)
        return token
    }

    internal fun nextToken(): Token {
        lastRead = if (input.available() > 0)
            TokenType.fromString(input.read().toChar().toString())
        else TokenType.eof()
        return lastRead
    }

    private fun matches(vararg tokenTypes: TokenType) = tokenTypes.none { it == lastRead.type }

    private fun expect(vararg tokenTypes: TokenType) {
        if (matches(*tokenTypes))
            throw ParserException.expected(tokenTypes[0], lastRead)
    }

    private fun consume(vararg tokenTypes: TokenType): Token {
        expect(*tokenTypes)
        val curr = lastRead
        nextToken()
        return curr
    }

}

