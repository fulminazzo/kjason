package it.fulminazzo.kjason

import spock.lang.Specification

class TokenizerTest extends Specification {

    def 'nextToken of #value should return #expected'() {
        given:
        def tokenizer = new Tokenizer(value)

        when:
        def read = tokenizer.nextToken$KJason()

        then:
        read == expected
        tokenizer.lastRead == expected

        where:
        value || expected
        '0'   || TokenType.ZERO
        ''    || TokenType.EOF
    }

    def 'fromString of #value should return #expected'() {
        when:
        def actual = TokenType.fromString(value)

        then:
        actual == expected

        where:
        value || expected
        '-'   || TokenType.MINUS
        '0'   || TokenType.ZERO
        '1'   || TokenType.ONENINE
        '2'   || TokenType.ONENINE
        '3'   || TokenType.ONENINE
        '4'   || TokenType.ONENINE
        '5'   || TokenType.ONENINE
        '6'   || TokenType.ONENINE
        '7'   || TokenType.ONENINE
        '8'   || TokenType.ONENINE
        '9'   || TokenType.ONENINE
        ' '   || TokenType.SPACE
        ''    || TokenType.EOF
    }

}
