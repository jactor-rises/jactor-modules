package com.gitlab.jactor.rises.persistence.entity.address;

import com.gitlab.jactor.rises.commons.dto.AddressDto;
import com.gitlab.jactor.rises.persistence.entity.PersistentEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.hash;

@Entity
@Table(name = "T_ADDRESS")
public class AddressEntity extends PersistentEntity<Long> {

    private @Id Long id;

    private @Column(name = "ADDRESS_LINE_1", nullable = false) String addressLine1;
    private @Column(name = "ADDRESS_LINE_2") String addressLine2;
    private @Column(name = "ADDRESS_LINE_3") String addressLine3;
    private @Column(name = "CITY", nullable = false) String city;
    private @Column(name = "COUNTRY") String country;
    private @Column(name = "ZIP_CODE", nullable = false) Integer zipCode;

    AddressEntity() {
        // used by builder
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
        country = address.getCountry();
        zipCode = address.getZipCode();
    }

    public AddressEntity(AddressDto addressDto) {
        super(addressDto);

        addressLine1 = addressDto.getAddressLine1();
        addressLine2 = addressDto.getAddressLine2();
        addressLine3 = addressDto.getAddressLine3();
        city = addressDto.getCity();
        country = addressDto.getCountry();
        zipCode = addressDto.getZipCode();
    }

    public AddressDto asDto() {
        AddressDto addressDto = addPersistentData(new AddressDto());
        addressDto.setAddressLine1(addressLine1);
        addressDto.setAddressLine2(addressLine2);
        addressDto.setAddressLine3(addressLine3);
        addressDto.setCity(city);
        addressDto.setCountry(country);
        addressDto.setZipCode(zipCode);

        return addressDto;
    }

    public @Override AddressEntity copy() {
        return new AddressEntity(this);
    }

    protected @Override Stream<Optional<PersistentEntity<Long>>> streamSequencedDependencies() {
        return Stream.empty();
    }

    public @Override boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressEntity addressEntity = (AddressEntity) o;

        return this == o || Objects.equals(addressLine1, addressEntity.addressLine1) &&
                Objects.equals(addressLine2, addressEntity.addressLine2) &&
                Objects.equals(addressLine3, addressEntity.addressLine3) &&
                Objects.equals(city, addressEntity.city) &&
                Objects.equals(country, addressEntity.country) &&
                Objects.equals(zipCode, addressEntity.zipCode);
    }

    public @Override int hashCode() {
        return hash(addressLine1, addressLine2, addressLine3, city, country, zipCode);
    }

    public @Override String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .appendSuper(super.toString())
                .append(getAddressLine1())
                .append(getAddressLine2())
                .append(getAddressLine3())
                .append(getZipCode())
                .append(getCity())
                .append(getCountry())
                .toString();
    }

    public @Override Long getId() {
        return id;
    }

    protected @Override void setId(Long id) {
        this.id = id;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getAddressLine3() {
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
