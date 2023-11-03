package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.commands.CommandUtil.getPersonAtIndex;
import static seedu.address.logic.commands.CommandUtil.verifyPersonChanged;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPOINTMENT_VENUE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Person;

/**
 * Adds/Edits the appointment with an existing person in the address book.
 */
public class AppointmentCommand extends Command {

    public static final String COMMAND_WORD = "addappt";
    public static final String MESSAGE_USAGE = "Usage: \n" + COMMAND_WORD
            + " <index> "
            + PREFIX_APPOINTMENT + "<date>"
            + "[" + PREFIX_APPOINTMENT_TIME + "<time>]"
            + "[" + PREFIX_APPOINTMENT_VENUE + "<venue>]\n";

    public static final String MESSAGE_ADD_APPOINTMENT_SUCCESS = "Added appointment with customer: %1$s %2$s %3$s %4$s";
    public static final String MESSAGE_ADD_APPOINTMENT_FAILURE_APPT_EXIST = "Appointment already exists!";
    private static final Logger logger = LogsCenter.getLogger(AppointmentCommand.class);
    private final Index index;
    private final Appointment appointment;


    /**
     * Constructor for an {@code AppointmentCommand}
     *
     * @param index index of the customer list to add appointment for
     * @param appointment appointment scheduled with the target
     */
    public AppointmentCommand(Index index, Appointment appointment) {
        requireAllNonNull(index, appointment);

        this.index = index;
        this.appointment = appointment;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        Person personToEdit = getPersonAtIndex(model, index);
        logger.fine("AppointmentCommand executing...");

        if (!Appointment.isAppointmentEmpty(personToEdit.getAppointment())) {
            logger.warning("-----Invalid Add Appointment Command: Appointment Already Exist-----");
            throw new CommandException(MESSAGE_ADD_APPOINTMENT_FAILURE_APPT_EXIST);
        }
        Person editedPerson = Person.createPersonWithEditedAppointment(personToEdit, appointment);
        verifyPersonChanged(personToEdit, editedPerson);
        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        logger.info("-----Add Appointment Command: Appointment added successfully-----");

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message when appointment has been added successfully.
     * {@code editedPerson}.
     */
    private String generateSuccessMessage(Person editedPerson) {
        return String.format(MESSAGE_ADD_APPOINTMENT_SUCCESS, editedPerson.getName().fullName,
                editedPerson.getAppointment().getDate(),
                editedPerson.getAppointment().getTimeFormatted(),
                editedPerson.getAppointment().getVenue());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof AppointmentCommand)) {
            return false;
        }

        AppointmentCommand a = (AppointmentCommand) other;
        return index.equals(a.index)
                && appointment.equals(a.appointment);
    }
}
