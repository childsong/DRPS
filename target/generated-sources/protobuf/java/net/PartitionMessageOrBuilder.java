// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ps.proto

package net;

public interface PartitionMessageOrBuilder extends
    // @@protoc_insertion_point(interface_extends:net.PartitionMessage)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>optional string key = 1;</code>
   */
  java.lang.String getKey();
  /**
   * <code>optional string key = 1;</code>
   */
  com.google.protobuf.ByteString
      getKeyBytes();

  /**
   * <code>repeated int32 value = 2;</code>
   */
  java.util.List<java.lang.Integer> getValueList();
  /**
   * <code>repeated int32 value = 2;</code>
   */
  int getValueCount();
  /**
   * <code>repeated int32 value = 2;</code>
   */
  int getValue(int index);
}