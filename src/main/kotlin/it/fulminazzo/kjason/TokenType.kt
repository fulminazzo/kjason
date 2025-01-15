package it.fulminazzo.kjason

enum class TokenType(private val regex: String) {
    // 'A'-'F' | 'a'-'f'
    AF("[A-Fa-f]"),

    // '+'
    PLUS("\\+"),
    // '-'
    MINUS("-"),
    // '.'
    DOT("\\."),

    // '0'
    ZERO("0"),

    // '1'-'9'
    ONENINE("[1-9]"),

    // 'CR' | 'LF' | 'TAB' | 'SPACE'
    SPACE("[\r\n\t ]"),

    /*
        COMPARISONS TOKEN
     */
    // 'E' | 'e'
    E("E|e"),
    LOW_B("b"),
    LOW_F("f"),
    LOW_N("n"),
    LOW_R("r"),
    LOW_T("t"),
    LOW_U("u"),

    DOUBLE_QUOTE("\""),
    BACKSLASH("\\"),
    FORWARDSLASH("/"),

    EOF("");

    fun matches(token: Token): Boolean {
        return regex.toRegex().matches(token.value)
    }

    companion object {

        @JvmStatic
        fun fromString(read: String): Token {
            for (tokenType in entries.filter { it != EOF })
                if (read.matches(Regex(tokenType.regex)))
                    return Token(tokenType, read)
            return eof()
        }

        fun eof(): Token {
            return Token(EOF, "")
        }

    }

}

data class Token(val type: TokenType, val value: String)
