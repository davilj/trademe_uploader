/*
 * This Spock specification was auto generated by running the Gradle 'init' task
 * by 'daniev' at '4/13/17 8:28 PM' with Gradle 3.2.1
 *
 * @author daniev, @date 4/13/17 8:28 PM
 */

import spock.lang.Specification

class LibraryTest extends Specification{
    def "someLibraryMethod returns true"() {
        setup:
        Library lib = new Library()
        when:
        def result = lib.someLibraryMethod()
        then:
        result == true
    }
}
