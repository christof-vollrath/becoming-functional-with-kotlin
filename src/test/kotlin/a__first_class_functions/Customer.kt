package a__first_class_functions

import b__pure_functions.Contract
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

data class Customer(
        val id: Int,
        var name: String,
        var address: String,
        var state: String,
        var primaryContact: String,
        var domain: String,
        var enabled: Boolean = true,
        var contract: Contract? = null)

typealias FieldSelection<T> = (Customer) -> T
typealias CustomerFilter = (Customer) -> Boolean

fun <T> getEnabledCustomerFields(customers: List<Customer>, fieldSelection: FieldSelection<T>): List<T> =
        getFilteredCustomerFields(customers, {it.enabled}, fieldSelection)

fun <T> getFilteredCustomerFields(customers: List<Customer>, customerFilter: CustomerFilter, fieldSelection: FieldSelection<T>): List<T> =
        customers
            .filter { customerFilter(it) }
            .map(fieldSelection)

fun getCustomerById(customers: List<Customer>, id: Int): Customer? = customers.firstOrNull {it.id == id}


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

    }
})

