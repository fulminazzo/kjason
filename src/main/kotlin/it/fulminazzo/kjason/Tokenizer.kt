package it.fulminazzo.kjason

import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream

class Tokenizer(private val input: InputStream) {
    private var lastRead: TokenType = TokenType.EOF

    private constructor(raw: String) : this(ByteArrayInputStream(raw.toByteArray()))

    private constructor(file: File) : this(file.inputStream())

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

