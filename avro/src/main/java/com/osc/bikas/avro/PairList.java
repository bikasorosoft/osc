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
public class PairList extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = -1557798334106419227L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"PairList\",\"namespace\":\"com.osc.bikas.avro\",\"fields\":[{\"name\":\"pairList\",\"type\":{\"type\":\"array\",\"items\":{\"type\":\"record\",\"name\":\"Pair\",\"fields\":[{\"name\":\"productId\",\"type\":[\"null\",\"string\"],\"default\":null},{\"name\":\"viewCount\",\"type\":\"long\",\",default\":0}]}}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<PairList> ENCODER =
      new BinaryMessageEncoder<PairList>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<PairList> DECODER =
      new BinaryMessageDecoder<PairList>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageEncoder instance used by this class.
   * @return the message encoder used by this class
   */
  public static BinaryMessageEncoder<PairList> getEncoder() {
    return ENCODER;
  }

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   * @return the message decoder used by this class
   */
  public static BinaryMessageDecoder<PairList> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   * @return a BinaryMessageDecoder instance for this class backed by the given SchemaStore
   */
  public static BinaryMessageDecoder<PairList> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<PairList>(MODEL$, SCHEMA$, resolver);
  }

  /**
   * Serializes this PairList to a ByteBuffer.
   * @return a buffer holding the serialized data for this instance
   * @throws java.io.IOException if this instance could not be serialized
   */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /**
   * Deserializes a PairList from a ByteBuffer.
   * @param b a byte buffer holding serialized data for an instance of this class
   * @return a PairList instance decoded from the given buffer
   * @throws java.io.IOException if the given bytes could not be deserialized into an instance of this class
   */
  public static PairList fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private java.util.List<com.osc.bikas.avro.Pair> pairList;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public PairList() {}

  /**
   * All-args constructor.
   * @param pairList The new value for pairList
   */
  public PairList(java.util.List<com.osc.bikas.avro.Pair> pairList) {
    this.pairList = pairList;
  }

  public org.apache.avro.specific.SpecificData getSpecificData() { return MODEL$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return pairList;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: pairList = (java.util.List<com.osc.bikas.avro.Pair>)value$; break;
    default: throw new IndexOutOfBoundsException("Invalid index: " + field$);
    }
  }

  /**
   * Gets the value of the 'pairList' field.
   * @return The value of the 'pairList' field.
   */
  public java.util.List<com.osc.bikas.avro.Pair> getPairList() {
    return pairList;
  }


  /**
   * Sets the value of the 'pairList' field.
   * @param value the value to set.
   */
  public void setPairList(java.util.List<com.osc.bikas.avro.Pair> value) {
    this.pairList = value;
  }

  /**
   * Creates a new PairList RecordBuilder.
   * @return A new PairList RecordBuilder
   */
  public static com.osc.bikas.avro.PairList.Builder newBuilder() {
    return new com.osc.bikas.avro.PairList.Builder();
  }

  /**
   * Creates a new PairList RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new PairList RecordBuilder
   */
  public static com.osc.bikas.avro.PairList.Builder newBuilder(com.osc.bikas.avro.PairList.Builder other) {
    if (other == null) {
      return new com.osc.bikas.avro.PairList.Builder();
    } else {
      return new com.osc.bikas.avro.PairList.Builder(other);
    }
  }

  /**
   * Creates a new PairList RecordBuilder by copying an existing PairList instance.
   * @param other The existing instance to copy.
   * @return A new PairList RecordBuilder
   */
  public static com.osc.bikas.avro.PairList.Builder newBuilder(com.osc.bikas.avro.PairList other) {
    if (other == null) {
      return new com.osc.bikas.avro.PairList.Builder();
    } else {
      return new com.osc.bikas.avro.PairList.Builder(other);
    }
  }

  /**
   * RecordBuilder for PairList instances.
   */
  @org.apache.avro.specific.AvroGenerated
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<PairList>
    implements org.apache.avro.data.RecordBuilder<PairList> {

    private java.util.List<com.osc.bikas.avro.Pair> pairList;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.osc.bikas.avro.PairList.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.pairList)) {
        this.pairList = data().deepCopy(fields()[0].schema(), other.pairList);
        fieldSetFlags()[0] = other.fieldSetFlags()[0];
      }
    }

    /**
     * Creates a Builder by copying an existing PairList instance
     * @param other The existing instance to copy.
     */
    private Builder(com.osc.bikas.avro.PairList other) {
      super(SCHEMA$);
      if (isValidValue(fields()[0], other.pairList)) {
        this.pairList = data().deepCopy(fields()[0].schema(), other.pairList);
        fieldSetFlags()[0] = true;
      }
    }

    /**
      * Gets the value of the 'pairList' field.
      * @return The value.
      */
    public java.util.List<com.osc.bikas.avro.Pair> getPairList() {
      return pairList;
    }


    /**
      * Sets the value of the 'pairList' field.
      * @param value The value of 'pairList'.
      * @return This builder.
      */
    public com.osc.bikas.avro.PairList.Builder setPairList(java.util.List<com.osc.bikas.avro.Pair> value) {
      validate(fields()[0], value);
      this.pairList = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'pairList' field has been set.
      * @return True if the 'pairList' field has been set, false otherwise.
      */
    public boolean hasPairList() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'pairList' field.
      * @return This builder.
      */
    public com.osc.bikas.avro.PairList.Builder clearPairList() {
      pairList = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public PairList build() {
      try {
        PairList record = new PairList();
        record.pairList = fieldSetFlags()[0] ? this.pairList : (java.util.List<com.osc.bikas.avro.Pair>) defaultValue(fields()[0]);
        return record;
      } catch (org.apache.avro.AvroMissingFieldException e) {
        throw e;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<PairList>
    WRITER$ = (org.apache.avro.io.DatumWriter<PairList>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<PairList>
    READER$ = (org.apache.avro.io.DatumReader<PairList>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

  @Override protected boolean hasCustomCoders() { return true; }

  @Override public void customEncode(org.apache.avro.io.Encoder out)
    throws java.io.IOException
  {
    long size0 = this.pairList.size();
    out.writeArrayStart();
    out.setItemCount(size0);
    long actualSize0 = 0;
    for (com.osc.bikas.avro.Pair e0: this.pairList) {
      actualSize0++;
      out.startItem();
      e0.customEncode(out);
    }
    out.writeArrayEnd();
    if (actualSize0 != size0)
      throw new java.util.ConcurrentModificationException("Array-size written was " + size0 + ", but element count was " + actualSize0 + ".");

  }

  @Override public void customDecode(org.apache.avro.io.ResolvingDecoder in)
    throws java.io.IOException
  {
    org.apache.avro.Schema.Field[] fieldOrder = in.readFieldOrderIfDiff();
    if (fieldOrder == null) {
      long size0 = in.readArrayStart();
      java.util.List<com.osc.bikas.avro.Pair> a0 = this.pairList;
      if (a0 == null) {
        a0 = new SpecificData.Array<com.osc.bikas.avro.Pair>((int)size0, SCHEMA$.getField("pairList").schema());
        this.pairList = a0;
      } else a0.clear();
      SpecificData.Array<com.osc.bikas.avro.Pair> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.osc.bikas.avro.Pair>)a0 : null);
      for ( ; 0 < size0; size0 = in.arrayNext()) {
        for ( ; size0 != 0; size0--) {
          com.osc.bikas.avro.Pair e0 = (ga0 != null ? ga0.peek() : null);
          if (e0 == null) {
            e0 = new com.osc.bikas.avro.Pair();
          }
          e0.customDecode(in);
          a0.add(e0);
        }
      }

    } else {
      for (int i = 0; i < 1; i++) {
        switch (fieldOrder[i].pos()) {
        case 0:
          long size0 = in.readArrayStart();
          java.util.List<com.osc.bikas.avro.Pair> a0 = this.pairList;
          if (a0 == null) {
            a0 = new SpecificData.Array<com.osc.bikas.avro.Pair>((int)size0, SCHEMA$.getField("pairList").schema());
            this.pairList = a0;
          } else a0.clear();
          SpecificData.Array<com.osc.bikas.avro.Pair> ga0 = (a0 instanceof SpecificData.Array ? (SpecificData.Array<com.osc.bikas.avro.Pair>)a0 : null);
          for ( ; 0 < size0; size0 = in.arrayNext()) {
            for ( ; size0 != 0; size0--) {
              com.osc.bikas.avro.Pair e0 = (ga0 != null ? ga0.peek() : null);
              if (e0 == null) {
                e0 = new com.osc.bikas.avro.Pair();
              }
              e0.customDecode(in);
              a0.add(e0);
            }
          }
          break;

        default:
          throw new java.io.IOException("Corrupt ResolvingDecoder.");
        }
      }
    }
  }
}










