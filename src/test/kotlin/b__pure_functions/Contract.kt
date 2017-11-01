package b__pure_functions

import a__first_class_functions.Customer
import a__first_class_functions.allCustomers
import a__first_class_functions.getCustomerById
import a__first_class_functions.upsertCustomer
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*

data class Contract(
        val beginDate: Date,
        val endDate: Date = defaultEndDate(beginDate),
        val enabled: Boolean = true)

fun defaultEndDate(beginDate: Date): Date {
    val cal = Calendar.getInstance()
    cal.time = beginDate
    cal.add(Calendar.YEAR, 2)
    return cal.time
}

fun setContractForCustomer(customers: List<Customer>, customerId: Int, enable: Boolean): List<Customer> {
    val customer = getCustomerById(customers, customerId)
    if (customer != null && customer.contract != null) {
        return upsertCustomer(customers, customer.copy(contract = customer.contract.copy(enabled = enable)))
    } else return customers
}

fun date(year: Int, month: Int, day: Int): Date {
    val cal = Calendar.getInstance()
    cal.timeInMillis = 0
    cal.set(year, month, day)
    return cal.time
}

class ContractSpec: Spek({
    describe("some contract") {
        val contract = Contract(date(2017, 27, 10))

        it("should be created correctly") {
            contract.beginDate `should equal` date(2017, 27, 10)
            contract.endDate `should equal` date(2019, 27, 10)
            contract.enabled `should be` true
        }
    }
    describe("some contract with customer BVG") {
        val contract = Contract(date(2017, 27, 10))
        contract.enabled `should be` true // Enabled by default
        val bvg = getCustomerById(allCustomers, 1)
        val allCustomers1 = upsertCustomer(allCustomers, bvg!!.copy(contract = contract))

        on("disable contract for customer") {
            val allCustomers2 = setContractForCustomer(allCustomers1,1, false)

            it("contract should be disabled") {
                getCustomerById(allCustomers2, 1)!!.contract!!.enabled `should be` false
            }
        }
        on("disable contract for non-existing customer") {
            val allCustomers2 = setContractForCustomer(allCustomers1,-1, false)
            it("should be ignored") {
                allCustomers2 `should equal` allCustomers1
            }
        }
        on("disable contract for customer without contract") {
            val allCustomers2 = setContractForCustomer(allCustomers1,2, false)
            it("should be ignored") {
                allCustomers2 `should equal` allCustomers1
            }
        }

    }
})


