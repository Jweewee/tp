package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BOB;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class PersonTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Person person = new PersonBuilder().build();
        assertThrows(UnsupportedOperationException.class, () -> person.getTags().remove(0));
    }

    @Test
    public void isSamePerson() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // same name, all other attributes different -> returns true
        Person editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB)
                .withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSamePerson(editedAlice));

        // different name, all other attributes same -> returns false
        editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSamePerson(editedAlice));

        // name differs in case, all other attributes same -> returns false
        Person editedBob = new PersonBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PersonBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void createPersonWithUpdatedTags() {
        Person bobWithoutTags = new PersonBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
                .withEmail(VALID_EMAIL_BOB).build();
        Person bobWithHusbandTag = new PersonBuilder(bobWithoutTags).withTags(VALID_TAG_HUSBAND).build();
        Person bobWithFriendTag = new PersonBuilder(bobWithoutTags).withTags(VALID_TAG_FRIEND).build();
        Person bobWithHusbandAndFriendTag = new PersonBuilder(bobWithoutTags)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();

        Tag friendTag = new Tag(VALID_TAG_FRIEND);
        Tag husbandTag = new Tag(VALID_TAG_HUSBAND);
        Set<Tag> emptyTagsSet = new HashSet<>();
        Set<Tag> friendTagSet = new HashSet<>();
        Set<Tag> husbandTagSet = new HashSet<>();
        Set<Tag> husbandAndFriendTagSet = new HashSet<>();

        friendTagSet.add(friendTag);
        husbandTagSet.add(husbandTag);
        husbandAndFriendTagSet.add(friendTag);
        husbandAndFriendTagSet.add(husbandTag);

        // do not update tag of person
        assertEquals(bobWithoutTags, bobWithoutTags.createPersonWithUpdatedTags(emptyTagsSet, emptyTagsSet));
        assertEquals(bobWithFriendTag, bobWithFriendTag.createPersonWithUpdatedTags(emptyTagsSet, emptyTagsSet));
        assertEquals(bobWithHusbandAndFriendTag,
                bobWithHusbandAndFriendTag.createPersonWithUpdatedTags(emptyTagsSet, emptyTagsSet));

        // adding one tag to person that does not have that tag
        assertEquals(bobWithFriendTag, bobWithoutTags.createPersonWithUpdatedTags(friendTagSet, emptyTagsSet));
        assertEquals(bobWithHusbandAndFriendTag,
                bobWithFriendTag.createPersonWithUpdatedTags(husbandTagSet, emptyTagsSet));

        // adding one tag to person that has that tag
        assertEquals(bobWithFriendTag, bobWithFriendTag.createPersonWithUpdatedTags(friendTagSet, emptyTagsSet));


        // adding mutiple tags to person that does not have all tag
        assertEquals(bobWithHusbandAndFriendTag,
                bobWithoutTags.createPersonWithUpdatedTags(husbandAndFriendTagSet, emptyTagsSet));

        // adding multiple tags to person that have some of the tags
        assertEquals(bobWithHusbandAndFriendTag,
                bobWithFriendTag.createPersonWithUpdatedTags(husbandAndFriendTagSet, emptyTagsSet));

        // adding multiple tags to person that have all of the tags
        assertEquals(bobWithHusbandAndFriendTag,
                bobWithHusbandAndFriendTag.createPersonWithUpdatedTags(husbandAndFriendTagSet, emptyTagsSet));




        // deleting one tag from person that does not have that tag
        assertEquals(bobWithoutTags, bobWithoutTags.createPersonWithUpdatedTags(emptyTagsSet, friendTagSet));
        assertEquals(bobWithFriendTag, bobWithFriendTag.createPersonWithUpdatedTags(emptyTagsSet, husbandTagSet));

        // deleting one tag from person that has that tag
        assertEquals(bobWithoutTags, bobWithFriendTag.createPersonWithUpdatedTags(emptyTagsSet, friendTagSet));
        assertEquals(bobWithHusbandTag,
                bobWithHusbandAndFriendTag.createPersonWithUpdatedTags(emptyTagsSet, friendTagSet));

        // deleting multiple tags from person that does not have that tag
        assertEquals(bobWithoutTags, bobWithoutTags.createPersonWithUpdatedTags(emptyTagsSet, husbandAndFriendTagSet));

        // deleting mutiple tags from person that has partial or all of the tags
        assertEquals(bobWithoutTags,
                bobWithFriendTag.createPersonWithUpdatedTags(emptyTagsSet, husbandAndFriendTagSet));
        assertEquals(bobWithoutTags,
                bobWithHusbandAndFriendTag.createPersonWithUpdatedTags(emptyTagsSet, husbandAndFriendTagSet));




        // adding new tag and deleting existing tag of a person
        assertEquals(bobWithFriendTag, bobWithHusbandTag.createPersonWithUpdatedTags(friendTagSet, husbandTagSet));

        // adding existing tag and deleting existing tag of a person
        assertEquals(bobWithFriendTag,
                bobWithHusbandAndFriendTag.createPersonWithUpdatedTags(friendTagSet, husbandTagSet));

        // adding new tag and deleting non-existing tag of a person
        assertEquals(bobWithFriendTag, bobWithoutTags.createPersonWithUpdatedTags(friendTagSet, husbandTagSet));

        // adding existing tag and deleting non-existing tag of a person
        assertEquals(bobWithFriendTag, bobWithFriendTag.createPersonWithUpdatedTags(friendTagSet, husbandTagSet));

    }

    @Test
    public void equals() {
        // same values -> returns true
        Person aliceCopy = new PersonBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertFalse(ALICE.equals(null));

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns false
        Person editedAlice = new PersonBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PersonBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different email -> returns false
        editedAlice = new PersonBuilder(ALICE).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PersonBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Person.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
                + ", email=" + ALICE.getEmail() + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags() + "}";
        assertEquals(expected, ALICE.toString());
    }
}
