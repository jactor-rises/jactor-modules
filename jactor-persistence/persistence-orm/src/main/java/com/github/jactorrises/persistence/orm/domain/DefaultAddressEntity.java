package nu.hjemme.persistence.orm.domain;

import nu.hjemme.client.datatype.Country;
import nu.hjemme.client.domain.Address;
import nu.hjemme.persistence.client.AddressEntity;
import nu.hjemme.persistence.orm.meta.AddressMetadata;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = AddressMetadata.TABLE)
public class DefaultAddressEntity extends DefaultPersistentEntity implements AddressEntity {

    @Column(name = AddressMetadata.COUNTRY) private String country;
    @Column(name = AddressMetadata.ZIP_CODE) private Integer zipCode;
    @Column(name = AddressMetadata.ADDRESS_LINE_1) private String addressLine1;
    @Column(name = AddressMetadata.ADDRESS_LINE_2) private String addressLine2;
    @Column(name = AddressMetadata.ADDRESS_LINE_3) private String addressLine3;
    @Column(name = AddressMetadata.CITY) private String city;

    public DefaultAddressEntity() {
    }

    /**
     * @param address to copy
     */
    public DefaultAddressEntity(Address address) {
        if (address != null) {
            addressLine1 = address.getAddressLine1();
            addressLine2 = address.getAddressLine2();
            addressLine3 = address.getAddressLine3();
            city = address.getCity();
            country = convertFrom(address.getCountry(), Country.class);
            zipCode = address.getZipCode();
        }
    }

    @Override public boolean equals(Object o) {
        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(getId(), ((DefaultAddressEntity) o).getId()) &&
                Objects.equals(addressLine1, ((DefaultAddressEntity) o).addressLine1) &&
                Objects.equals(addressLine2, ((DefaultAddressEntity) o).addressLine2) &&
                Objects.equals(addressLine3, ((DefaultAddressEntity) o).addressLine3) &&
                Objects.equals(city, ((DefaultAddressEntity) o).city) &&
                Objects.equals(country, ((DefaultAddressEntity) o).country) &&
                Objects.equals(zipCode, ((DefaultAddressEntity) o).zipCode);
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
        return convertTo(country, Country.class);
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

    @Override public void setCountry(String country) {
        this.country = country;
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
