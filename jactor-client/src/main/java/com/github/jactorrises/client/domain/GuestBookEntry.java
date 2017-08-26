package nu.hjemme.client.domain;

public interface GuestBookEntry extends Persistent<Long>, Entry {

    GuestBook getGuestBook();
}

