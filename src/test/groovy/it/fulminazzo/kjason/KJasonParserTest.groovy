package it.fulminazzo.kjason

import spock.lang.Specification

class KJasonParserTest extends Specification {

    def 'nextTokenSpaceless should ignore all the spaces before a Token'() {
        given:
        def tokenizer = new KJasonParser('        0')

        when:
        def read = tokenizer.nextTokenSpaceless$KJason().type

        then:
        read == TokenType.ZERO
    }

    def 'nextToken of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        when:
        def read = tokenizer.nextToken$KJason().type

        then:
        read == expected
        tokenizer.lastRead.type == expected

        where:
        value || expected
        '0'   || TokenType.ZERO
        ''    || TokenType.EOF
    }

}
