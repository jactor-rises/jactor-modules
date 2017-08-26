package nu.hjemme.persistence.client.converter;

public interface TypeConverter<To, From> {
    To convertTo(From from);

    From convertFrom(To to);
}
