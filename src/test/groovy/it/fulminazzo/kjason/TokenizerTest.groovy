package it.fulminazzo.kjason

import spock.lang.Specification

class TokenizerTest extends Specification {

    def 'nextTokenSpaceless should ignore all the spaces before a Token'() {
        given:
        def tokenizer = new Tokenizer('        0')

        when:
        def read = tokenizer.nextTokenSpaceless$KJason()

        then:
        read == TokenType.ZERO
    }

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

}
