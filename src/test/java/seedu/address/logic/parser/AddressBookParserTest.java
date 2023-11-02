package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_CAR;
import static seedu.address.logic.commands.CommandTestUtil.VALID_INSURANCE_HEALTH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PRIORITY_LOW;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.InsuranceCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.PriorityCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.TagCommand.UpdatePersonTagsDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.insurance.Insurance;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Person;
import seedu.address.model.person.Tag;
import seedu.address.model.person.predicate.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicate.PersonContainsKeywordsPredicate;
import seedu.address.model.priority.Priority;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().withAppointment(Appointment.getDefaultEmptyAppointment()).build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().withTags("dad").build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        NameContainsKeywordsPredicate nameContainsKeywordsPredicate = new NameContainsKeywordsPredicate(keywords);

        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(List.of(nameContainsKeywordsPredicate));

        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " n/" + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(predicate), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_tag_returnsTagCommand() throws Exception {
        Index testIndex = INDEX_FIRST_PERSON;

        Set<Tag> testSetToAdd = Set.of(new Tag("tagToAdd"));
        Set<Tag> testSetToDelete = Set.of(new Tag("tagToDelete"));
        UpdatePersonTagsDescriptor updatePersonTagsDescriptor =
                new UpdatePersonTagsDescriptor(testSetToAdd, testSetToDelete);

        TagCommand expectedCommand = new TagCommand(testIndex, updatePersonTagsDescriptor);
        Command actualCommand = parser.parseCommand(PersonUtil.getTagCommand(testIndex, updatePersonTagsDescriptor));

        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void parseCommand_priority() throws Exception {
        Index testIndex = INDEX_FIRST_PERSON;
        Priority priority = new Priority(VALID_PRIORITY_LOW);

        PriorityCommand expectedCommand = new PriorityCommand(testIndex, priority);
        Command actualCommand = parser.parseCommand(PersonUtil.getPriorityCommand(testIndex, priority));

        assertEquals(expectedCommand, actualCommand);
    }

    @Test
    public void parseCommand_insurance() throws Exception {
        Index testIndex = INDEX_FIRST_PERSON;
        Insurance carInsurance = new Insurance(VALID_INSURANCE_CAR);
        Insurance healthInsurance = new Insurance(VALID_INSURANCE_HEALTH);
        InsuranceCommand.UpdatePersonInsuranceDescriptor descriptor =
                new InsuranceCommand.UpdatePersonInsuranceDescriptor(new HashSet<>(), new HashSet<>());

        descriptor.setInsurancesToAdd(carInsurance);
        descriptor.setInsurancesToDelete(healthInsurance);

        InsuranceCommand expectedCommand = new InsuranceCommand(testIndex, descriptor);
        Command actualCommand = parser.parseCommand(PersonUtil.getInsuranceCommand(testIndex, descriptor));

        assertEquals(expectedCommand, actualCommand);
    }


    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
