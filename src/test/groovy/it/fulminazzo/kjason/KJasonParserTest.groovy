package it.fulminazzo.kjason

import spock.lang.Specification

class KJasonParserTest extends Specification {

    def 'parseObject of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        and:
        tokenizer.nextToken$KJason()

        when:
        def actual = tokenizer.parseObject$KJason()

        then:
        actual == expected

        where:
        value                              || expected
        '{}'                               || [:]
        '{\r\t\n \r\t\n \r\t\n \r\t\n }'   || [:]
        '{1: "one", 2: "two", 3: "three"}' || [1: 'one', 2: 'two', 3: 'three']
        '{"Hello": true, "world": false}'  || ['Hello': true, 'world': true]
        '{1 : 2}'                          || [1: 2]
    }

    def 'parseArray of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        and:
        tokenizer.nextToken$KJason()

        when:
        def actual = tokenizer.parseArray$KJason()

        then:
        actual == expected

        where:
        value                            || expected
        '[]'                             || [].toArray()
        '[\r\t\n \r\t\n \r\t\n \r\t\n ]' || [].toArray()
        '[1, 2, 3]'                      || [1, 2, 3].toArray()
        '["Hello"]'                      || ['Hello'].toArray()
    }

    def 'parseString of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        and:
        tokenizer.nextToken$KJason()

        when:
        def actual = tokenizer.parseString$KJason()

        then:
        actual == expected

        where:
        value      || expected
        '\"\"'     || ''
        '\"a\"'    || 'a'
        '\"\\\"\"' || '\"'
    }

    def 'parseCharacter of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        and:
        tokenizer.nextToken$KJason()

        when:
        def actual = tokenizer.parseCharacter$KJason()

        then:
        actual == expected

        where:
        value     || expected
        'a'       || 'a' as char
        'A'       || 'A' as char
        '\\u4321' || '䌡' as char
    }

    def 'parseEscape of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        and:
        tokenizer.nextToken$KJason()

        when:
        def actual = tokenizer.parseEscape()

        then:
        actual == expected

        where:
        value   || expected
        '\"'    || '\"'
        '\\'    || '\\'
        '/'     || '/'
        'b'     || '\b'
        'f'     || '\f'
        'n'     || '\n'
        'r'     || '\r'
        't'     || '\t'
        'u4321' || '䌡'
    }

    def 'parseNumber of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        and:
        tokenizer.nextToken$KJason()

        when:
        def actual = tokenizer.parseNumber$KJason()

        then:
        actual == expected

        where:
        value      || expected
        '10'       || 10.0
        '10.12'    || 10.12
        '10E2'     || 1000.0
        '10E-2'    || 0.1
        '10.12E2'  || 1012.0
        '10.12E-2' || 0.1012 as double
    }

    def 'parseInteger of #value should return #expected'() {
        given:
        def tokenizer = new KJasonParser(value)

        and:
        tokenizer.nextToken$KJason()

        when:
        def actual = tokenizer.parseInteger$KJason()

        then:
        actual == expected

        where:
        value         || expected
        '0'           || 0
        '+0'          || 0
        '-0'          || 0
        '1'           || 1
        '+1'          || 1
        '-1'          || -1
        '1234567890'  || 1234567890
        '+1234567890' || 1234567890
        '-1234567890' || -1234567890
    }

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
