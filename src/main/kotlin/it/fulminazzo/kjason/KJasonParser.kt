package it.fulminazzo.kjason

import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

class KJasonParser internal constructor(private val input: InputStream) {
    private val DIGITS = arrayOf(TokenType.ZERO, TokenType.ONENINE)

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

    private fun matches(vararg tokenTypes: TokenType) = tokenTypes.any { it.matches(lastRead) }

    private fun expect(vararg tokenTypes: TokenType) {
        if (!matches(*tokenTypes))
            throw ParserException.expected(tokenTypes[0], lastRead)
    }

    private fun consume(vararg tokenTypes: TokenType): Token {
        for (type in tokenTypes)
            if (matches(type)) {
                val curr = Token(type, lastRead.value)
                nextToken()
                return curr
            }
        throw ParserException.expected(tokenTypes[0], lastRead)
    }

    /**
     * escape := '"' | '\' | '/' | 'b' | 'f' | 'n' | 'r' | 't' | 'u' hex hex hex hex
     */
    private fun parseEscape(): String {
        if (matches(TokenType.LOW_U)) {
            consume(TokenType.LOW_U)
            return (1..4)
                .joinToString("") { parseHex() }
                .toInt(16)
                .toChar()
                .toString()
        } else return when (consume(
            TokenType.DOUBLE_QUOTE,
            TokenType.BACKSLASH,
            TokenType.FORWARDSLASH,
            TokenType.LOW_B,
            TokenType.LOW_F,
            TokenType.LOW_N,
            TokenType.LOW_R,
            TokenType.LOW_T
        ).type) {
            TokenType.DOUBLE_QUOTE -> "\""
            TokenType.BACKSLASH -> "\\"
            TokenType.FORWARDSLASH -> "/"
            TokenType.LOW_B -> "\b"
            TokenType.LOW_F -> "\u000c"
            TokenType.LOW_N -> "\n"
            TokenType.LOW_R -> "\r"
            else -> "\t"
        }
    }

    /**
     * hex := digit | 'A'-'F' | 'a'-'f'
     */
    private fun parseHex(): String = consume(*DIGITS, TokenType.AF).value

    /**
     * number := integer fraction exponent
     */
    internal fun parseNumber(): Double {
        var integer = parseInteger().toString()
        if (matches(TokenType.DOT)) integer += parseFraction()
        if (matches(TokenType.E)) integer += parseExponent()
        return integer.toDouble()
    }

    /**
     * integer := sign digit | sign onenine digits
     */
    internal fun parseInteger(): Long {
        var number: String = parseSign()
        number += if (matches(TokenType.ZERO)) consume(TokenType.ZERO).value
        else parseDigits()
        return number.toLong()
    }

    /**
     * digits := digit digits?
     */
    private fun parseDigits(): String {
        val digit = parseDigit()
        return if (matches(*DIGITS)) digit + parseDigits()
        else digit
    }

    /**
     * digit := '0' | ONENINE
     */
    private fun parseDigit(): String = consume(*DIGITS).value

    /**
     * fraction := '.' digits
     */
    private fun parseFraction(): String = consume(TokenType.DOT).value + parseDigits()

    /**
     * exponent := ('E' | 'e') sign digits
     */
    private fun parseExponent(): String = consume(TokenType.E).value + parseSign() + parseDigits()

    /**
     * sign := '' | '+' | '-'
     */
    private fun parseSign(): String {
        return if (matches(TokenType.PLUS, TokenType.MINUS))
            consume(TokenType.PLUS, TokenType.MINUS).value
        else ""
    }

}

