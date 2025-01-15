package it.fulminazzo.kjason

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