/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.osc.bikas.avro;
@org.apache.avro.specific.AvroGenerated
public enum Filter implements org.apache.avro.generic.GenericEnumSymbol<Filter> {
  POPULAR, LOW_TO_HIGH, HIGH_TO_LOW, NEW_FIRST  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"Filter\",\"namespace\":\"com.osc.bikas.avro\",\"symbols\":[\"POPULAR\",\"LOW_TO_HIGH\",\"HIGH_TO_LOW\",\"NEW_FIRST\"]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}