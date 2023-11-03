package seedu.address.logic.parser;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/**
 * A prefix that marks the beginning of an argument in an arguments string.
 * E.g. 't/' in 'add James t/ friend'.
 */
public class Prefix {
    private final String prefix;
    private final String secondaryPrefix;

    /**
     * Constructs a {@code Prefix} with {@code prefix} and {@code secondaryPrefix}.
     */
    public Prefix(String prefix, String secondaryPrefix) {
        requireAllNonNull(prefix, secondaryPrefix);

        this.prefix = prefix.toLowerCase();
        this.secondaryPrefix = secondaryPrefix.toLowerCase();
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSecondaryPrefix() {
        return secondaryPrefix;
    }

    /**
     * Returns the length of the prefix.
     */
    public int getPrefixLength() {
        return prefix.length();
    }

    /**
     * Returns the length of the secondaryPrefix.
     */
    public int getSecondaryPrefixLength() {
        return secondaryPrefix.length();
    }

    /**
     * Returns a formatter display message for this {@code Prefix}.
     * @return
     */
    public String getDisplayMessage() {
        return String.format("%s (%s)", prefix, secondaryPrefix);
    }

    @Override
    public String toString() {
        return getPrefix();
    }

    @Override
    public int hashCode() {
        return prefix == null ? 0 : prefix.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Prefix)) {
            return false;
        }

        Prefix otherPrefix = (Prefix) other;
        return prefix.equals(otherPrefix.prefix);
    }
}
