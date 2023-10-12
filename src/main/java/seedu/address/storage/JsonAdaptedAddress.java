package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;

/**
 * Jackson-friendly version of {@link Address}.
 */
public class JsonAdaptedAddress {

    private final String value;
    private final boolean isEmptyAddress;

    /**
     * Constructs a {@code JsonAdaptedAddress} with the given {@code value} and {@code isEmptyAddress}.
     * @param value
     * @param isEmptyAddress
     */
    @JsonCreator
    public JsonAdaptedAddress(@JsonProperty("value") String value,
            @JsonProperty("isEmptyAddress") boolean isEmptyAddress) {
        this.value = value;
        this.isEmptyAddress = isEmptyAddress;
    }

    /**
     * Converts a given {@code Address} into this class for Jackson use.
     */
    public JsonAdaptedAddress(Address address) {
        value = address.value;
        isEmptyAddress = address.isEmptyAddress();
    }

    public String getValue() {
        return value;
    }

    public boolean isEmptyAddress() {
        return isEmptyAddress;
    }

    /**
     * Converts this Jackson-friendly adapted address object into the model's {@code Address} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted address.
     */
    public Address toModelType() throws IllegalValueException {
        if (isEmptyAddress) {
            return Address.EMPTY_ADDRESS;
        }

        if (!Address.isValidAddress(value)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }

        return new Address(value);
    }

}