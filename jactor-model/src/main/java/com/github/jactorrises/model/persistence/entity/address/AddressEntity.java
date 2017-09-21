package com.github.jactorrises.model.persistence.entity.address;

import com.github.jactorrises.client.datatype.Country;
import com.github.jactorrises.client.domain.Address;
import com.github.jactorrises.model.persistence.entity.PersistentEntity;
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
public class AddressEntity extends PersistentEntity implements Address {

    @Embedded @AttributeOverride(name = "country", column = @Column(name = "COUNTRY")) private CountryEmbaddable country;
    @Column(name = "ZIP_CODE") private Integer zipCode;
    @Column(name = "ADDRESS_LINE_1") private String addressLine1;
    @Column(name = "ADDRESS_LINE_2") private String addressLine2;
    @Column(name = "ADDRESS_LINE_3") private String addressLine3;
    @Column(name = "CITY") private String city;

    public AddressEntity() {
    }

    /**
     * @param address to copy
     */
    AddressEntity(AddressEntity address) {
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
                Objects.equals(getId(), ((AddressEntity) o).getId()) &&
                Objects.equals(addressLine1, ((AddressEntity) o).addressLine1) &&
                Objects.equals(addressLine2, ((AddressEntity) o).addressLine2) &&
                Objects.equals(addressLine3, ((AddressEntity) o).addressLine3) &&
                Objects.equals(city, ((AddressEntity) o).city) &&
                Objects.equals(country, ((AddressEntity) o).country) &&
                Objects.equals(zipCode, ((AddressEntity) o).zipCode);
    }

    public AddressEntity copy() {
        return new AddressEntity(this);
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

    public void setCountry(Country country) {
        this.country = new CountryEmbaddable(country);
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public static AddressEntityBuilder anAddress() {
        return new AddressEntityBuilder();
    }
}
