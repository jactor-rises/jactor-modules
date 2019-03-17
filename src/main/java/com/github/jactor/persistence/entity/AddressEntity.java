package com.github.jactor.persistence.entity;

import static java.util.Objects.hash;

import com.github.jactor.persistence.dto.AddressDto;
import com.github.jactor.persistence.dto.PersistentDto;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.stream.Stream;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
@Table(name = "T_ADDRESS")
public class AddressEntity implements PersistentEntity<AddressEntity> {

  @Id
  private Long id;

  @Embedded
  @AttributeOverride(name = "createdBy", column = @Column(name = "CREATED_BY"))
  @AttributeOverride(name = "timeOfCreation", column = @Column(name = "CREATION_TIME"))
  @AttributeOverride(name = "modifiedBy", column = @Column(name = "UPDATED_BY"))
  @AttributeOverride(name = "timeOfModification", column = @Column(name = "UPDATED_TIME"))
  private PersistentDataEmbeddable persistentDataEmbeddable;

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
   * @param address to copyWithoutId
   */
  private AddressEntity(AddressEntity address) {
    persistentDataEmbeddable = new PersistentDataEmbeddable();
    addressLine1 = address.getAddressLine1();
    addressLine2 = address.getAddressLine2();
    addressLine3 = address.getAddressLine3();
    city = address.getCity();
    country = address.getCountry();
    id = address.getId();
    zipCode = address.getZipCode();
  }

  AddressEntity(@NotNull AddressDto addressDto) {
    persistentDataEmbeddable = new PersistentDataEmbeddable(addressDto.fetchPersistentDto());
    addressLine1 = addressDto.getAddressLine1();
    addressLine2 = addressDto.getAddressLine2();
    addressLine3 = addressDto.getAddressLine3();
    city = addressDto.getCity();
    country = addressDto.getCountry();
    id = addressDto.getId();
    zipCode = addressDto.getZipCode();
  }

  public AddressDto asDto() {
    return new AddressDto(initPersistentDto(), zipCode, addressLine1, addressLine2, addressLine3, city, country);
  }

  @Override
  public AddressEntity copyWithoutId() {
    AddressEntity addressEntity = new AddressEntity(this);
    addressEntity.setId(null);

    return addressEntity;
  }

  @Override
  public PersistentDto initPersistentDto() {
    return new PersistentDto(getId(), getCreatedBy(), getTimeOfCreation(), getModifiedBy(), getTimeOfModification());
  }

  @Override
  public void modify() {
    persistentDataEmbeddable.modify();
  }

  @Override
  public Stream<PersistentEntity> streamSequencedDependencies() {
    return Stream.empty();
  }

  @Override
  public boolean equals(Object o) {
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

  @Override
  public int hashCode() {
    return hash(addressLine1, addressLine2, addressLine3, city, country, zipCode);
  }

  @Override
  public String toString() {
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

  @Override
  public String getCreatedBy() {
    return persistentDataEmbeddable.getCreatedBy();
  }

  @Override
  public LocalDateTime getTimeOfCreation() {
    return persistentDataEmbeddable.getTimeOfCreation();
  }

  @Override
  public String getModifiedBy() {
    return persistentDataEmbeddable.getModifiedBy();
  }

  @Override
  public LocalDateTime getTimeOfModification() {
    return persistentDataEmbeddable.getTimeOfModification();
  }

  @Override
  public Long getId() {
    return id;
  }

  @Override
  public void setId(Long id) {
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
