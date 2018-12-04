// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: ps.proto

package net;

/**
 * Protobuf type {@code net.MatrixMessage}
 */
public  final class MatrixMessage extends
    com.google.protobuf.GeneratedMessage implements
    // @@protoc_insertion_point(message_implements:net.MatrixMessage)
    MatrixMessageOrBuilder {
  // Use MatrixMessage.newBuilder() to construct.
  private MatrixMessage(com.google.protobuf.GeneratedMessage.Builder<?> builder) {
    super(builder);
  }
  private MatrixMessage() {
    key_ = "";
    row_ = 0;
    cols_ = 0;
    data_ = java.util.Collections.emptyList();
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
  }
  private MatrixMessage(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry) {
    this();
    int mutable_bitField0_ = 0;
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!input.skipField(tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            java.lang.String s = input.readStringRequireUtf8();

            key_ = s;
            break;
          }
          case 16: {

            row_ = input.readInt32();
            break;
          }
          case 24: {

            cols_ = input.readInt32();
            break;
          }
          case 37: {
            if (!((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
              data_ = new java.util.ArrayList<java.lang.Float>();
              mutable_bitField0_ |= 0x00000008;
            }
            data_.add(input.readFloat());
            break;
          }
          case 34: {
            int length = input.readRawVarint32();
            int limit = input.pushLimit(length);
            if (!((mutable_bitField0_ & 0x00000008) == 0x00000008) && input.getBytesUntilLimit() > 0) {
              data_ = new java.util.ArrayList<java.lang.Float>();
              mutable_bitField0_ |= 0x00000008;
            }
            while (input.getBytesUntilLimit() > 0) {
              data_.add(input.readFloat());
            }
            input.popLimit(limit);
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw new RuntimeException(e.setUnfinishedMessage(this));
    } catch (java.io.IOException e) {
      throw new RuntimeException(
          new com.google.protobuf.InvalidProtocolBufferException(
              e.getMessage()).setUnfinishedMessage(this));
    } finally {
      if (((mutable_bitField0_ & 0x00000008) == 0x00000008)) {
        data_ = java.util.Collections.unmodifiableList(data_);
      }
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return net.Ps.internal_static_net_MatrixMessage_descriptor;
  }

  protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return net.Ps.internal_static_net_MatrixMessage_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            net.MatrixMessage.class, net.MatrixMessage.Builder.class);
  }

  private int bitField0_;
  public static final int KEY_FIELD_NUMBER = 1;
  private volatile java.lang.Object key_;
  /**
   * <code>optional string key = 1;</code>
   */
  public java.lang.String getKey() {
    java.lang.Object ref = key_;
    if (ref instanceof java.lang.String) {
      return (java.lang.String) ref;
    } else {
      com.google.protobuf.ByteString bs = 
          (com.google.protobuf.ByteString) ref;
      java.lang.String s = bs.toStringUtf8();
      key_ = s;
      return s;
    }
  }
  /**
   * <code>optional string key = 1;</code>
   */
  public com.google.protobuf.ByteString
      getKeyBytes() {
    java.lang.Object ref = key_;
    if (ref instanceof java.lang.String) {
      com.google.protobuf.ByteString b = 
          com.google.protobuf.ByteString.copyFromUtf8(
              (java.lang.String) ref);
      key_ = b;
      return b;
    } else {
      return (com.google.protobuf.ByteString) ref;
    }
  }

  public static final int ROW_FIELD_NUMBER = 2;
  private int row_;
  /**
   * <code>optional int32 row = 2;</code>
   */
  public int getRow() {
    return row_;
  }

  public static final int COLS_FIELD_NUMBER = 3;
  private int cols_;
  /**
   * <code>optional int32 cols = 3;</code>
   */
  public int getCols() {
    return cols_;
  }

  public static final int DATA_FIELD_NUMBER = 4;
  private java.util.List<java.lang.Float> data_;
  /**
   * <code>repeated float data = 4;</code>
   */
  public java.util.List<java.lang.Float>
      getDataList() {
    return data_;
  }
  /**
   * <code>repeated float data = 4;</code>
   */
  public int getDataCount() {
    return data_.size();
  }
  /**
   * <code>repeated float data = 4;</code>
   */
  public float getData(int index) {
    return data_.get(index);
  }
  private int dataMemoizedSerializedSize = -1;

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    getSerializedSize();
    if (!getKeyBytes().isEmpty()) {
      com.google.protobuf.GeneratedMessage.writeString(output, 1, key_);
    }
    if (row_ != 0) {
      output.writeInt32(2, row_);
    }
    if (cols_ != 0) {
      output.writeInt32(3, cols_);
    }
    if (getDataList().size() > 0) {
      output.writeRawVarint32(34);
      output.writeRawVarint32(dataMemoizedSerializedSize);
    }
    for (int i = 0; i < data_.size(); i++) {
      output.writeFloatNoTag(data_.get(i));
    }
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (!getKeyBytes().isEmpty()) {
      size += com.google.protobuf.GeneratedMessage.computeStringSize(1, key_);
    }
    if (row_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, row_);
    }
    if (cols_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(3, cols_);
    }
    {
      int dataSize = 0;
      dataSize = 4 * getDataList().size();
      size += dataSize;
      if (!getDataList().isEmpty()) {
        size += 1;
        size += com.google.protobuf.CodedOutputStream
            .computeInt32SizeNoTag(dataSize);
      }
      dataMemoizedSerializedSize = dataSize;
    }
    memoizedSize = size;
    return size;
  }

  private static final long serialVersionUID = 0L;
  public static net.MatrixMessage parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.MatrixMessage parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.MatrixMessage parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static net.MatrixMessage parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static net.MatrixMessage parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static net.MatrixMessage parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }
  public static net.MatrixMessage parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input);
  }
  public static net.MatrixMessage parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseDelimitedFrom(input, extensionRegistry);
  }
  public static net.MatrixMessage parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return PARSER.parseFrom(input);
  }
  public static net.MatrixMessage parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return PARSER.parseFrom(input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(net.MatrixMessage prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessage.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code net.MatrixMessage}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessage.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:net.MatrixMessage)
      net.MatrixMessageOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return net.Ps.internal_static_net_MatrixMessage_descriptor;
    }

    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return net.Ps.internal_static_net_MatrixMessage_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              net.MatrixMessage.class, net.MatrixMessage.Builder.class);
    }

    // Construct using net.MatrixMessage.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      key_ = "";

      row_ = 0;

      cols_ = 0;

      data_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000008);
      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return net.Ps.internal_static_net_MatrixMessage_descriptor;
    }

    public net.MatrixMessage getDefaultInstanceForType() {
      return net.MatrixMessage.getDefaultInstance();
    }

    public net.MatrixMessage build() {
      net.MatrixMessage result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public net.MatrixMessage buildPartial() {
      net.MatrixMessage result = new net.MatrixMessage(this);
      int from_bitField0_ = bitField0_;
      int to_bitField0_ = 0;
      result.key_ = key_;
      result.row_ = row_;
      result.cols_ = cols_;
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        data_ = java.util.Collections.unmodifiableList(data_);
        bitField0_ = (bitField0_ & ~0x00000008);
      }
      result.data_ = data_;
      result.bitField0_ = to_bitField0_;
      onBuilt();
      return result;
    }

    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof net.MatrixMessage) {
        return mergeFrom((net.MatrixMessage)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(net.MatrixMessage other) {
      if (other == net.MatrixMessage.getDefaultInstance()) return this;
      if (!other.getKey().isEmpty()) {
        key_ = other.key_;
        onChanged();
      }
      if (other.getRow() != 0) {
        setRow(other.getRow());
      }
      if (other.getCols() != 0) {
        setCols(other.getCols());
      }
      if (!other.data_.isEmpty()) {
        if (data_.isEmpty()) {
          data_ = other.data_;
          bitField0_ = (bitField0_ & ~0x00000008);
        } else {
          ensureDataIsMutable();
          data_.addAll(other.data_);
        }
        onChanged();
      }
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      net.MatrixMessage parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (net.MatrixMessage) e.getUnfinishedMessage();
        throw e;
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }
    private int bitField0_;

    private java.lang.Object key_ = "";
    /**
     * <code>optional string key = 1;</code>
     */
    public java.lang.String getKey() {
      java.lang.Object ref = key_;
      if (!(ref instanceof java.lang.String)) {
        com.google.protobuf.ByteString bs =
            (com.google.protobuf.ByteString) ref;
        java.lang.String s = bs.toStringUtf8();
        key_ = s;
        return s;
      } else {
        return (java.lang.String) ref;
      }
    }
    /**
     * <code>optional string key = 1;</code>
     */
    public com.google.protobuf.ByteString
        getKeyBytes() {
      java.lang.Object ref = key_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8(
                (java.lang.String) ref);
        key_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    /**
     * <code>optional string key = 1;</code>
     */
    public Builder setKey(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      key_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional string key = 1;</code>
     */
    public Builder clearKey() {
      
      key_ = getDefaultInstance().getKey();
      onChanged();
      return this;
    }
    /**
     * <code>optional string key = 1;</code>
     */
    public Builder setKeyBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      key_ = value;
      onChanged();
      return this;
    }

    private int row_ ;
    /**
     * <code>optional int32 row = 2;</code>
     */
    public int getRow() {
      return row_;
    }
    /**
     * <code>optional int32 row = 2;</code>
     */
    public Builder setRow(int value) {
      
      row_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 row = 2;</code>
     */
    public Builder clearRow() {
      
      row_ = 0;
      onChanged();
      return this;
    }

    private int cols_ ;
    /**
     * <code>optional int32 cols = 3;</code>
     */
    public int getCols() {
      return cols_;
    }
    /**
     * <code>optional int32 cols = 3;</code>
     */
    public Builder setCols(int value) {
      
      cols_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>optional int32 cols = 3;</code>
     */
    public Builder clearCols() {
      
      cols_ = 0;
      onChanged();
      return this;
    }

    private java.util.List<java.lang.Float> data_ = java.util.Collections.emptyList();
    private void ensureDataIsMutable() {
      if (!((bitField0_ & 0x00000008) == 0x00000008)) {
        data_ = new java.util.ArrayList<java.lang.Float>(data_);
        bitField0_ |= 0x00000008;
       }
    }
    /**
     * <code>repeated float data = 4;</code>
     */
    public java.util.List<java.lang.Float>
        getDataList() {
      return java.util.Collections.unmodifiableList(data_);
    }
    /**
     * <code>repeated float data = 4;</code>
     */
    public int getDataCount() {
      return data_.size();
    }
    /**
     * <code>repeated float data = 4;</code>
     */
    public float getData(int index) {
      return data_.get(index);
    }
    /**
     * <code>repeated float data = 4;</code>
     */
    public Builder setData(
        int index, float value) {
      ensureDataIsMutable();
      data_.set(index, value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated float data = 4;</code>
     */
    public Builder addData(float value) {
      ensureDataIsMutable();
      data_.add(value);
      onChanged();
      return this;
    }
    /**
     * <code>repeated float data = 4;</code>
     */
    public Builder addAllData(
        java.lang.Iterable<? extends java.lang.Float> values) {
      ensureDataIsMutable();
      com.google.protobuf.AbstractMessageLite.Builder.addAll(
          values, data_);
      onChanged();
      return this;
    }
    /**
     * <code>repeated float data = 4;</code>
     */
    public Builder clearData() {
      data_ = java.util.Collections.emptyList();
      bitField0_ = (bitField0_ & ~0x00000008);
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return this;
    }


    // @@protoc_insertion_point(builder_scope:net.MatrixMessage)
  }

  // @@protoc_insertion_point(class_scope:net.MatrixMessage)
  private static final net.MatrixMessage DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new net.MatrixMessage();
  }

  public static net.MatrixMessage getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<MatrixMessage>
      PARSER = new com.google.protobuf.AbstractParser<MatrixMessage>() {
    public MatrixMessage parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      try {
        return new MatrixMessage(input, extensionRegistry);
      } catch (RuntimeException e) {
        if (e.getCause() instanceof
            com.google.protobuf.InvalidProtocolBufferException) {
          throw (com.google.protobuf.InvalidProtocolBufferException)
              e.getCause();
        }
        throw e;
      }
    }
  };

  public static com.google.protobuf.Parser<MatrixMessage> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<MatrixMessage> getParserForType() {
    return PARSER;
  }

  public net.MatrixMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

