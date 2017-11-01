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


tailrec fun <T: Identifyable> upsertRec(list: List<out T>, change: T, found: Boolean = false, interimResult: MutableList<T> = ArrayList(list.size + 1)): List<T> {
    val head = list.firstOrNull()
    if (head == null) {
        if (found) return interimResult
        else return interimResult + change
    } else {
        val found = head.id == change.id
        if (found) {
            interimResult += change
            return upsertRec(list.drop(1), change, found, interimResult)
        } else {
            interimResult += head
            return upsertRec(list.drop(1), change, found, interimResult)
        }
        // Not efficent at all since drop creeates a new list and copies the tail into it
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
