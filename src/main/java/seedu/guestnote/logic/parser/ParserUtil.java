package seedu.guestnote.logic.parser;

import static java.util.Objects.requireNonNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import seedu.guestnote.commons.core.index.Index;
import seedu.guestnote.commons.util.StringUtil;
import seedu.guestnote.logic.parser.exceptions.ParseException;
import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.guest.RoomNumber;
import seedu.guestnote.model.request.Request;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    public static final String MESSAGE_INDEX_HAS_LEADING_ZEROES =
            "The index provided has leading zeros. Use '%s' instead of '%s'.";
    public static final String MESSAGE_INDEX_TOO_LARGE = "The index provided is too large.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!trimmedIndex.matches("\\d+")) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        BigInteger bigInt = new BigInteger(trimmedIndex);
        if (bigInt.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new ParseException(MESSAGE_INDEX_TOO_LARGE);
        }

        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }

        if (trimmedIndex.matches("^0+[1-9]\\d*$")) {
            String suggested = trimmedIndex.replaceFirst("^0+(?!$)", "");
            throw new ParseException(String.format(MESSAGE_INDEX_HAS_LEADING_ZEROES, suggested, trimmedIndex));
        }

        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a list of {@code String indexes} into a list of {@code Index}.
     * Leading and trailing whitespaces will be trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static List<Index> parseIndexes(List<String> indexes) throws ParseException {
        List<Index> indexList = new ArrayList<>();
        for (String index : indexes) {
            indexList.add(parseIndex(index));
        }
        return indexList;
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String roomNumber} into a {@code RoomNumber}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code roomNumber} is invalid.
     */
    public static RoomNumber parseRoomNumber(String roomNumber) throws ParseException {
        requireNonNull(roomNumber);
        String trimmedRoomNumber = roomNumber.trim();
        if (!RoomNumber.isValidRoomNumber(trimmedRoomNumber)) {
            throw new ParseException(RoomNumber.MESSAGE_CONSTRAINTS);
        }
        return new RoomNumber(trimmedRoomNumber);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim().toLowerCase();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String request} into a {@code Request}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code request} is invalid.
     */
    public static Request parseRequest(String request) throws ParseException {
        requireNonNull(request);
        String trimmedRequest = request.trim();
        if (!Request.isValidRequestName(trimmedRequest)) {
            throw new ParseException(Request.MESSAGE_CONSTRAINTS);
        }
        return new Request(trimmedRequest);
    }

    /**
     * Parses {@code Collection<String> requests} into a {@code UniqueRequestList}.
     */
    public static List<Request> parseRequests(Collection<String> reqs) throws ParseException {
        requireNonNull(reqs);
        List<Request> requestList = new ArrayList<>();
        for (String req : reqs) {
            requestList.add(parseRequest(req));
        }
        return requestList;
    }
}
