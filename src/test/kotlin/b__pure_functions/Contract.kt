package b__pure_functions

import a__first_class_functions.Customer
import a__first_class_functions.allCustomers
import a__first_class_functions.getCustomerById
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*

data class Contract(
        val beginDate: Date,
        var endDate: Date = defaultEndDate(beginDate),
        var enabled: Boolean = true)

fun defaultEndDate(beginDate: Date): Date {
    val cal = Calendar.getInstance()
    cal.time = beginDate
    cal.add(Calendar.YEAR, 2)
    return cal.time
}

fun setContractForCustomer(customers: List<Customer>, customerId: Int, enable: Boolean): Contract? {
    val contract = getCustomerById(customers, customerId)?.contract
    return if (contract != null) {
        contract.enabled = enable
        contract
    } else {
        null
    }
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
        getCustomerById(allCustomers, 1)!!.contract = contract

        on("disable contract for customer") {
            val result = setContractForCustomer(allCustomers,1, false)

            it("contract should be disabled") {
                contract.enabled `should be` false
                result `should be` contract
            }
        }
        on("disable contract for non-existing customer") {
            val result = setContractForCustomer(allCustomers, -1, false)
            it("should be ignored") {
                result `should be` null
            }
        }
        on("disable contract for customer without contract") {
            val result = setContractForCustomer(allCustomers, 2, false)
            it("should be ignored") {
                result `should be` null
                getCustomerById(allCustomers, 2)!!.contract `should be` null
            }
        }

    }
})


