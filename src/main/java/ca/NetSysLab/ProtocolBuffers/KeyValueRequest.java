// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: proto/KeyValueRequest.proto

package ca.NetSysLab.ProtocolBuffers;

public final class KeyValueRequest {
  private KeyValueRequest() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface KVRequestOrBuilder extends
      // @@protoc_insertion_point(interface_extends:KVRequest)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>required uint32 command = 1;</code>
     */
    boolean hasCommand();
    /**
     * <code>required uint32 command = 1;</code>
     */
    int getCommand();

    /**
     * <code>optional bytes key = 2;</code>
     */
    boolean hasKey();
    /**
     * <code>optional bytes key = 2;</code>
     */
    com.google.protobuf.ByteString getKey();

    /**
     * <code>optional bytes value = 3;</code>
     */
    boolean hasValue();
    /**
     * <code>optional bytes value = 3;</code>
     */
    com.google.protobuf.ByteString getValue();

    /**
     * <code>optional int32 version = 4;</code>
     */
    boolean hasVersion();
    /**
     * <code>optional int32 version = 4;</code>
     */
    int getVersion();
  }
  /**
   * Protobuf type {@code KVRequest}
   */
  public  static final class KVRequest extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:KVRequest)
      KVRequestOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use KVRequest.newBuilder() to construct.
    private KVRequest(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private KVRequest() {
      command_ = 0;
      key_ = com.google.protobuf.ByteString.EMPTY;
      value_ = com.google.protobuf.ByteString.EMPTY;
      version_ = 0;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private KVRequest(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      if (extensionRegistry == null) {
        throw new java.lang.NullPointerException();
      }
      int mutable_bitField0_ = 0;
      com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder();
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            case 8: {
              bitField0_ |= 0x00000001;
              command_ = input.readUInt32();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              key_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              value_ = input.readBytes();
              break;
            }
            case 32: {
              bitField0_ |= 0x00000008;
              version_ = input.readInt32();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        this.unknownFields = unknownFields.build();
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return ca.NetSysLab.ProtocolBuffers.KeyValueRequest.internal_static_KVRequest_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return ca.NetSysLab.ProtocolBuffers.KeyValueRequest.internal_static_KVRequest_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest.class, ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest.Builder.class);
    }

    private int bitField0_;
    public static final int COMMAND_FIELD_NUMBER = 1;
    private int command_;
    /**
     * <code>required uint32 command = 1;</code>
     */
    public boolean hasCommand() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    /**
     * <code>required uint32 command = 1;</code>
     */
    public int getCommand() {
      return command_;
    }

    public static final int KEY_FIELD_NUMBER = 2;
    private com.google.protobuf.ByteString key_;
    /**
     * <code>optional bytes key = 2;</code>
     */
    public boolean hasKey() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    /**
     * <code>optional bytes key = 2;</code>
     */
    public com.google.protobuf.ByteString getKey() {
      return key_;
    }

    public static final int VALUE_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString value_;
    /**
     * <code>optional bytes value = 3;</code>
     */
    public boolean hasValue() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    /**
     * <code>optional bytes value = 3;</code>
     */
    public com.google.protobuf.ByteString getValue() {
      return value_;
    }

    public static final int VERSION_FIELD_NUMBER = 4;
    private int version_;
    /**
     * <code>optional int32 version = 4;</code>
     */
    public boolean hasVersion() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    /**
     * <code>optional int32 version = 4;</code>
     */
    public int getVersion() {
      return version_;
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      if (!hasCommand()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeUInt32(1, command_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, key_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, value_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeInt32(4, version_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt32Size(1, command_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, key_);
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, value_);
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, version_);
      }
      size += unknownFields.getSerializedSize();
      memoizedSize = size;
      return size;
    }

    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest)) {
        return super.equals(obj);
      }
      ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest other = (ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest) obj;

      boolean result = true;
      result = result && (hasCommand() == other.hasCommand());
      if (hasCommand()) {
        result = result && (getCommand()
            == other.getCommand());
      }
      result = result && (hasKey() == other.hasKey());
      if (hasKey()) {
        result = result && getKey()
            .equals(other.getKey());
      }
      result = result && (hasValue() == other.hasValue());
      if (hasValue()) {
        result = result && getValue()
            .equals(other.getValue());
      }
      result = result && (hasVersion() == other.hasVersion());
      if (hasVersion()) {
        result = result && (getVersion()
            == other.getVersion());
      }
      result = result && unknownFields.equals(other.unknownFields);
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasCommand()) {
        hash = (37 * hash) + COMMAND_FIELD_NUMBER;
        hash = (53 * hash) + getCommand();
      }
      if (hasKey()) {
        hash = (37 * hash) + KEY_FIELD_NUMBER;
        hash = (53 * hash) + getKey().hashCode();
      }
      if (hasValue()) {
        hash = (37 * hash) + VALUE_FIELD_NUMBER;
        hash = (53 * hash) + getValue().hashCode();
      }
      if (hasVersion()) {
        hash = (37 * hash) + VERSION_FIELD_NUMBER;
        hash = (53 * hash) + getVersion();
      }
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code KVRequest}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:KVRequest)
        ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequestOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return ca.NetSysLab.ProtocolBuffers.KeyValueRequest.internal_static_KVRequest_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return ca.NetSysLab.ProtocolBuffers.KeyValueRequest.internal_static_KVRequest_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest.class, ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest.Builder.class);
      }

      // Construct using ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      @java.lang.Override
      public Builder clear() {
        super.clear();
        command_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        key_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000002);
        value_ = com.google.protobuf.ByteString.EMPTY;
        bitField0_ = (bitField0_ & ~0x00000004);
        version_ = 0;
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return ca.NetSysLab.ProtocolBuffers.KeyValueRequest.internal_static_KVRequest_descriptor;
      }

      @java.lang.Override
      public ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest getDefaultInstanceForType() {
        return ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest.getDefaultInstance();
      }

      @java.lang.Override
      public ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest build() {
        ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest buildPartial() {
        ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest result = new ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.command_ = command_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.key_ = key_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.value_ = value_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        result.version_ = version_;
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return (Builder) super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest) {
          return mergeFrom((ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest other) {
        if (other == ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest.getDefaultInstance()) return this;
        if (other.hasCommand()) {
          setCommand(other.getCommand());
        }
        if (other.hasKey()) {
          setKey(other.getKey());
        }
        if (other.hasValue()) {
          setValue(other.getValue());
        }
        if (other.hasVersion()) {
          setVersion(other.getVersion());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        if (!hasCommand()) {
          return false;
        }
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }
      private int bitField0_;

      private int command_ ;
      /**
       * <code>required uint32 command = 1;</code>
       */
      public boolean hasCommand() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      /**
       * <code>required uint32 command = 1;</code>
       */
      public int getCommand() {
        return command_;
      }
      /**
       * <code>required uint32 command = 1;</code>
       */
      public Builder setCommand(int value) {
        bitField0_ |= 0x00000001;
        command_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>required uint32 command = 1;</code>
       */
      public Builder clearCommand() {
        bitField0_ = (bitField0_ & ~0x00000001);
        command_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString key_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes key = 2;</code>
       */
      public boolean hasKey() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      /**
       * <code>optional bytes key = 2;</code>
       */
      public com.google.protobuf.ByteString getKey() {
        return key_;
      }
      /**
       * <code>optional bytes key = 2;</code>
       */
      public Builder setKey(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        key_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes key = 2;</code>
       */
      public Builder clearKey() {
        bitField0_ = (bitField0_ & ~0x00000002);
        key_ = getDefaultInstance().getKey();
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString value_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>optional bytes value = 3;</code>
       */
      public boolean hasValue() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      /**
       * <code>optional bytes value = 3;</code>
       */
      public com.google.protobuf.ByteString getValue() {
        return value_;
      }
      /**
       * <code>optional bytes value = 3;</code>
       */
      public Builder setValue(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        value_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional bytes value = 3;</code>
       */
      public Builder clearValue() {
        bitField0_ = (bitField0_ & ~0x00000004);
        value_ = getDefaultInstance().getValue();
        onChanged();
        return this;
      }

      private int version_ ;
      /**
       * <code>optional int32 version = 4;</code>
       */
      public boolean hasVersion() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      /**
       * <code>optional int32 version = 4;</code>
       */
      public int getVersion() {
        return version_;
      }
      /**
       * <code>optional int32 version = 4;</code>
       */
      public Builder setVersion(int value) {
        bitField0_ |= 0x00000008;
        version_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional int32 version = 4;</code>
       */
      public Builder clearVersion() {
        bitField0_ = (bitField0_ & ~0x00000008);
        version_ = 0;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:KVRequest)
    }

    // @@protoc_insertion_point(class_scope:KVRequest)
    private static final ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest();
    }

    public static ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    @java.lang.Deprecated public static final com.google.protobuf.Parser<KVRequest>
        PARSER = new com.google.protobuf.AbstractParser<KVRequest>() {
      @java.lang.Override
      public KVRequest parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new KVRequest(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<KVRequest> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<KVRequest> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public ca.NetSysLab.ProtocolBuffers.KeyValueRequest.KVRequest getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_KVRequest_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_KVRequest_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\033proto/KeyValueRequest.proto\"I\n\tKVReque" +
      "st\022\017\n\007command\030\001 \002(\r\022\013\n\003key\030\002 \001(\014\022\r\n\005valu" +
      "e\030\003 \001(\014\022\017\n\007version\030\004 \001(\005B/\n\034ca.NetSysLab" +
      ".ProtocolBuffersB\017KeyValueRequest"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
        }, assigner);
    internal_static_KVRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_KVRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_KVRequest_descriptor,
        new java.lang.String[] { "Command", "Key", "Value", "Version", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
