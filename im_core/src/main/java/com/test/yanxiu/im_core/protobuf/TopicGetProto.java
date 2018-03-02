package com.test.yanxiu.im_core.protobuf;
// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: TopicGet.proto

public final class TopicGetProto {
  private TopicGetProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface TopicGetOrBuilder extends
      // @@protoc_insertion_point(interface_extends:TopicGet)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>int32 bizSource = 1;</code>
     */
    int getBizSource();

    /**
     * <code>int64 topicId = 2;</code>
     */
    long getTopicId();
  }
  /**
   * Protobuf type {@code TopicGet}
   */
  public  static final class TopicGet extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:TopicGet)
      TopicGetOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use TopicGet.newBuilder() to construct.
    private TopicGet(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private TopicGet() {
      bizSource_ = 0;
      topicId_ = 0L;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private TopicGet(
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
            default: {
              if (!parseUnknownFieldProto3(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
              break;
            }
            case 8: {

              bizSource_ = input.readInt32();
              break;
            }
            case 16: {

              topicId_ = input.readInt64();
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
      return TopicGetProto.internal_static_TopicGet_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return TopicGetProto.internal_static_TopicGet_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              TopicGetProto.TopicGet.class, TopicGetProto.TopicGet.Builder.class);
    }

    public static final int BIZSOURCE_FIELD_NUMBER = 1;
    private int bizSource_;
    /**
     * <code>int32 bizSource = 1;</code>
     */
    public int getBizSource() {
      return bizSource_;
    }

    public static final int TOPICID_FIELD_NUMBER = 2;
    private long topicId_;
    /**
     * <code>int64 topicId = 2;</code>
     */
    public long getTopicId() {
      return topicId_;
    }

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
      if (bizSource_ != 0) {
        output.writeInt32(1, bizSource_);
      }
      if (topicId_ != 0L) {
        output.writeInt64(2, topicId_);
      }
      unknownFields.writeTo(output);
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (bizSource_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, bizSource_);
      }
      if (topicId_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(2, topicId_);
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
      if (!(obj instanceof TopicGetProto.TopicGet)) {
        return super.equals(obj);
      }
      TopicGetProto.TopicGet other = (TopicGetProto.TopicGet) obj;

      boolean result = true;
      result = result && (getBizSource()
          == other.getBizSource());
      result = result && (getTopicId()
          == other.getTopicId());
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
      hash = (37 * hash) + BIZSOURCE_FIELD_NUMBER;
      hash = (53 * hash) + getBizSource();
      hash = (37 * hash) + TOPICID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getTopicId());
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static TopicGetProto.TopicGet parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static TopicGetProto.TopicGet parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static TopicGetProto.TopicGet parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static TopicGetProto.TopicGet parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static TopicGetProto.TopicGet parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static TopicGetProto.TopicGet parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static TopicGetProto.TopicGet parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static TopicGetProto.TopicGet parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static TopicGetProto.TopicGet parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static TopicGetProto.TopicGet parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static TopicGetProto.TopicGet parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static TopicGetProto.TopicGet parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(TopicGetProto.TopicGet prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
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
     * Protobuf type {@code TopicGet}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:TopicGet)
        TopicGetProto.TopicGetOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return TopicGetProto.internal_static_TopicGet_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return TopicGetProto.internal_static_TopicGet_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                TopicGetProto.TopicGet.class, TopicGetProto.TopicGet.Builder.class);
      }

      // Construct using TopicGetProto.TopicGet.newBuilder()
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
      public Builder clear() {
        super.clear();
        bizSource_ = 0;

        topicId_ = 0L;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return TopicGetProto.internal_static_TopicGet_descriptor;
      }

      public TopicGetProto.TopicGet getDefaultInstanceForType() {
        return TopicGetProto.TopicGet.getDefaultInstance();
      }

      public TopicGetProto.TopicGet build() {
        TopicGetProto.TopicGet result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public TopicGetProto.TopicGet buildPartial() {
        TopicGetProto.TopicGet result = new TopicGetProto.TopicGet(this);
        result.bizSource_ = bizSource_;
        result.topicId_ = topicId_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof TopicGetProto.TopicGet) {
          return mergeFrom((TopicGetProto.TopicGet)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(TopicGetProto.TopicGet other) {
        if (other == TopicGetProto.TopicGet.getDefaultInstance()) return this;
        if (other.getBizSource() != 0) {
          setBizSource(other.getBizSource());
        }
        if (other.getTopicId() != 0L) {
          setTopicId(other.getTopicId());
        }
        this.mergeUnknownFields(other.unknownFields);
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
        TopicGetProto.TopicGet parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (TopicGetProto.TopicGet) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private int bizSource_ ;
      /**
       * <code>int32 bizSource = 1;</code>
       */
      public int getBizSource() {
        return bizSource_;
      }
      /**
       * <code>int32 bizSource = 1;</code>
       */
      public Builder setBizSource(int value) {
        
        bizSource_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 bizSource = 1;</code>
       */
      public Builder clearBizSource() {
        
        bizSource_ = 0;
        onChanged();
        return this;
      }

      private long topicId_ ;
      /**
       * <code>int64 topicId = 2;</code>
       */
      public long getTopicId() {
        return topicId_;
      }
      /**
       * <code>int64 topicId = 2;</code>
       */
      public Builder setTopicId(long value) {
        
        topicId_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int64 topicId = 2;</code>
       */
      public Builder clearTopicId() {
        
        topicId_ = 0L;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFieldsProto3(unknownFields);
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:TopicGet)
    }

    // @@protoc_insertion_point(class_scope:TopicGet)
    private static final TopicGetProto.TopicGet DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new TopicGetProto.TopicGet();
    }

    public static TopicGetProto.TopicGet getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<TopicGet>
        PARSER = new com.google.protobuf.AbstractParser<TopicGet>() {
      public TopicGet parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new TopicGet(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<TopicGet> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<TopicGet> getParserForType() {
      return PARSER;
    }

    public TopicGetProto.TopicGet getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_TopicGet_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_TopicGet_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016TopicGet.proto\".\n\010TopicGet\022\021\n\tbizSourc" +
      "e\030\001 \001(\005\022\017\n\007topicId\030\002 \001(\003B\017B\rTopicGetProt" +
      "ob\006proto3"
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
    internal_static_TopicGet_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_TopicGet_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_TopicGet_descriptor,
        new java.lang.String[] { "BizSource", "TopicId", });
  }

  // @@protoc_insertion_point(outer_class_scope)
}
