package g_pattern_matching

import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFailsWith

enum class Color { RED, BLUE, GREEN }

sealed class Mood
class Happy : Mood()
class Sad : Mood()
class NoPartOfMood

open class ProgramingLanguage // Intentionally not sealed
class Fortran : ProgramingLanguage()
class Cobol : ProgramingLanguage()
class Lisp : ProgramingLanguage()
class Kotlin : ProgramingLanguage()

class PatternMatchingPlaygroundSpec : Spek({
    describe("when expression must be exhaustive") {
        val intMatcher = { n: Int -> when (n) {
                1 -> "one"
                2 -> "two"
                else -> "unkown!"  // Without 'else' compiler error: "'when' expression must be exhaustive, add necessary 'else' branch"
            }
        }
        on("when enum not matched") {
            val intString = intMatcher(42)
            it ("should return else match") {
                intString `should equal` "unkown!"
            }
        }
    }
    describe( "enums can be exhaustively handled without else") {
        val germanColorMatcher = { c: Color -> when(c) {
                Color.RED -> "rot"
                Color.BLUE -> "blau"
                Color.GREEN -> "grün"
            }
        }
        on("when enum matched") {
            val germanColor = germanColorMatcher(Color.RED)
            it ("should return german word") {
                germanColor `should equal` "rot"
            }
        }
    }
    describe( "sealed classes can be exhaustively handled without else") {
        val moodMatcher = { mood: Mood -> when(mood) {
            is Happy -> "happy"
            is Sad -> "sad"
        }
        }
        on("when sealed class matched") {
            val moodString = moodMatcher(Happy())
            it ("should return happy") {
                moodString `should equal` "happy"
            }
        }
        on("class not subclass of sealed class causes compiler error") {
//            val moodString = moodMatcher(NoPartOfMood()) // Compiler Error: Type mismatch, required 'Mood'
        }
    }
    describe("when expression must be exhaustive for open classes") {
        val progLangMatcher = { l: ProgramingLanguage -> when (l) {
            is Fortran -> "Fortran"
            is Cobol -> "Cobol"
            is Lisp -> "Lisp"
            else -> "Future Language"  // Without 'else' compiler error: "'when' expression must be exhaustive, add necessary 'else' branch"
        }
        }
        on("when enum not matched") {
            val progLangString = progLangMatcher(Kotlin())
            it ("should return else match") {
                progLangString `should equal` "Future Language"
            }
        }
    }
    describe("different matchers can be mixed") {
        val isPrime = { n: Int -> when (n) {
                0 -> throw IllegalArgumentException("0 can't be prime")
                1 -> false
                2, 3, 5, 7 -> true
                4, 6 -> false
                in 8..10 -> false
                else -> when {
                    (n < 0) -> throw IllegalArgumentException("Negative numbers can't be prime")
                    else -> TODO()
                }
            }
        }
        on("when n matched") {
            val p = isPrime(3)
            it ("should return matched case") {
                p `should equal` true
            }
        }
        on("when n not matched") {
            assertFailsWith<NotImplementedError> { isPrime(23) }
        }
    }
})
