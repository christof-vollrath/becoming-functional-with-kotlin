package a__first_class_functions

import b__pure_functions.Contract
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*

data class Customer(
        val id: Int,
        val name: String,
        val address: String,
        val state: String,
        val primaryContact: String,
        val domain: String,
        val enabled: Boolean = true,
        val contract: Contract? = null)

typealias FieldSelection<T> = (Customer) -> T
typealias CustomerFilter = (Customer) -> Boolean

fun <T> getEnabledCustomerFields(customers: List<Customer>, fieldSelection: FieldSelection<T>): List<T> =
        getFilteredCustomerFields(customers, {it.enabled}, fieldSelection)

fun <T> getFilteredCustomerFields(customers: List<Customer>, customerFilter: CustomerFilter, fieldSelection: FieldSelection<T>): List<T> =
        customers
            .filter(customerFilter)
            .map(fieldSelection)

fun getCustomerById(customers: List<Customer>, id: Int): Customer? = customers.firstOrNull {it.id == id}

fun upsertCustomer(customers: List<Customer>, changedCustomer: Customer): List<Customer> {
    val result = ArrayList<Customer>(customers.size + 1)
    var found = false
    for(customer in customers) {
        result +=
            if (customer.id == changedCustomer.id) {
                found = true
                changedCustomer
            }
            else customer
    }
    if (! found) result += changedCustomer
    return result
}

val allCustomers = listOf(
        Customer(1, "BVG", "Berlin", "Germany", "Hans", "bvg.de"),
        Customer(2, "Axel Springer", "Berlin", "Germany", "Axel", "as.de", false),
        Customer(3, "Deutsche Bahn", "Franfurt", "Germany", "Dorn", "db.de")
)

class CustomersSpec: Spek({
    describe("using all customers") {
        it("getEnabledCustomerFields with function selecting name should return name of enabled customers") {
            val result = getEnabledCustomerFields(allCustomers) { it.name }
            result `should equal` listOf("BVG", "Deutsche Bahn")
        }
        it("should return email with parametrized recipient using a closure to access outside value") {
            val recipient = "boss"
            val result = getEnabledCustomerFields(allCustomers) { "$recipient@${it.domain}" }
            result `should equal` listOf("boss@bvg.de", "boss@db.de")
        }
        on("getCustomerById with existing id") {
            val result = getCustomerById(allCustomers, 1)
            it("should return customer") {
                result `should equal` allCustomers[0]
            }
        }
        on("getCustomerById with non-existing id") {
            val result = getCustomerById(allCustomers, 0)
            it("should return customer") {
                result `should be` null
            }
        }
        on("upsertCustomer with existing id") {
            val changedCustomer = Customer(3, "Deutsche Bahn", "Franfurt", "Germany", "Dorn", "db.de", false)
            val changedCustomers = upsertCustomer(allCustomers, changedCustomer)
            it("should return customers with changed customer") {
                changedCustomers.size `should equal` 3
                getCustomerById(changedCustomers, 3)?.enabled `should be` false
            }
        }
        on("upsertCustomer with new id") {
            val changedCustomer = Customer(4, "Deutsche Bank", "Franfurt", "Germany", "Hass", "deutsche-bank.de", false)
            val changedCustomers = upsertCustomer(allCustomers, changedCustomer)
            it("should return customers with changed customer") {
                changedCustomers.size `should equal` 4
                getCustomerById(changedCustomers, 4) `should equal` changedCustomer
            }
        }

    }
})


