package it.fulminazzo.kjason

import spock.lang.Specification

class TokenTypeTest extends Specification {

    def 'fromString of #value should return #expected'() {
        when:
        def actual = TokenType.fromString(value).type

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
        ' '   || TokenType.WS
        ''    || TokenType.EOF
    }

}
