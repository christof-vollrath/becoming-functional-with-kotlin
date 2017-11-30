package c__immutable_variables

import a__first_class_functions.*
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

data class Contact(
        override val id: Int,
        val firstName: String,
        val lastName: String,
        val email: String,
        val enabled: Boolean = true
) : Identifyable

fun upsertContact(contacts: List<Contact>, contact: Contact): List<Contact> = upsert(contacts, contact)

fun upsertContactInCustomers(customers: List<Customer>, id: Int, contact: Contact): List<Customer> {
    val customer = getCustomerById(customers, id)
    return if (customer != null) {
        val changedCustomer = customer.copy(contacts = upsertContact(customer.contacts, contact))
        upsertCustomer(customers, changedCustomer)
    } else customers
}

class ContactsSpec: Spek({
    describe("given empty contacts and a contact") {
        val contacts = emptyList<Contact>()
        val contact = Contact(1, "Hans", "Peter", "hans.peter")

        on("upsert contacts") {
            val contacts1 = upsertContact(contacts, contact)

            it("should contain contact") {
                contacts1 `should equal` listOf(contact)
            }
        }
    }

    describe("using all customers with empty and a given contact") {
        val contact = Contact(1, "Hans", "Peter", "hans.peter")

        on("upsert contacts in customers") {
            val allCustomers1 = upsertContactInCustomers(allCustomers, 2, contact)

            it("customer should have contacts") {
                getCustomerById(allCustomers1, 2)!!.contacts `should equal` listOf(contact)
            }
        }
    }

})

