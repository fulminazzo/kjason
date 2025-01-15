package it.fulminazzo.kjason

enum class TokenType(private val regex: String) {
    // '+'
    PLUS("\\+"),
    // '-'
    MINUS("-"),
    // '.'
    DOT("\\."),
    // 'E' | 'e'
    E("E|e"),

    // '0'
    ZERO("0"),

    // '1'.'9'
    ONENINE("[1-9]"),

    // 'CR' | 'LF' | 'TAB' | 'SPACE'
    SPACE("[\r\n\t ]"),

    EOF("");

    companion object {

        @JvmStatic
        fun fromString(read: String): Token {
            for (tokenType in entries.filter { it != EOF })
                if (read.matches(Regex(tokenType.regex)))
                    return Token(tokenType, tokenType.regex)
            return eof()
        }

        fun eof(): Token {
            return Token(EOF, "")
        }

    }

}

data class Token(val type: TokenType, val value: String)
