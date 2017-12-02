package g_pattern_matching

import a__first_class_functions.Customer
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertFailsWith


fun createCustomer(name: String?, state: String?, domain: String?): Customer =
    when {
        name == null -> throw IllegalArgumentException("Name should not be null")
        state == null -> throw IllegalArgumentException("State should not be null")
        domain == null -> throw IllegalArgumentException("Domain should not be null")
        else -> Customer(id=0, name=name, state=state, domain=domain)
    }

class PatternMatchingSpec : Spek({
    describe("creating a customer with validation") {
        on("creating a customer with all fields") {
            val customer = createCustomer("Christof", "Germany", "taobits.net")
            it("should succeed") {
                customer `should equal` Customer(0, "Christof", state="Germany", domain="taobits.net")
            }
        }
        it("creating a customer with no name should throw IllegalArgumentException") {
            assertFailsWith<IllegalArgumentException> {
                createCustomer(null, "Germany", "taobits.net")
            }
        }
    }
})
