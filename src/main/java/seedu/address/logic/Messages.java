package seedu.address.logic;

import static seedu.address.model.insurance.Insurance.MAX_INSURANCE_COUNT;
import static seedu.address.model.person.Tag.MAXIMUM_TAGS_PER_PERSON;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The customer index provided is invalid";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d customers listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_NO_PERSON_FOUND = "No customer found!";

    public static final String MESSAGE_TAG_COUNT_EXCEED =
            String.format("Command will cause number of tags to exceed the limit of %d.", MAXIMUM_TAGS_PER_PERSON);

    public static final String MESSAGE_INSURANCE_COUNT_EXCEED =
            String.format("Command will cause number of insurance to exceed the limit of %d.", MAX_INSURANCE_COUNT);

    public static final String MESSAGE_PERSON_NOT_CHANGED =
            "Information provided in the command does not change the targeted customer.";

    public static final String MESSAGE_DUPLICATE_PERSON = "This customer already exists in the address book";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::getDisplayMessage).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\nName: ")
                .append(person.getName())
                .append("    |    Phone: ")
                .append(person.getPhone())
                .append("    |    Email: ")
                .append(person.getEmail())
                .append("\nAddress: ")
                .append(person.getAddress())
                .append("\nPriority: ")
                .append(person.getPriority());

        addTagString(builder, person);
        addInsuranceString(builder, person);
        addRemarkString(builder, person);
        builder.append("\nAppointment: ")
                .append(person.getAppointment());

        return builder.toString();
    }

    private static void addTagString(StringBuilder builder, Person person) {
        builder.append("\nTags: ");
        if (person.getTagsCount() == 0) {
            builder.append("-");
        } else {
            person.getTags().forEach(builder::append);
        }
    }

    private static void addInsuranceString(StringBuilder builder, Person person) {
        builder.append("\nInsurances: ");
        if (person.getInsurancesCount() == 0) {
            builder.append("-");
        } else {
            person.getInsurances().forEach(builder::append);
        }
    }

    private static void addRemarkString(StringBuilder builder, Person person) {
        builder.append("\nRemarks: ");
        if (person.hasRemark()) {
            builder.append(person.getRemark());
        } else {
            builder.append("-");
        }
    }
}
