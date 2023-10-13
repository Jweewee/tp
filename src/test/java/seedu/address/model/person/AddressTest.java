package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidAddress = "";
        assertThrows(IllegalArgumentException.class, () -> new Address(invalidAddress));
    }

    @Test
    public void isValidAddress() {
        // null address
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // invalid addresses
        assertFalse(Address.isValidAddress("")); // empty string
        assertFalse(Address.isValidAddress(" ")); // spaces only

        // valid addresses
        assertTrue(Address.isValidAddress("Blk 456, Den Road, #01-355"));
        assertTrue(Address.isValidAddress("-")); // one character
        assertTrue(Address.isValidAddress("Leng Inc; 1234 Market St; San Francisco CA 2349879; USA")); // long address
    }

    @Test
    public void isEmptyAddress() {
        // empty address
        assertTrue(Address.EMPTY_ADDRESS.isEmptyAddress());

        // non-empty address
        assertFalse(new Address("Some address").isEmptyAddress());

        // address with "-" value
        assertFalse(new Address("-").isEmptyAddress());
    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");
        Address address_with_value_of_empty_address = new Address(EmptyAddress.DUMMY_VALUE_FOR_EMPTY_ADDRESS);

        // same values -> returns true
        assertTrue(address.equals(new Address("Valid Address")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("Other Valid Address")));

        // empty address -> returns false
        assertFalse(address.equals(Address.EMPTY_ADDRESS));

        // valid address with same value as empty address should not be equal to empty address
        assertFalse(address_with_value_of_empty_address.equals(Address.EMPTY_ADDRESS));
    }
}
