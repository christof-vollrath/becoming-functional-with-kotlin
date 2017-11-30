package e_nonstrict_evaluation

import a__first_class_functions.Customer
import c__immutable_variables.Contact
import c__immutable_variables.upsertContact
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

fun List<Contact>.upsertContact(contact: Contact) = upsertContact(this, contact)

class NonstrictCustomersSpec: Spek({
    describe("two enabled contacts and one not enabled contact") {
        val contacts = emptyList<Contact>()
                .upsertContact(Contact(1, "Hans", "Peter", "hans.peter"))
                .upsertContact(Contact(2, "Not", "Enabled", "not.enabled", false))
                .upsertContact(Contact(3, "Müller", "Turgau", "mueller.turgau"))

        on("filter enabled customers") {
            val filteredContacts = contacts.filter { it.enabled }

            it("should contain only the enabled customers") {
                filteredContacts.size `should equal` 2
                filteredContacts `should equal`  emptyList<Contact>()
                        .upsertContact(Contact(1, "Hans", "Peter", "hans.peter"))
                        .upsertContact(Contact(3, "Müller", "Turgau", "mueller.turgau"))
            }
        }

        describe("customer with two enabled and one not enabled contacts") {
            val customer = Customer(1, "BVG", "Berlin", "Germany", "Hans", "bvg.de", contacts=contacts)
            on("enabled customers") {
                val filteredContacts = customer.enabledContacts
                it("should contain only the enabled customers") {
                    filteredContacts.size `should equal` 2
                    filteredContacts `should equal` emptyList<Contact>()
                            .upsertContact(Contact(1, "Hans", "Peter", "hans.peter"))
                            .upsertContact(Contact(3, "Müller", "Turgau", "mueller.turgau"))
                }
            }
        }
    }
})

