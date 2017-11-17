package com.github.jactorrises.model.persistence.entity.address;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.model.persistence.entity.PersistentEntity;
import com.github.jactorrises.persistence.client.entity.AddressEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_ADDRESS")
public class AddressOrm extends PersistentEntity implements AddressEntity {

    @Embedded @AttributeOverride(name = "country", column = @Column(name = "COUNTRY")) private CountryEmbaddable country;
    @Column(name = "ZIP_CODE") private Integer zipCode;
    @Column(name = "ADDRESS_LINE_1") private String addressLine1;
    @Column(name = "ADDRESS_LINE_2") private String addressLine2;
    @Column(name = "ADDRESS_LINE_3") private String addressLine3;
    @Column(name = "CITY") private String city;

    AddressOrm() {
    }

    /**
     * @param address to copy
     */
    private AddressOrm(AddressOrm address) {
        super(address);

        addressLine1 = address.getAddressLine1();
        addressLine2 = address.getAddressLine2();
        addressLine3 = address.getAddressLine3();
        city = address.getCity();
        country = address.country;
        zipCode = address.getZipCode();
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(addressLine1, ((AddressOrm) o).addressLine1) &&
                Objects.equals(addressLine2, ((AddressOrm) o).addressLine2) &&
                Objects.equals(addressLine3, ((AddressOrm) o).addressLine3) &&
                Objects.equals(city, ((AddressOrm) o).city) &&
                Objects.equals(getCountry(), fetchCountry(((AddressOrm) o).country)) &&
                Objects.equals(zipCode, ((AddressOrm) o).zipCode);
    }

    private Country fetchCountry(CountryEmbaddable country) {
        return country != null ? country.fetchCountry() : null;
    }

    public AddressOrm copy() {
        return new AddressOrm(this);
    }

    @Override public int hashCode() {
        return hash(addressLine1, addressLine2, addressLine3, city, country, zipCode);
    }

    @Override public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getAddressLine1())
                .append(getAddressLine2())
                .append(getAddressLine3())
                .append(getCity())
                .append(getCountry())
                .append(getZipCode())
                .toString();
    }

    @Override public Country getCountry() {
        return country != null ? country.fetchCountry() : null;
    }

    @Override public Integer getZipCode() {
        return zipCode;
    }

    @Override public String getAddressLine1() {
        return addressLine1;
    }

    @Override public String getAddressLine2() {
        return addressLine2;
    }

    @Override public String getAddressLine3() {
        return addressLine3;
    }

    @Override public String getCity() {
        return city;
    }

    @Override public void setCountry(Country country) {
        this.country = new CountryEmbaddable(country);
    }

    @Override public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    @Override public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    @Override public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    @Override public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    @Override public void setCity(String city) {
        this.city = city;
    }
}
