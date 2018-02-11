package com.github.jactorrises.persistence.orm.entity.address;

import com.github.jactor.rises.client.datatype.Country;

import java.util.Objects;

import static java.util.Objects.hash;

class CountryEmbaddable {
    private final String country;

    @SuppressWarnings("unused") // used by hibernate
    CountryEmbaddable() {
        country = null;
    }

    CountryEmbaddable(Country country) {
        this.country = country != null ? country.getCountryCode() : null;
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
