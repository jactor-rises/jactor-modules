package com.github.jactorrises.model.persistence.entity.address;

import com.github.jactorrises.client.datatype.Country;

import java.util.Objects;

import static java.util.Objects.hash;

class CountryEmbaddable {
    private final String country;

    @SuppressWarnings("unused") // used by hibernate
    CountryEmbaddable() {
        country = null;
    }

    CountryEmbaddable(Country country) {
        this.country = country.getCountryCode();
    }

    Country fetchCountry() {
        return country != null ? new Country(country) : null;
    }

    @Override public int hashCode() {
        return hash(country);
    }

    @Override public boolean equals(Object obj) {
        return obj == this || obj != null && obj.getClass().equals(getClass()) && Objects.equals(country, ((CountryEmbaddable) obj).country);
    }

    @Override public String toString() {
        return country;
    }
}
