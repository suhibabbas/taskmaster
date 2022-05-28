package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the user type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "users")
public final class user implements Model {
  public static final QueryField ID = field("user", "id");
  public static final QueryField REGISTRATION_NUMBER = field("user", "registrationNumber");
  public static final QueryField NAME = field("user", "name");
  public static final QueryField AGE = field("user", "age");
  public static final QueryField NUMBER = field("user", "number");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="ID", isRequired = true) String registrationNumber;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="Int") Integer age;
  private final @ModelField(targetType="String") String number;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getRegistrationNumber() {
      return registrationNumber;
  }
  
  public String getName() {
      return name;
  }
  
  public Integer getAge() {
      return age;
  }
  
  public String getNumber() {
      return number;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private user(String id, String registrationNumber, String name, Integer age, String number) {
    this.id = id;
    this.registrationNumber = registrationNumber;
    this.name = name;
    this.age = age;
    this.number = number;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      user user = (user) obj;
      return ObjectsCompat.equals(getId(), user.getId()) &&
              ObjectsCompat.equals(getRegistrationNumber(), user.getRegistrationNumber()) &&
              ObjectsCompat.equals(getName(), user.getName()) &&
              ObjectsCompat.equals(getAge(), user.getAge()) &&
              ObjectsCompat.equals(getNumber(), user.getNumber()) &&
              ObjectsCompat.equals(getCreatedAt(), user.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), user.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getRegistrationNumber())
      .append(getName())
      .append(getAge())
      .append(getNumber())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("user {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("registrationNumber=" + String.valueOf(getRegistrationNumber()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("age=" + String.valueOf(getAge()) + ", ")
      .append("number=" + String.valueOf(getNumber()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static RegistrationNumberStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static user justId(String id) {
    return new user(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      registrationNumber,
      name,
      age,
      number);
  }
  public interface RegistrationNumberStep {
    NameStep registrationNumber(String registrationNumber);
  }
  

  public interface NameStep {
    BuildStep name(String name);
  }
  

  public interface BuildStep {
    user build();
    BuildStep id(String id);
    BuildStep age(Integer age);
    BuildStep number(String number);
  }
  

  public static class Builder implements RegistrationNumberStep, NameStep, BuildStep {
    private String id;
    private String registrationNumber;
    private String name;
    private Integer age;
    private String number;
    @Override
     public user build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new user(
          id,
          registrationNumber,
          name,
          age,
          number);
    }
    
    @Override
     public NameStep registrationNumber(String registrationNumber) {
        Objects.requireNonNull(registrationNumber);
        this.registrationNumber = registrationNumber;
        return this;
    }
    
    @Override
     public BuildStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public BuildStep age(Integer age) {
        this.age = age;
        return this;
    }
    
    @Override
     public BuildStep number(String number) {
        this.number = number;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String registrationNumber, String name, Integer age, String number) {
      super.id(id);
      super.registrationNumber(registrationNumber)
        .name(name)
        .age(age)
        .number(number);
    }
    
    @Override
     public CopyOfBuilder registrationNumber(String registrationNumber) {
      return (CopyOfBuilder) super.registrationNumber(registrationNumber);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder age(Integer age) {
      return (CopyOfBuilder) super.age(age);
    }
    
    @Override
     public CopyOfBuilder number(String number) {
      return (CopyOfBuilder) super.number(number);
    }
  }
  
}
