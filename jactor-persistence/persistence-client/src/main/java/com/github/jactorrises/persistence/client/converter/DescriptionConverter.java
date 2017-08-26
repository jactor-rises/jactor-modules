package nu.hjemme.persistence.client.converter;

import nu.hjemme.client.datatype.Description;

public class DescriptionConverter implements TypeConverter<Description, String> {

    @Override public Description convertTo(String from) {
        return from != null ? new Description(from) : null;
    }

    @Override public String convertFrom(Description description) {
        return description != null ? description.getDescription() : null;
    }
}
