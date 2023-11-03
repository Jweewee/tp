package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;

/**
 * Represents an appointment with the Person in the address book.
 * Guarantees: immutable; is always valid.
 */
public class Appointment {

    public static final String MESSAGE_CONSTRAINTS =
            "Index must be a positive integer \n"
            + "Date must be in YYYY-MM-DD format and in the future \n"
            + "Time must be in 24h HH:MM format \n"
            + "Venue should not be longer than 30 characters";
    public static final String NO_APPOINTMENT = "-";
    public static final String NO_TIME = "";
    public static final String NO_VENUE = "";
    public static final String INVALID_DATE_INPUT = "Date parameter must be YYYY-MM-DD, "
            + "with valid calendar year, month and days";
    public static final String PREVIOUS_DATE_INPUT = "Unable to schedule your appointment "
            + "on a date in the past";
    public static final String INVALID_TIME_INPUT = "Time field must be HH:MM";
    public static final String INVALID_VENUE_INPUT = "Venue field must not be greater than 30 characters";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmm");
    private static final Logger logger = LogsCenter.getLogger(Appointment.class);

    private String date;
    private String time;
    private String venue;
    private LocalDate localDate;
    /**
     * Constructs an {@code Appointment}.
     *
     * @param date A valid date (dd MMM yyyy).
     * @param time A valid time (hhmm).
     * @param venue A venue (less than 30 characters).
     */
    public Appointment(String date, String time, String venue) {
        requireNonNull(date);
        checkArgument(isValidDateFormat(date), INVALID_DATE_INPUT);
        checkArgument(isValidTimeFormat(time), INVALID_TIME_INPUT);
        checkArgument(isValidVenueFormat(time), INVALID_VENUE_INPUT);
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.localDate = createDate();
    }

    /**
     * Creates a Date object from the date from a String.
     *
     * @return LocalDate object from the string.
     */
    public LocalDate createDate() {
        if (date.equals(NO_APPOINTMENT)) {
            return LocalDate.MAX; // no date -> return max localdate
        }
        return LocalDate.parse(this.date, DATE_FORMATTER);
    }


    /**
     * Returns a string in the format 'dd MM yyyy' of the appointment.
     *
     * @return A string representing the date of the appointment.
     */
    public String getDate() {
        return this.date;
    }


    /**
     * Returns a string in the format 'HHmm'hrs of the appointment.
     *
     * @return A string representing the formatted time of the appointment.
     */
    public String getTimeFormatted() {
        if (this.time.equals(NO_TIME)) {
            return "";
        }

        return this.time + "hrs";
    }

    /**
     * Returns a string in the format 'HHmm' of the appointment.
     *
     * @return A string representing the time of the appointment.
     */
    public String getTime() {
        return this.time;
    }

    /**
     * Returns a string representing the venue of the appointment.
     *
     * @return A string representing the venue of the appointment.
     */
    public String getVenue() {
        return this.venue;
    }

    /**
     * Generates an empty {@code Appointment}.
     *
     * @return An empty Appointment.
     */
    public static Appointment getDefaultEmptyAppointment() {
        return new Appointment(NO_APPOINTMENT, NO_TIME, NO_VENUE);
    }

    /**
     * Checks if the date input follows the format (dd-MM-yyyy).
     *
     * @param test The input date string.
     * @return A boolean indicating if the date input follows the format.
     */
    public static boolean isValidDateFormat(String test) {
        if (test.equalsIgnoreCase(NO_APPOINTMENT)) {
            return true;
        }
        try {
            LocalDate.parse(test, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if the time input follows the format (HHmm).
     *
     * @param test The input time string.
     * @return A boolean indicating if the time input follows the format.
     */
    public static boolean isValidTimeFormat(String test) {
        if (test.equalsIgnoreCase(NO_TIME)) {
            return true;
        }
        try {
            LocalTime.parse(test, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Checks if the venue input follows the format (max 30 characters).
     *
     * @param test The input venue string.
     * @return A boolean indicating if the time input follows the format.
     */
    public static boolean isValidVenueFormat(String test) {
        if (test.equalsIgnoreCase(NO_VENUE)) {
            return true;
        }
        return test.length() <= 30;
    }
    /**
     * Checks whether an appointment has been scheduled.
     *
     * @param appointment The appointment to be checked
     * @return A boolean indicating whether the appointment has been scheduled.
     */
    public static boolean isAppointmentEmpty(Appointment appointment) {
        return appointment.equals(getDefaultEmptyAppointment());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppt = (Appointment) other;
        return otherAppt.getDate().equals(getDate())
                && otherAppt.getTimeFormatted().equals(getTimeFormatted())
                && otherAppt.getVenue().equals(getVenue());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(date, time, venue);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append(this.getDate())
                .append(" ")
                .append(this.getTimeFormatted())
                .append(" ")
                .append(this.getVenue());

        return builder.toString();
    }
}
