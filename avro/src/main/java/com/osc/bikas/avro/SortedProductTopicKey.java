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
public class SortedProductTopicKey extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 6862205340081057337L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"SortedProductTopicKey\",\"namespace\":\"com.osc.bikas.avro\",\"fields\":[{\"name\":\"categoryId\",\"type\":\"string\"},{\"name\":\"filter\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<SortedProductTopicKey> ENCODER =
      new BinaryMessageEncoder<SortedProductTopicKey>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<SortedProductTopicKey> DECODER =
      new BinaryMessageDecoder<SortedProductTopicKey>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<SortedProductTopicKey> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<SortedProductTopicKey> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<SortedProductTopicKey> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<SortedProductTopicKey>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this SortedProductTopicKey to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a SortedProductTopicKey from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a SortedProductTopicKey instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static SortedProductTopicKey fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.lang.CharSequence categoryId;
   private java.lang.CharSequence filter;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public SortedProductTopicKey() {}

  /**
   * All-args constructor.
   * @param categoryId The new value for categoryId
   * @param filter The new value for filter
   */
  public SortedProductTopicKey(java.lang.CharSequence categoryId, java.lang.CharSequence filter) {
    this.categoryId = categoryId;
    this.filter = filter;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return categoryId;
    case 1: return filter;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: categoryId = (java.lang.CharSequence)value$; break;
    case 1: filter = (java.lang.CharSequence)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'categoryId' field.
   * @return The value of the 'categoryId' field.
   */
  public java.lang.CharSequence getCategoryId() {
    return categoryId;
  }


  /**
   * Sets the value of the 'categoryId' field.
   * @param value the value to set.
   */
  public void setCategoryId(java.lang.CharSequence value) {
    this.categoryId = value;
  }

  /**
   * Gets the value of the 'filter' field.
   * @return The value of the 'filter' field.
   */
  public java.lang.CharSequence getFilter() {
    return filter;
  }


  /**
   * Sets the value of the 'filter' field.
   * @param value the value to set.
   */
  public void setFilter(java.lang.CharSequence value) {
    this.filter = value;
  }

  /**
   * Creates a new SortedProductTopicKey RecordBuilder.
   * @return A new SortedProductTopicKey RecordBuilder
   */
  public static com.osc.bikas.avro.SortedProductTopicKey.Builder newBuilder() {
    return new com.osc.bikas.avro.SortedProductTopicKey.Builder();
  }

  /**
   * Creates a new SortedProductTopicKey RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new SortedProductTopicKey RecordBuilder
   */
  public static com.osc.bikas.avro.SortedProductTopicKey.Builder newBuilder(com.osc.bikas.avro.SortedProductTopicKey.Builder other) {
    if (other == null) {
      return new com.osc.bikas.avro.SortedProductTopicKey.Builder();
    } else {
      return new com.osc.bikas.avro.SortedProductTopicKey.Builder(other);
    }
  }

  /**
   * Creates a new SortedProductTopicKey RecordBuilder by copying an existing SortedProductTopicKey instance.
   * @param other The existing instance to copy.
   * @return A new SortedProductTopicKey RecordBuilder
   */
  public static com.osc.bikas.avro.SortedProductTopicKey.Builder newBuilder(com.osc.bikas.avro.SortedProductTopicKey other) {
    if (other == null) {
      return new com.osc.bikas.avro.SortedProductTopicKey.Builder();
    } else {
      return new com.osc.bikas.avro.SortedProductTopicKey.Builder(other);
    }
  }

  /**
   * RecordBuilder for SortedProductTopicKey instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<SortedProductTopicKey>
    implements org.apache.avro.data.RecordBuilder<SortedProductTopicKey> {

    private java.lang.CharSequence categoryId;
    private java.lang.CharSequence filter;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.osc.bikas.avro.SortedProductTopicKey.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.categoryId)) {
        this.categoryId = data().deepCopy(fields()[0].schema(), other.categoryId);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
      if (isValidValue(fields()[1], other.filter)) {
        this.filter = data().deepCopy(fields()[1].schema(), other.filter);
        fieldSetFlags()[1] = other.fieldSetFlags()[1];
      }
    }

    /**
     * Creates a Builder by copying an existing SortedProductTopicKey instance
     * @param other The existing instance to copy.
     */
    private Builder(com.osc.bikas.avro.SortedProductTopicKey other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.categoryId)) {
        this.categoryId = data().deepCopy(fields()[0].schema(), other.categoryId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.filter)) {
        this.filter = data().deepCopy(fields()[1].schema(), other.filter);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'categoryId' field.
      * @return The value.
      */
    public java.lang.CharSequence getCategoryId() {
      return categoryId;
    }


    /**
      * Sets the value of the 'categoryId' field.
      * @param value The value of 'categoryId'.
      * @return This builder.
      */
    public com.osc.bikas.avro.SortedProductTopicKey.Builder setCategoryId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.categoryId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'categoryId' field has been set.
      * @return True if the 'categoryId' field has been set, false otherwise.
      */
    public boolean hasCategoryId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'categoryId' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.SortedProductTopicKey.Builder clearCategoryId() {
      categoryId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'filter' field.
      * @return The value.
      */
    public java.lang.CharSequence getFilter() {
      return filter;
    }


    /**
      * Sets the value of the 'filter' field.
      * @param value The value of 'filter'.
      * @return This builder.
      */
    public com.osc.bikas.avro.SortedProductTopicKey.Builder setFilter(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.filter = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'filter' field has been set.
      * @return True if the 'filter' field has been set, false otherwise.
      */
    public boolean hasFilter() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'filter' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.SortedProductTopicKey.Builder clearFilter() {
      filter = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SortedProductTopicKey build() {
      try {
        SortedProductTopicKey record = new SortedProductTopicKey();
        record.categoryId = fieldSetFlags()[0] ? this.categoryId : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.filter = fieldSetFlags()[1] ? this.filter : (java.lang.CharSequence) defaultValue(fields()[1]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<SortedProductTopicKey>
    WRITER$ = (org.apache.avro.io.DatumWriter<SortedProductTopicKey>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<SortedProductTopicKey>
    READER$ = (org.apache.avro.io.DatumReader<SortedProductTopicKey>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    out.writeString(this.categoryId);

    out.writeString(this.filter);

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      this.categoryId = in.readString(this.categoryId instanceof Utf8 ? (Utf8)this.categoryId : null);

      this.filter = in.readString(this.filter instanceof Utf8 ? (Utf8)this.filter : null);

    } else {
      for (int i = 0; i < 2; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          this.categoryId = in.readString(this.categoryId instanceof Utf8 ? (Utf8)this.categoryId : null);
          break;

        case 1:
          this.filter = in.readString(this.filter instanceof Utf8 ? (Utf8)this.filter : null);
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










