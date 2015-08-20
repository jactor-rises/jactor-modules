package nu.hjemme.business.domain.builder;

import nu.hjemme.business.domain.GuestBookEntryDomain;
import nu.hjemme.client.datatype.Name;
import nu.hjemme.persistence.db.GuestBookEntityImpl;
import nu.hjemme.persistence.db.PersonEntityImpl;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/** @author Tor Egil Jacobsen */
public class GuestBookDomainEntryBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void willNotBuildGuestBookEntryWithoutAnEntry() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(GuestBookEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);

        GuestBookEntryDomainBuilder.init().withCreatorNameAs("some creator").with(new GuestBookEntityImpl()).get();
    }

    @Test
    public void willNotBuildGuestBookEntryWithAnEmptyEntry() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(GuestBookEntryDomainBuilder.THE_ENTRY_CANNOT_BE_EMPTY);

        GuestBookEntryDomainBuilder.init()
                .withEntryAs("")
                .withCreatorNameAs("some creator")
                .with(new GuestBookEntityImpl())
                .get();
    }

    @Test
    public void willNotBuildGuestBookEntryWithoutTheGuestBook() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(GuestBookEntryDomainBuilder.THE_ENTRY_MUST_BELONG_TO_A_GUEST_BOOK);

        GuestBookEntryDomainBuilder.init().withEntryAs("some entry").withCreatorNameAs("some creator").get();
    }

    @Test
    public void willNotBuildGuestBookEntryWithoutTheNameOfTheCreator() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(GuestBookEntryDomainBuilder.THE_ENTRY_MUST_BE_CREATED_BY_SOMEONE);

        GuestBookEntryDomainBuilder.init()
                .withEntryAs("some entry")
                .with(new GuestBookEntityImpl())
                .get();
    }

    @Test
    public void willNotBuildGuestBookEntryWithAnEmptyNameOfTheCreator() {
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(hentFeilmeldingFraName());

        GuestBookEntryDomainBuilder.init()
                .withEntryAs("some entry")
                .withCreatorNameAs("")
                .with(new GuestBookEntityImpl())
                .get();
    }

    public String hentFeilmeldingFraName() {
        String nameErrorMessage = null;

        try {
            new Name("");
        } catch (IllegalArgumentException iae) {
            nameErrorMessage = iae.getMessage();
        }

        return nameErrorMessage;
    }

    @Test
    public void willBuildGuestBookEntryWhenAllRequiredFieldsAreSet() {
        GuestBookEntryDomain guestBookEntryDomain = GuestBookEntryDomainBuilder.init()
                .withEntryAs("some entry")
                .withCreatorNameAs("some creator")
                .with(new GuestBookEntityImpl())
                .get();

        assertThat("GuestBookEntryEntity", guestBookEntryDomain, is(notNullValue()));
    }

    @Test
    public void willSetCreatorNameWhenCreatorIsAppended() {
        PersonEntityImpl creator = new PersonEntityImpl();
        creator.setFirstName(new Name("some creator"));

        GuestBookEntryDomain guestBookEntryDomain = GuestBookEntryDomainBuilder.init()
                .withEntryAs("some entry")
                .with(creator)
                .with(new GuestBookEntityImpl())
                .get();

        assertThat("CreatorName", guestBookEntryDomain.getCreatorName(), is(equalTo(new Name("some creator"))));
    }
}
