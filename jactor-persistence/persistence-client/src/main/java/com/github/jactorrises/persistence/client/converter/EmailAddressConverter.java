package nu.hjemme.persistence.client.converter;

import nu.hjemme.client.datatype.EmailAddress;

public class EmailAddressConverter implements TypeConverter<EmailAddress, String> {
    @Override public EmailAddress convertTo(String from) {
        return from != null ? new EmailAddress(from) : null;
    }

    @Override public String convertFrom(EmailAddress emailAddress) {
        return emailAddress != null ? emailAddress.asString() : null;
    }
}
