package d__recursion

import a__first_class_functions.Customer
import a__first_class_functions.Identifyable
import a__first_class_functions.allCustomers
import a__first_class_functions.getCustomerById
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*


tailrec fun <T: Identifyable> upsertRec(list: List<T>, change: T, found: Boolean = false, interimResult: List<T> = listOf()): List<T> {
    val head = list.firstOrNull()
    return if (head == null) {
        if (found) interimResult
        else interimResult + change
    } else {
        val foundId = head.id == change.id
        upsertRec(list.drop(1), change, foundId,
                if (foundId) {
                    interimResult + change
                } else {
                    interimResult + head
                })
        // Not efficent at all since drop creates a new list and copies the tail into it
        // For Kotlin lists recursion seams not to be an appropriate approach
    }
}
fun upsertCustomerRec(customers: List<Customer>, changedCustomer: Customer): List<Customer> = upsertRec(customers, changedCustomer)

class RecursiveCustomersSpec: Spek({
    describe("using all customers") {
        on("upsertCustomerRec with existing id") {
            val changedCustomer = Customer(3, "Deutsche Bahn", "Franfurt", "Germany", "Dorn", "db.de", false)
            val changedCustomers = upsertCustomerRec(allCustomers, changedCustomer)
            it("should return customers with changed customer") {
                changedCustomers.size `should equal` 3
                getCustomerById(changedCustomers, 3)?.enabled `should be` false
            }
        }
        on("upsertCustomerRec with new id") {
            val changedCustomer = Customer(4, "Deutsche Bank", "Franfurt", "Germany", "Hass", "deutsche-bank.de", false)
            val changedCustomers = upsertCustomerRec(allCustomers, changedCustomer)
            it("should return customers with changed customer") {
                changedCustomers.size `should equal` 4
                getCustomerById(changedCustomers, 4) `should equal` changedCustomer
            }
        }

    }
})
