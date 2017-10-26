package a__first_class_functions

import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

import b__pure_functions.Contract

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

fun <T> getEnabledCustomerFields(customers: List<Customer>, fieldSelection: FieldSelection<T>): List<T> =
        customers.filter({it.enabled}).map(fieldSelection)

class CustomersSpec: Spek({
    describe("some customers") {
        val customers = listOf(
                Customer(1, "BVG", "Berlin", "Germany", "Hans", "bvg.de"),
                Customer(2, "Axel Springer", "Berlin", "Germany", "Axel", "as.de", false),
                Customer(3, "Deutsche Bahn", "Franfurt", "Germany", "Dorn", "db.de")
        )

        it("getEnabledCustomerFields with function selecting name should return name of enabled customers") {
            val result = getEnabledCustomerFields(customers, { it.name })
            result `should equal` listOf("BVG", "Deutsche Bahn")
        }
        it("should return email with parametrized recipient using a closure to access outside value") {
            val recipient = "boss"
            val result = getEnabledCustomerFields(customers, { "$recipient@${it.domain}" })
            result `should equal` listOf("boss@bvg.de", "boss@db.de")
        }
    }
})
