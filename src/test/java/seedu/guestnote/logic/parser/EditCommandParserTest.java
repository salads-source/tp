package seedu.guestnote.logic.parser;

import static seedu.guestnote.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.guestnote.logic.commands.CommandTestUtil.ADD_REQUEST_DESC_EXTRAPILLOW;
import static seedu.guestnote.logic.commands.CommandTestUtil.ADD_REQUEST_DESC_SEAVIEW;
import static seedu.guestnote.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_ADD_REQUEST_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.guestnote.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.REQUEST_DESC_EXTRAPILLOW;
import static seedu.guestnote.logic.commands.CommandTestUtil.REQUEST_DESC_SEAVIEW;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_REQUEST_EXTRAPILLOW;
import static seedu.guestnote.logic.commands.CommandTestUtil.VALID_REQUEST_SEAVIEW;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ADD_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_DELETE_REQ;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_DELETE_REQ_INDEX;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.guestnote.logic.parser.CliSyntax.PREFIX_ROOMNUMBER;
import static seedu.guestnote.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.guestnote.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.guestnote.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_FIRST_GUEST;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_SECOND_GUEST;
import static seedu.guestnote.testutil.TypicalIndexes.INDEX_THIRD_GUEST;

import org.junit.jupiter.api.Test;

import seedu.guestnote.commons.core.index.Index;
import seedu.guestnote.logic.Messages;
import seedu.guestnote.logic.commands.EditCommand;
import seedu.guestnote.logic.commands.EditCommand.EditGuestDescriptor;
import seedu.guestnote.model.guest.Email;
import seedu.guestnote.model.guest.Name;
import seedu.guestnote.model.guest.Phone;
import seedu.guestnote.model.request.Request;
import seedu.guestnote.testutil.EditGuestDescriptorBuilder;

public class EditCommandParserTest {
    private static final String REQUEST_ADD_EMPTY = " " + PREFIX_ADD_REQ;
    private static final String REQUEST_DELETE_EMPTY = " " + PREFIX_ADD_REQ;

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE);
    private static final String MESSAGE_INVALID_INDEX_TEST = String.format(MESSAGE_INVALID_INDEX,
            EditCommand.MESSAGE_USAGE);

    private EditCommandParser parser = new EditCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NAME_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_INDEX_TEST);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_INDEX_TEST);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_NAME_DESC, Name.MESSAGE_CONSTRAINTS); // invalid name
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC, Phone.MESSAGE_CONSTRAINTS); // invalid phone
        assertParseFailure(parser, "1" + INVALID_EMAIL_DESC, Email.MESSAGE_CONSTRAINTS); // invalid email
        assertParseFailure(parser, "1" + INVALID_ADD_REQUEST_DESC, Request.MESSAGE_CONSTRAINTS); // invalid request

        // invalid phone followed by valid email
        assertParseFailure(parser, "1" + INVALID_PHONE_DESC + EMAIL_DESC_AMY, Phone.MESSAGE_CONSTRAINTS);

        // parsing adding an empty request results in error
        assertParseFailure(parser,
                "1" + REQUEST_ADD_EMPTY, Request.MESSAGE_CONSTRAINTS);

        // parsing adding an empty request results in error
        assertParseFailure(parser,
                "1" + REQUEST_DELETE_EMPTY, Request.MESSAGE_CONSTRAINTS);

        // multiple invalid values, but only the first invalid value is captured
        assertParseFailure(parser,
                "1" + INVALID_NAME_DESC + INVALID_EMAIL_DESC + VALID_PHONE_AMY,
                Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_GUEST;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + ADD_REQUEST_DESC_EXTRAPILLOW
                + EMAIL_DESC_AMY + NAME_DESC_AMY;

        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_AMY)
                .withRequestsToAdd(VALID_REQUEST_EXTRAPILLOW).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_GUEST;
        String userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + EMAIL_DESC_AMY;

        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder().withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_oneFieldSpecified_success() {
        // name
        Index targetIndex = INDEX_THIRD_GUEST;
        String userInput = targetIndex.getOneBased() + NAME_DESC_AMY;
        EditGuestDescriptor descriptor = new EditGuestDescriptorBuilder().withName(VALID_NAME_AMY).build();
        EditCommand expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // phone
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY;
        descriptor = new EditGuestDescriptorBuilder().withPhone(VALID_PHONE_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // email
        userInput = targetIndex.getOneBased() + EMAIL_DESC_AMY;
        descriptor = new EditGuestDescriptorBuilder().withEmail(VALID_EMAIL_AMY).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);

        // requests
        userInput = targetIndex.getOneBased() + ADD_REQUEST_DESC_SEAVIEW;
        descriptor = new EditGuestDescriptorBuilder().withRequestsToAdd(VALID_REQUEST_SEAVIEW).build();
        expectedCommand = new EditCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_failure() {
        // More extensive testing of duplicate parameter detections is done in
        // AddCommandParserTest#parse_repeatedNonRequestValue_failure()

        // valid followed by invalid
        Index targetIndex = INDEX_FIRST_GUEST;
        String userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + PHONE_DESC_BOB;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid followed by valid
        userInput = targetIndex.getOneBased() + PHONE_DESC_BOB + INVALID_PHONE_DESC;

        assertParseFailure(parser, userInput, Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // mulltiple valid fields repeated
        userInput = targetIndex.getOneBased() + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + REQUEST_DESC_SEAVIEW + PHONE_DESC_AMY + EMAIL_DESC_AMY + REQUEST_DESC_SEAVIEW
                + PHONE_DESC_BOB + EMAIL_DESC_BOB + REQUEST_DESC_EXTRAPILLOW;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL));

        // multiple invalid values
        userInput = targetIndex.getOneBased() + INVALID_PHONE_DESC + INVALID_EMAIL_DESC
                + INVALID_PHONE_DESC + INVALID_EMAIL_DESC;

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE, PREFIX_EMAIL));
    }

    @Test
    public void parse_multipleRiFields_failure() {
        // multiple ri fields
        Index targetIndex = INDEX_FIRST_GUEST;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_DELETE_REQ_INDEX + "1 "
                + PREFIX_DELETE_REQ_INDEX + "2";
        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_DELETE_REQ_INDEX));
    }

    @Test
    public void parse_deleteReqIndexAndDeleteReq_failure() {
        Index targetIndex = INDEX_FIRST_GUEST;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_DELETE_REQ_INDEX + "1 "
                + PREFIX_DELETE_REQ + "Extra pillow";

        assertParseFailure(parser, userInput,
                "Cannot use " + PREFIX_DELETE_REQ_INDEX + " " + PREFIX_DELETE_REQ + " together.");
    }

    @Test
    public void parse_duplicateNonRequestPrefixes_failure() {
        Index targetIndex = INDEX_FIRST_GUEST;

        // Duplicate name prefix
        String userInput = targetIndex.getOneBased() + " "
                + PREFIX_NAME + "Bob" + " " + PREFIX_NAME + "Alice";

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // Duplicate phone prefix
        userInput = targetIndex.getOneBased() + " "
                + PREFIX_PHONE + "89238322" + " " + PREFIX_PHONE + "87654321";

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // Duplicate email prefix
        userInput = targetIndex.getOneBased() + " "
                + PREFIX_EMAIL + "alice@example.com" + " " + PREFIX_EMAIL + "bob@example.com";

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // Duplicate room number prefix
        userInput = targetIndex.getOneBased() + " "
                + PREFIX_ROOMNUMBER + "01-01" + " " + PREFIX_ROOMNUMBER + "01-02";

        assertParseFailure(parser, userInput,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ROOMNUMBER));
    }
}
