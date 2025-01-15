package it.fulminazzo.kjason

import spock.lang.Specification

class KJasonTest extends Specification {

    def 'write of #target should return #expected'() {
        when:
        def actual = KJason.write(target)

        then:
        actual == expected

        where:
        target                               || expected
        true                                 || 'true'
        false                                || 'false'
        null                                 || 'null'
        [1: true, 2: 10, 3: 'Hello, World!'] || '{"1": true, "2": 10, "3": "Hello, World!"}'
        [1, 2, 3]                            || '[1, 2, 3]'
        [true, false].toArray()              || '[true, false]'
        'Hello, world!'                      || '"Hello, world!"'
        10                                   || '10'
        10.12                                || '10.12'
        new MockClass()                      || '{"first": 1, "second": 2, "third": 3}'
    }

    def 'parse of #target did not return the expected object'() {
        when:
        def parsed = KJason.parse(target)

        then:
        parsed == ['hello': 'world!']

        where:
        target << [
                '{"hello": "world!"}',
                new File('build/resources/test/test_file.json'),
                new File('build/resources/test/test_file.json').newInputStream(),
        ]
    }

    def 'consume with invalid token types should throw exception'() {
        given:
        def kjason = new KJason('')

        and:
        def message = ParserException.expected(TokenType.ZERO, new Token(TokenType.EOF, '')).message

        when:
        kjason.consume(TokenType.ZERO)

        then:
        def e = thrown(ParserException)
        e.message == message
    }

    def 'parseValue of #value should return #expected'() {
        given:
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseValue()

        then:
        actual == expected

        where:
        value                                    || expected
        'true'                                   || true
        'false'                                  || false
        'null'                                   || null
        '{"1": "one", "2": "two", "3": "three"}' || ['1': 'one', '2': 'two', '3': 'three']
        '[1, 2, 3]'                              || [1, 2, 3]
        '"Hello, World!"'                        || 'Hello, World!'
        '1'                                      || 1
        '1.0'                                    || 1.0
    }

    def 'parseObject of #value should return #expected'() {
        given:
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseObject()

        then:
        actual == expected

        where:
        value                              || expected
        '{}'                               || [:]
        '{\r\t\n \r\t\n \r\t\n \r\t\n }'   || [:]
        '{"one": 1, "two": 2, "three": 3}' || ['one': 1, 'two': 2, 'three': 3]
        '{"Hello": true, "world": false}'  || ['Hello': true, 'world': false]
        '{"1" : 2}'                        || ['1': 2]
    }

    def 'parseArray of #value should return #expected'() {
        given:
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseArray()

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
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseString()

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
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseCharacter()

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
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseEscape()

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
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseNumber()

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
        def kjason = new KJason(value)

        and:
        kjason.nextToken()

        when:
        def actual = kjason.parseInteger()

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

    def 'nextToken of #value should return #expected'() {
        given:
        def kjason = new KJason(value)

        when:
        def read = kjason.nextToken().type

        then:
        read == expected
        kjason.lastRead.type == expected

        where:
        value || expected
        '0'   || TokenType.ZERO
        ''    || TokenType.EOF
    }

    class MockClass {
        int first = 1
        int second = 2
        int third = 3

    }

}
