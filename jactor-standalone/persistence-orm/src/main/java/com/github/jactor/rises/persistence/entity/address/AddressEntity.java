package com.github.jactor.rises.persistence.entity.address;

import com.github.jactor.rises.client.dto.NewAddressDto;
import com.github.jactor.rises.persistence.entity.PersistentEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_ADDRESS")
public class AddressEntity extends PersistentEntity {

    @Column(name = "ADDRESS_LINE_1", nullable = false) private String addressLine1;
    @Column(name = "ADDRESS_LINE_2") private String addressLine2;
    @Column(name = "ADDRESS_LINE_3") private String addressLine3;
    @Column(name = "CITY", nullable = false) private String city;
    @Column(name = "COUNTRY") private String country;
    @Column(name = "ZIP_CODE", nullable = false) private Integer zipCode;

    AddressEntity() {
    }

    /**
     * @param address to copy
     */
    private AddressEntity(AddressEntity address) {
        super(address);

        addressLine1 = address.getAddressLine1();
        addressLine2 = address.getAddressLine2();
        addressLine3 = address.getAddressLine3();
        city = address.getCity();
        country = address.country;
        zipCode = address.getZipCode();
    }

    public AddressEntity(NewAddressDto addressDto) {
        super(addressDto);
    }

    @Override public boolean equals(Object o) {
        AddressEntity addressEntity = (AddressEntity) o;

        return this == o || o != null && getClass() == o.getClass() &&
                Objects.equals(addressLine1, addressEntity.addressLine1) &&
                Objects.equals(addressLine2, addressEntity.addressLine2) &&
                Objects.equals(addressLine3, addressEntity.addressLine3) &&
                Objects.equals(city, addressEntity.city) &&
                Objects.equals(country, addressEntity.country) &&
                Objects.equals(zipCode, addressEntity.zipCode);
    }

    public AddressEntity copy() {
        return new AddressEntity(this).asCopy();
    }

    public NewAddressDto asDto() {
        NewAddressDto addressDto = addPersistentData(new NewAddressDto());
        addressDto.setAddressLine1(addressLine1);
        addressDto.setAddressLine2(addressLine2);
        addressDto.setAddressLine3(addressLine3);
        addressDto.setCity(city);
        addressDto.setCountry(country);
        addressDto.setZipCode(zipCode);

        return addressDto;
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

    public Integer getZipCode() {
        return zipCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    private String getAddressLine2() {
        return addressLine2;
    }

    private String getAddressLine3() {
        return addressLine3;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
