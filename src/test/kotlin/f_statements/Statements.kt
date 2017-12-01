package f_statements

import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.coroutines.experimental.buildSequence

class StatementsSpec : Spek({
    describe("for loop is not a statement (expression) in kotlin") {
//        val x = for(i in 0..10) { // Compiler Error: "For is not an expression, and only expressions are allowed here
//            println(i)
//        }
    }
    describe("forEach ") {
        val x = (0..5).forEach { println(it) }
        it ("should return Unit") {
            x `should equal` Unit
        }
    }
    describe("yield can be used to create a sequence") {
        on ("building a sequence of ints") {
            val seq = buildSequence {
                for (i in 0..5)
                    yield(i * 2)
            }
            it ("should contain that sequence") {
                seq.toList() `should equal` listOf(0, 2, 4, 6, 8, 10)
            }
        }
    }
})
