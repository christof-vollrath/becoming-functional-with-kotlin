package h_functional_oop

import org.amshove.kluent.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

interface EMailProvider {
    fun sendEmail(adress: String, subject: String, body: String)
}

class EmailService(val emailProvider: EMailProvider) {
    fun send(email: Email) = with(email) {
        emailProvider.sendEmail(adress, subject, body)
    }
}

data class Email(val adress: String, val subject: String, val body: String)

class FunctionOopSpec : Spek({
    describe("send a plain email") {
        val emailProvider = mock(EMailProvider::class)
        val email = Email("christof@taobits.net", "Test subject", "This is a test")
        val emailService = EmailService(emailProvider)
        on("send email") {
            emailService.send(email)
            it("should be send (mock)") {
                Verify on emailProvider that emailProvider.sendEmail("christof@taobits.net", "Test subject", "This is a test") was called
            }
        }
    }
})
