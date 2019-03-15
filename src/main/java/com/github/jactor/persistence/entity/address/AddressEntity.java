package com.github.jactor.persistence.entity.address;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.entity.PersistentEntity;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_ADDRESS")
public class AddressEntity extends PersistentEntity {

  @Id
  private Long id;

  @Column(name = "ADDRESS_LINE_1", nullable = false)
  private String addressLine1;
  @Column(name = "ADDRESS_LINE_2")
  private String addressLine2;
  @Column(name = "ADDRESS_LINE_3")
  private String addressLine3;
  @Column(name = "CITY", nullable = false)
  private String city;
  @Column(name = "COUNTRY")
  private String country;
  @Column(name = "ZIP_CODE", nullable = false)
  private Integer zipCode;

  @SuppressWarnings("unused")
  AddressEntity() {
    // used by entity manager
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

  public AddressEntity(@NotNull AddressDto addressDto) {
    super(addressDto.fetchPersistentDto());

    addressLine1 = addressDto.getAddressLine1();
    addressLine2 = addressDto.getAddressLine2();
    addressLine3 = addressDto.getAddressLine3();
    city = addressDto.getCity();
    country = addressDto.getCountry();
    zipCode = addressDto.getZipCode();
  }

  public AddressDto asDto() {
    return new AddressDto(
        initPersistentDto(),
        zipCode, addressLine1, addressLine2, addressLine3, city, country
    );
  }

  public @Override
  AddressEntity copy() {
    return new AddressEntity(this);
  }

  protected @Override
  Stream<Optional<PersistentEntity>> streamSequencedDependencies() {
    return Stream.empty();
  }

  public @Override
  boolean equals(Object o) {
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

  public @Override
  int hashCode() {
    return hash(addressLine1, addressLine2, addressLine3, city, country, zipCode);
  }

  public @Override
  String toString() {
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

  public @Override
  Long getId() {
    return id;
  }

  protected @Override
  void setId(Long id) {
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


  public static AddressEntity anAddress(AddressDto addressDto) {
    return new AddressEntity(addressDto);
  }
}
