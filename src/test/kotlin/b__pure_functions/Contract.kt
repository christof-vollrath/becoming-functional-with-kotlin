package b__pure_functions

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
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
})

