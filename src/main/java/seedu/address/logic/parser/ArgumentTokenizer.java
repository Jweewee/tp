package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PREFIX_EMPTY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tokenizes arguments string of the form: {@code preamble <prefix>value <prefix>value ...}<br>
 *     e.g. {@code some preamble text t/ 11.00 t/12.00 k/ m/ July}  where prefixes are {@code t/ k/ m/}.<br>
 * 1. An argument's value can be an empty string e.g. the value of {@code k/} in the above example.<br>
 * 2. Leading and trailing whitespaces of an argument value will be discarded.<br>
 * 3. An argument may be repeated and all its values will be accumulated e.g. the value of {@code t/}
 *    in the above example.<br>
 */
public class ArgumentTokenizer {

    /**
     * Tokenizes an arguments string and returns an {@code ArgumentMultimap} object that maps prefixes to their
     * respective argument values. Only the given prefixes will be recognized in the arguments string.
     *
     * @param argsString Arguments string of the form: {@code preamble <prefix>value <prefix>value ...}
     * @param prefixes   Prefixes to tokenize the arguments string with
     * @return           ArgumentMultimap object that maps prefixes to their arguments
     */
    public static ArgumentMultimap tokenize(String argsString, Prefix... prefixes) {
        List<PrefixPosition> positions = findAllPrefixPositions(argsString, prefixes);
        return extractArguments(argsString, positions);
    }

    /**
     * Finds all zero-based prefix positions in the given arguments string.
     *
     * @param argsString Arguments string of the form: {@code preamble <prefix>value <prefix>value ...}
     * @param prefixes   Prefixes to find in the arguments string
     * @return           List of zero-based prefix positions in the given arguments string
     */
    private static List<PrefixPosition> findAllPrefixPositions(String argsString, Prefix... prefixes) {
        return Arrays.stream(prefixes)
                .flatMap(prefix -> findPrefixPositions(argsString, prefix).stream())
                .collect(Collectors.toList());
    }

    /**
     * {@see findAllPrefixPositions}
     */
    private static List<PrefixPosition> findPrefixPositions(String argsString, Prefix prefix) {
        List<PrefixPosition> positions = new ArrayList<>();

        PrefixPosition prefixPosition = findPrefixPosition(argsString, prefix, 0);
        while (prefixPosition != null) {
            positions.add(prefixPosition);
            prefixPosition = findPrefixPosition(argsString, prefix, prefixPosition.getEndPosition());
        }

        return positions;
    }

    /**
     * Returns the {@code PrefixPosition} of the first occurrence of {@code prefix} in
     * {@code argsString} starting from index {@code fromIndex}. An occurrence
     * is valid if there is a whitespace before {@code prefix}. Returns null if no
     * such occurrence can be found.
     *
     * E.g if {@code argsString} = "e/hip/900", {@code prefix} = "p/" and
     * {@code fromIndex} = 0, this method returns null as there are no valid
     * occurrences of "p/" with whitespace before it. However, if
     * {@code argsString} = "e/hi p/900", {@code prefix} = "p/" and
     * {@code fromIndex} = 0, this method returns 5.
     */
    private static PrefixPosition findPrefixPosition(String argsString, Prefix prefix, int fromIndex) {
        String convertedArgsString = argsString.toLowerCase();

        int prefixIndex = convertedArgsString.indexOf(" " + prefix.getPrefix(), fromIndex);
        int secondaryPrefixIndex = convertedArgsString.indexOf(" " + prefix.getSecondaryPrefix(), fromIndex);

        if (prefixIndex == -1 && secondaryPrefixIndex == -1) {
            return null;
        }

        if (prefixIndex == -1) {
            return new PrefixPosition(prefix, secondaryPrefixIndex + 1,
                    secondaryPrefixIndex + 1 + prefix.getSecondaryPrefixLength());
        }

        if (secondaryPrefixIndex == -1) {
            return new PrefixPosition(prefix, prefixIndex + 1,
                    prefixIndex + 1 + prefix.getPrefixLength());
        }

        // both indexes are present
        if (prefixIndex < secondaryPrefixIndex) {
            return new PrefixPosition(prefix, prefixIndex + 1,
                    prefixIndex + 1 + prefix.getPrefixLength());
        } else {
            return new PrefixPosition(prefix, secondaryPrefixIndex + 1,
                    secondaryPrefixIndex + 1 + prefix.getSecondaryPrefixLength());
        }
    }

    /**
     * Extracts prefixes and their argument values, and returns an {@code ArgumentMultimap} object that maps the
     * extracted prefixes to their respective arguments. Prefixes are extracted based on their zero-based positions in
     * {@code argsString}.
     *
     * @param argsString      Arguments string of the form: {@code preamble <prefix>value <prefix>value ...}
     * @param prefixPositions Zero-based positions of all prefixes in {@code argsString}
     * @return                ArgumentMultimap object that maps prefixes to their arguments
     */
    private static ArgumentMultimap extractArguments(String argsString, List<PrefixPosition> prefixPositions) {

        // Sort by start position
        prefixPositions.sort((prefix1, prefix2) -> prefix1.getStartPosition() - prefix2.getStartPosition());

        // Insert a PrefixPosition to represent the preamble
        PrefixPosition preambleMarker = new PrefixPosition(PREFIX_EMPTY, 0, 0);
        prefixPositions.add(0, preambleMarker);

        // Add a dummy PrefixPosition to represent the end of the string
        PrefixPosition endPositionMarker = new PrefixPosition(PREFIX_EMPTY, argsString.length(), argsString.length());
        prefixPositions.add(endPositionMarker);

        // Map prefixes to their argument values (if any)
        ArgumentMultimap argMultimap = new ArgumentMultimap();
        for (int i = 0; i < prefixPositions.size() - 1; i++) {
            // Extract and store prefixes and their arguments
            Prefix argPrefix = prefixPositions.get(i).getPrefix();
            String argValue = extractArgumentValue(argsString, prefixPositions.get(i), prefixPositions.get(i + 1));
            argMultimap.put(argPrefix, argValue);
        }

        return argMultimap;
    }

    /**
     * Returns the trimmed value of the argument in the arguments string specified by {@code currentPrefixPosition}.
     * The end position of the value is determined by {@code nextPrefixPosition}.
     */
    private static String extractArgumentValue(String argsString,
                                        PrefixPosition currentPrefixPosition,
                                        PrefixPosition nextPrefixPosition) {
        int valueStartPos = currentPrefixPosition.getEndPosition();
        int valueEndPos = nextPrefixPosition.getStartPosition();
        String value = argsString.substring(valueStartPos, valueEndPos);

        return value.trim();
    }

    /**
     * Represents a prefix's position in an arguments string.
     */
    private static class PrefixPosition {
        private int startPosition;
        private int endPosition;
        private final Prefix prefix;

        PrefixPosition(Prefix prefix, int startPosition, int endPosition) {
            this.prefix = prefix;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
        }

        int getStartPosition() {
            return startPosition;
        }

        Prefix getPrefix() {
            return prefix;
        }


        /**
         * Returns the end position of the prefix.
         */
        int getEndPosition() {
            return endPosition;
        }
    }

}
