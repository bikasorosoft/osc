/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.osc.bikas.avro;

import org.apache.avro.generic.GenericArray;
import org.apache.avro.specific.SpecificData;
import org.apache.avro.util.Utf8;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@org.apache.avro.specific.AvroGenerated
public class UserRegistrationDetail extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 5474754934261725324L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UserRegistrationDetail\",\"namespace\":\"com.osc.bikas.avro\",\"fields\":[{\"name\":\"userId\",\"type\":\"string\"},{\"name\":\"name\",\"type\":\"string\"},{\"name\":\"email\",\"type\":\"string\"},{\"name\":\"contact\",\"type\":\"string\"},{\"name\":\"DOB\",\"type\":{\"type\":\"int\",\"logicalType\":\"date\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();
static {
    MODEL$.addLogicalTypeConversion(new org.apache.avro.data.TimeConversions.DateConversion());
  }

  private static final BinaryMessageEncoder<UserRegistrationDetail> ENCODER =
      new BinaryMessageEncoder<UserRegistrationDetail>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<UserRegistrationDetail> DECODER =
      new BinaryMessageDecoder<UserRegistrationDetail>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<UserRegistrationDetail> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<UserRegistrationDetail> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<UserRegistrationDetail> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<UserRegistrationDetail>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this UserRegistrationDetail to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a UserRegistrationDetail from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a UserRegistrationDetail instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static UserRegistrationDetail fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence userId;
   private java.lang.CharSequence name;
   private java.lang.CharSequence email;
   private java.lang.CharSequence contact;
   private java.time.LocalDate DOB;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public UserRegistrationDetail() {}

  /**
   * All-args constructor.
   * @param userId The new value for userId
   * @param name The new value for name
   * @param email The new value for email
   * @param contact The new value for contact
   * @param DOB The new value for DOB
   */
  public UserRegistrationDetail(java.lang.CharSequence userId, java.lang.CharSequence name, java.lang.CharSequence email, java.lang.CharSequence contact, java.time.LocalDate DOB) {
    this.userId = userId;
    this.name = name;
    this.email = email;
    this.contact = contact;
    this.DOB = DOB;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return userId;
    case 1: return name;
    case 2: return email;
    case 3: return contact;
    case 4: return DOB;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  private static final org.apache.avro.Conversion<?>[] conversions =
      new org.apache.avro.Conversion<?>[] {
      null,
      null,
      null,
      null,
      new org.apache.avro.data.TimeConversions.DateConversion(),
      null
  };

  @Override
  public org.apache.avro.Conversion<?> getConversion(int field) {
    return conversions[field];
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: userId = (java.lang.CharSequence)value$; break;
    case 1: name = (java.lang.CharSequence)value$; break;
    case 2: email = (java.lang.CharSequence)value$; break;
    case 3: contact = (java.lang.CharSequence)value$; break;
    case 4: DOB = (java.time.LocalDate)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'userId' field.
   * @return The value of the 'userId' field.
   */
  public java.lang.CharSequence getUserId() {
    return userId;
  }


  /**
   * Sets the value of the 'userId' field.
   * @param value the value to set.
   */
  public void setUserId(java.lang.CharSequence value) {
    this.userId = value;
  }

  /**
   * Gets the value of the 'name' field.
   * @return The value of the 'name' field.
   */
  public java.lang.CharSequence getName() {
    return name;
  }


  /**
   * Sets the value of the 'name' field.
   * @param value the value to set.
   */
  public void setName(java.lang.CharSequence value) {
    this.name = value;
  }

  /**
   * Gets the value of the 'email' field.
   * @return The value of the 'email' field.
   */
  public java.lang.CharSequence getEmail() {
    return email;
  }


  /**
   * Sets the value of the 'email' field.
   * @param value the value to set.
   */
  public void setEmail(java.lang.CharSequence value) {
    this.email = value;
  }

  /**
   * Gets the value of the 'contact' field.
   * @return The value of the 'contact' field.
   */
  public java.lang.CharSequence getContact() {
    return contact;
  }


  /**
   * Sets the value of the 'contact' field.
   * @param value the value to set.
   */
  public void setContact(java.lang.CharSequence value) {
    this.contact = value;
  }

  /**
   * Gets the value of the 'DOB' field.
   * @return The value of the 'DOB' field.
   */
  public java.time.LocalDate getDOB() {
    return DOB;
  }


  /**
   * Sets the value of the 'DOB' field.
   * @param value the value to set.
   */
  public void setDOB(java.time.LocalDate value) {
    this.DOB = value;
  }

  /**
   * Creates a new UserRegistrationDetail RecordBuilder.
   * @return A new UserRegistrationDetail RecordBuilder
   */
  public static com.osc.bikas.avro.UserRegistrationDetail.Builder newBuilder() {
    return new com.osc.bikas.avro.UserRegistrationDetail.Builder();
  }

  /**
   * Creates a new UserRegistrationDetail RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new UserRegistrationDetail RecordBuilder
   */
  public static com.osc.bikas.avro.UserRegistrationDetail.Builder newBuilder(com.osc.bikas.avro.UserRegistrationDetail.Builder other) {
    if (other == null) {
      return new com.osc.bikas.avro.UserRegistrationDetail.Builder();
    } else {
      return new com.osc.bikas.avro.UserRegistrationDetail.Builder(other);
    }
  }

  /**
   * Creates a new UserRegistrationDetail RecordBuilder by copying an existing UserRegistrationDetail instance.
   * @param other The existing instance to copy.
   * @return A new UserRegistrationDetail RecordBuilder
   */
  public static com.osc.bikas.avro.UserRegistrationDetail.Builder newBuilder(com.osc.bikas.avro.UserRegistrationDetail other) {
    if (other == null) {
      return new com.osc.bikas.avro.UserRegistrationDetail.Builder();
    } else {
      return new com.osc.bikas.avro.UserRegistrationDetail.Builder(other);
    }
  }

  /**
   * RecordBuilder for UserRegistrationDetail instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<UserRegistrationDetail>
    implements org.apache.avro.data.RecordBuilder<UserRegistrationDetail> {

    private java.lang.CharSequence userId;
    private java.lang.CharSequence name;
    private java.lang.CharSequence email;
    private java.lang.CharSequence contact;
    private java.time.LocalDate DOB;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.osc.bikas.avro.UserRegistrationDetail.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
      if (isValidValue(fields()[2], other.email)) {
        this.email = data().deepCopy(fields()[2].schema(), other.email);
        fieldSetFlags()[2] = other.fieldSetFlags()[2];
      }
      if (isValidValue(fields()[3], other.contact)) {
        this.contact = data().deepCopy(fields()[3].schema(), other.contact);
        fieldSetFlags()[3] = other.fieldSetFlags()[3];
      }
      if (isValidValue(fields()[4], other.DOB)) {
        this.DOB = data().deepCopy(fields()[4].schema(), other.DOB);
        fieldSetFlags()[4] = other.fieldSetFlags()[4];
      }
    }

    /**
     * Creates a Builder by copying an existing UserRegistrationDetail instance
     * @param other The existing instance to copy.
     */
    private Builder(com.osc.bikas.avro.UserRegistrationDetail other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.name)) {
        this.name = data().deepCopy(fields()[1].schema(), other.name);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.email)) {
        this.email = data().deepCopy(fields()[2].schema(), other.email);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.contact)) {
        this.contact = data().deepCopy(fields()[3].schema(), other.contact);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.DOB)) {
        this.DOB = data().deepCopy(fields()[4].schema(), other.DOB);
        fieldSetFlags()[4] = true;
      }
    }

    /**
      * Gets the value of the 'userId' field.
      * @return The value.
      */
    public java.lang.CharSequence getUserId() {
      return userId;
    }


    /**
      * Sets the value of the 'userId' field.
      * @param value The value of 'userId'.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder setUserId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.userId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'userId' field has been set.
      * @return True if the 'userId' field has been set, false otherwise.
      */
    public boolean hasUserId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'userId' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder clearUserId() {
      userId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'name' field.
      * @return The value.
      */
    public java.lang.CharSequence getName() {
      return name;
    }


    /**
      * Sets the value of the 'name' field.
      * @param value The value of 'name'.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder setName(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.name = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'name' field has been set.
      * @return True if the 'name' field has been set, false otherwise.
      */
    public boolean hasName() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'name' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder clearName() {
      name = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'email' field.
      * @return The value.
      */
    public java.lang.CharSequence getEmail() {
      return email;
    }


    /**
      * Sets the value of the 'email' field.
      * @param value The value of 'email'.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder setEmail(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.email = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'email' field has been set.
      * @return True if the 'email' field has been set, false otherwise.
      */
    public boolean hasEmail() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'email' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder clearEmail() {
      email = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'contact' field.
      * @return The value.
      */
    public java.lang.CharSequence getContact() {
      return contact;
    }


    /**
      * Sets the value of the 'contact' field.
      * @param value The value of 'contact'.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder setContact(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.contact = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'contact' field has been set.
      * @return True if the 'contact' field has been set, false otherwise.
      */
    public boolean hasContact() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'contact' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder clearContact() {
      contact = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'DOB' field.
      * @return The value.
      */
    public java.time.LocalDate getDOB() {
      return DOB;
    }


    /**
      * Sets the value of the 'DOB' field.
      * @param value The value of 'DOB'.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder setDOB(java.time.LocalDate value) {
      validate(fields()[4], value);
      this.DOB = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'DOB' field has been set.
      * @return True if the 'DOB' field has been set, false otherwise.
      */
    public boolean hasDOB() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'DOB' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.UserRegistrationDetail.Builder clearDOB() {
      fieldSetFlags()[4] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserRegistrationDetail build() {
      try {
        UserRegistrationDetail record = new UserRegistrationDetail();
        record.userId = fieldSetFlags()[0] ? this.userId : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.name = fieldSetFlags()[1] ? this.name : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.email = fieldSetFlags()[2] ? this.email : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.contact = fieldSetFlags()[3] ? this.contact : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.DOB = fieldSetFlags()[4] ? this.DOB : (java.time.LocalDate) defaultValue(fields()[4]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<UserRegistrationDetail>
    WRITER$ = (org.apache.avro.io.DatumWriter<UserRegistrationDetail>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<UserRegistrationDetail>
    READER$ = (org.apache.avro.io.DatumReader<UserRegistrationDetail>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}










