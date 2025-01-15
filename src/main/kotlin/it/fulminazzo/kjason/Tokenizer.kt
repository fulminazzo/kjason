package it.fulminazzo.kjason

import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class Tokenizer internal constructor(private val input: InputStream) {
    private var lastRead: TokenType = TokenType.EOF

    internal constructor(raw: String) : this(ByteArrayInputStream(raw.toByteArray()))

    internal constructor(file: File) : this(file.inputStream())

    internal fun nextTokenSpaceless(): TokenType {
        var token: TokenType
        do token = nextToken()
        while (token == TokenType.SPACE)
        return token
    }

    internal fun nextToken(): TokenType {
        return if (input.available() > 0)
            TokenType.fromString(input.read().toChar().toString())
        else TokenType.EOF
    }

}

enum class TokenType(private val regex: String) {
    // '-'
    MINUS("-"),

    // '0'
    ZERO("0"),

    // '1'.'9'
    ONENINE("[1-9]"),

    // 'CR' | 'LF' | 'TAB' | 'SPACE'
    SPACE("[\r\n\t ]"),

    EOF("");

    companion object {

        @JvmStatic
        fun fromString(read: String): TokenType {
            for (tokenType in entries.filter { it != EOF })
                if (read.matches(Regex(tokenType.regex)))
                    return tokenType
            return EOF
        }

    }

}

