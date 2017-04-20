// Code generated by Wire protocol buffer compiler, do not edit.
// Source file: message.proto
import com.squareup.wire.FieldEncoding;
import com.squareup.wire.Message;
import com.squareup.wire.ProtoAdapter;
import com.squareup.wire.ProtoReader;
import com.squareup.wire.ProtoWriter;
import com.squareup.wire.WireField;
import com.squareup.wire.internal.Internal;
import java.io.IOException;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.List;
import okio.ByteString;

public final class MagicData extends Message<MagicData, MagicData.Builder> {
  public static final ProtoAdapter<MagicData> ADAPTER = new ProtoAdapter_MagicData();

  private static final long serialVersionUID = 0L;

  @WireField(
      tag = 1,
      adapter = "MagicData$Marker#ADAPTER",
      label = WireField.Label.REQUIRED
  )
  public final Marker marker;

  @WireField(
      tag = 2,
      adapter = "MagicData$Information#ADAPTER",
      label = WireField.Label.REQUIRED
  )
  public final Information information;

  public MagicData(Marker marker, Information information) {
    this(marker, information, ByteString.EMPTY);
  }

  public MagicData(Marker marker, Information information, ByteString unknownFields) {
    super(ADAPTER, unknownFields);
    this.marker = marker;
    this.information = information;
  }

  @Override
  public Builder newBuilder() {
    Builder builder = new Builder();
    builder.marker = marker;
    builder.information = information;
    builder.addUnknownFields(unknownFields());
    return builder;
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) return true;
    if (!(other instanceof MagicData)) return false;
    MagicData o = (MagicData) other;
    return unknownFields().equals(o.unknownFields())
        && marker.equals(o.marker)
        && information.equals(o.information);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode;
    if (result == 0) {
      result = unknownFields().hashCode();
      result = result * 37 + marker.hashCode();
      result = result * 37 + information.hashCode();
      super.hashCode = result;
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(", marker=").append(marker);
    builder.append(", information=").append(information);
    return builder.replace(0, 2, "MagicData{").append('}').toString();
  }

  public static final class Builder extends Message.Builder<MagicData, Builder> {
    public Marker marker;

    public Information information;

    public Builder() {
    }

    public Builder marker(Marker marker) {
      this.marker = marker;
      return this;
    }

    public Builder information(Information information) {
      this.information = information;
      return this;
    }

    @Override
    public MagicData build() {
      if (marker == null
          || information == null) {
        throw Internal.missingRequiredFields(marker, "marker",
            information, "information");
      }
      return new MagicData(marker, information, super.buildUnknownFields());
    }
  }

  public static final class Marker extends Message<Marker, Marker.Builder> {
    public static final ProtoAdapter<Marker> ADAPTER = new ProtoAdapter_Marker();

    private static final long serialVersionUID = 0L;

    public static final String DEFAULT_MARKERNAME = "";

    public static final ByteString DEFAULT_ISET = ByteString.EMPTY;

    public static final ByteString DEFAULT_FSET = ByteString.EMPTY;

    public static final ByteString DEFAULT_FSET3 = ByteString.EMPTY;

    @WireField(
        tag = 1,
        adapter = "com.squareup.wire.ProtoAdapter#STRING",
        label = WireField.Label.REQUIRED
    )
    public final String markerName;

    @WireField(
        tag = 2,
        adapter = "com.squareup.wire.ProtoAdapter#BYTES",
        label = WireField.Label.REQUIRED
    )
    public final ByteString iset;

    @WireField(
        tag = 3,
        adapter = "com.squareup.wire.ProtoAdapter#BYTES",
        label = WireField.Label.REQUIRED
    )
    public final ByteString fset;

    @WireField(
        tag = 4,
        adapter = "com.squareup.wire.ProtoAdapter#BYTES",
        label = WireField.Label.REQUIRED
    )
    public final ByteString fset3;

    public Marker(String markerName, ByteString iset, ByteString fset, ByteString fset3) {
      this(markerName, iset, fset, fset3, ByteString.EMPTY);
    }

    public Marker(String markerName, ByteString iset, ByteString fset, ByteString fset3,
        ByteString unknownFields) {
      super(ADAPTER, unknownFields);
      this.markerName = markerName;
      this.iset = iset;
      this.fset = fset;
      this.fset3 = fset3;
    }

    @Override
    public Builder newBuilder() {
      Builder builder = new Builder();
      builder.markerName = markerName;
      builder.iset = iset;
      builder.fset = fset;
      builder.fset3 = fset3;
      builder.addUnknownFields(unknownFields());
      return builder;
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof Marker)) return false;
      Marker o = (Marker) other;
      return unknownFields().equals(o.unknownFields())
          && markerName.equals(o.markerName)
          && iset.equals(o.iset)
          && fset.equals(o.fset)
          && fset3.equals(o.fset3);
    }

    @Override
    public int hashCode() {
      int result = super.hashCode;
      if (result == 0) {
        result = unknownFields().hashCode();
        result = result * 37 + markerName.hashCode();
        result = result * 37 + iset.hashCode();
        result = result * 37 + fset.hashCode();
        result = result * 37 + fset3.hashCode();
        super.hashCode = result;
      }
      return result;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(", markerName=").append(markerName);
      builder.append(", iset=").append(iset);
      builder.append(", fset=").append(fset);
      builder.append(", fset3=").append(fset3);
      return builder.replace(0, 2, "Marker{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<Marker, Builder> {
      public String markerName;

      public ByteString iset;

      public ByteString fset;

      public ByteString fset3;

      public Builder() {
      }

      public Builder markerName(String markerName) {
        this.markerName = markerName;
        return this;
      }

      public Builder iset(ByteString iset) {
        this.iset = iset;
        return this;
      }

      public Builder fset(ByteString fset) {
        this.fset = fset;
        return this;
      }

      public Builder fset3(ByteString fset3) {
        this.fset3 = fset3;
        return this;
      }

      @Override
      public Marker build() {
        if (markerName == null
            || iset == null
            || fset == null
            || fset3 == null) {
          throw Internal.missingRequiredFields(markerName, "markerName",
              iset, "iset",
              fset, "fset",
              fset3, "fset3");
        }
        return new Marker(markerName, iset, fset, fset3, super.buildUnknownFields());
      }
    }

    private static final class ProtoAdapter_Marker extends ProtoAdapter<Marker> {
      public ProtoAdapter_Marker() {
        super(FieldEncoding.LENGTH_DELIMITED, Marker.class);
      }

      @Override
      public int encodedSize(Marker value) {
        return ProtoAdapter.STRING.encodedSizeWithTag(1, value.markerName)
            + ProtoAdapter.BYTES.encodedSizeWithTag(2, value.iset)
            + ProtoAdapter.BYTES.encodedSizeWithTag(3, value.fset)
            + ProtoAdapter.BYTES.encodedSizeWithTag(4, value.fset3)
            + value.unknownFields().size();
      }

      @Override
      public void encode(ProtoWriter writer, Marker value) throws IOException {
        ProtoAdapter.STRING.encodeWithTag(writer, 1, value.markerName);
        ProtoAdapter.BYTES.encodeWithTag(writer, 2, value.iset);
        ProtoAdapter.BYTES.encodeWithTag(writer, 3, value.fset);
        ProtoAdapter.BYTES.encodeWithTag(writer, 4, value.fset3);
        writer.writeBytes(value.unknownFields());
      }

      @Override
      public Marker decode(ProtoReader reader) throws IOException {
        Builder builder = new Builder();
        long token = reader.beginMessage();
        for (int tag; (tag = reader.nextTag()) != -1;) {
          switch (tag) {
            case 1: builder.markerName(ProtoAdapter.STRING.decode(reader)); break;
            case 2: builder.iset(ProtoAdapter.BYTES.decode(reader)); break;
            case 3: builder.fset(ProtoAdapter.BYTES.decode(reader)); break;
            case 4: builder.fset3(ProtoAdapter.BYTES.decode(reader)); break;
            default: {
              FieldEncoding fieldEncoding = reader.peekFieldEncoding();
              Object value = fieldEncoding.rawProtoAdapter().decode(reader);
              builder.addUnknownField(tag, fieldEncoding, value);
            }
          }
        }
        reader.endMessage(token);
        return builder.build();
      }

      @Override
      public Marker redact(Marker value) {
        Builder builder = value.newBuilder();
        builder.clearUnknownFields();
        return builder.build();
      }
    }
  }

  public static final class Information extends Message<Information, Information.Builder> {
    public static final ProtoAdapter<Information> ADAPTER = new ProtoAdapter_Information();

    private static final long serialVersionUID = 0L;

    public static final ByteString DEFAULT_OBJ = ByteString.EMPTY;

    public static final ByteString DEFAULT_MTL = ByteString.EMPTY;

    @WireField(
        tag = 1,
        adapter = "com.squareup.wire.ProtoAdapter#BYTES",
        label = WireField.Label.REQUIRED
    )
    public final ByteString obj;

    @WireField(
        tag = 2,
        adapter = "com.squareup.wire.ProtoAdapter#BYTES",
        label = WireField.Label.REQUIRED
    )
    public final ByteString mtl;

    @WireField(
        tag = 3,
        adapter = "MagicData$Images#ADAPTER",
        label = WireField.Label.REPEATED
    )
    public final List<Images> image;

    public Information(ByteString obj, ByteString mtl, List<Images> image) {
      this(obj, mtl, image, ByteString.EMPTY);
    }

    public Information(ByteString obj, ByteString mtl, List<Images> image,
        ByteString unknownFields) {
      super(ADAPTER, unknownFields);
      this.obj = obj;
      this.mtl = mtl;
      this.image = Internal.immutableCopyOf("image", image);
    }

    @Override
    public Builder newBuilder() {
      Builder builder = new Builder();
      builder.obj = obj;
      builder.mtl = mtl;
      builder.image = Internal.copyOf("image", image);
      builder.addUnknownFields(unknownFields());
      return builder;
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof Information)) return false;
      Information o = (Information) other;
      return unknownFields().equals(o.unknownFields())
          && obj.equals(o.obj)
          && mtl.equals(o.mtl)
          && image.equals(o.image);
    }

    @Override
    public int hashCode() {
      int result = super.hashCode;
      if (result == 0) {
        result = unknownFields().hashCode();
        result = result * 37 + obj.hashCode();
        result = result * 37 + mtl.hashCode();
        result = result * 37 + image.hashCode();
        super.hashCode = result;
      }
      return result;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(", obj=").append(obj);
      builder.append(", mtl=").append(mtl);
      if (!image.isEmpty()) builder.append(", image=").append(image);
      return builder.replace(0, 2, "Information{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<Information, Builder> {
      public ByteString obj;

      public ByteString mtl;

      public List<Images> image;

      public Builder() {
        image = Internal.newMutableList();
      }

      public Builder obj(ByteString obj) {
        this.obj = obj;
        return this;
      }

      public Builder mtl(ByteString mtl) {
        this.mtl = mtl;
        return this;
      }

      public Builder image(List<Images> image) {
        Internal.checkElementsNotNull(image);
        this.image = image;
        return this;
      }

      @Override
      public Information build() {
        if (obj == null
            || mtl == null) {
          throw Internal.missingRequiredFields(obj, "obj",
              mtl, "mtl");
        }
        return new Information(obj, mtl, image, super.buildUnknownFields());
      }
    }

    private static final class ProtoAdapter_Information extends ProtoAdapter<Information> {
      public ProtoAdapter_Information() {
        super(FieldEncoding.LENGTH_DELIMITED, Information.class);
      }

      @Override
      public int encodedSize(Information value) {
        return ProtoAdapter.BYTES.encodedSizeWithTag(1, value.obj)
            + ProtoAdapter.BYTES.encodedSizeWithTag(2, value.mtl)
            + Images.ADAPTER.asRepeated().encodedSizeWithTag(3, value.image)
            + value.unknownFields().size();
      }

      @Override
      public void encode(ProtoWriter writer, Information value) throws IOException {
        ProtoAdapter.BYTES.encodeWithTag(writer, 1, value.obj);
        ProtoAdapter.BYTES.encodeWithTag(writer, 2, value.mtl);
        Images.ADAPTER.asRepeated().encodeWithTag(writer, 3, value.image);
        writer.writeBytes(value.unknownFields());
      }

      @Override
      public Information decode(ProtoReader reader) throws IOException {
        Builder builder = new Builder();
        long token = reader.beginMessage();
        for (int tag; (tag = reader.nextTag()) != -1;) {
          switch (tag) {
            case 1: builder.obj(ProtoAdapter.BYTES.decode(reader)); break;
            case 2: builder.mtl(ProtoAdapter.BYTES.decode(reader)); break;
            case 3: builder.image.add(Images.ADAPTER.decode(reader)); break;
            default: {
              FieldEncoding fieldEncoding = reader.peekFieldEncoding();
              Object value = fieldEncoding.rawProtoAdapter().decode(reader);
              builder.addUnknownField(tag, fieldEncoding, value);
            }
          }
        }
        reader.endMessage(token);
        return builder.build();
      }

      @Override
      public Information redact(Information value) {
        Builder builder = value.newBuilder();
        Internal.redactElements(builder.image, Images.ADAPTER);
        builder.clearUnknownFields();
        return builder.build();
      }
    }
  }

  public static final class Images extends Message<Images, Images.Builder> {
    public static final ProtoAdapter<Images> ADAPTER = new ProtoAdapter_Images();

    private static final long serialVersionUID = 0L;

    public static final ByteString DEFAULT_IMAGEBYTES = ByteString.EMPTY;

    @WireField(
        tag = 1,
        adapter = "com.squareup.wire.ProtoAdapter#BYTES",
        label = WireField.Label.REQUIRED
    )
    public final ByteString imagebytes;

    public Images(ByteString imagebytes) {
      this(imagebytes, ByteString.EMPTY);
    }

    public Images(ByteString imagebytes, ByteString unknownFields) {
      super(ADAPTER, unknownFields);
      this.imagebytes = imagebytes;
    }

    @Override
    public Builder newBuilder() {
      Builder builder = new Builder();
      builder.imagebytes = imagebytes;
      builder.addUnknownFields(unknownFields());
      return builder;
    }

    @Override
    public boolean equals(Object other) {
      if (other == this) return true;
      if (!(other instanceof Images)) return false;
      Images o = (Images) other;
      return unknownFields().equals(o.unknownFields())
          && imagebytes.equals(o.imagebytes);
    }

    @Override
    public int hashCode() {
      int result = super.hashCode;
      if (result == 0) {
        result = unknownFields().hashCode();
        result = result * 37 + imagebytes.hashCode();
        super.hashCode = result;
      }
      return result;
    }

    @Override
    public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append(", imagebytes=").append(imagebytes);
      return builder.replace(0, 2, "Images{").append('}').toString();
    }

    public static final class Builder extends Message.Builder<Images, Builder> {
      public ByteString imagebytes;

      public Builder() {
      }

      public Builder imagebytes(ByteString imagebytes) {
        this.imagebytes = imagebytes;
        return this;
      }

      @Override
      public Images build() {
        if (imagebytes == null) {
          throw Internal.missingRequiredFields(imagebytes, "imagebytes");
        }
        return new Images(imagebytes, super.buildUnknownFields());
      }
    }

    private static final class ProtoAdapter_Images extends ProtoAdapter<Images> {
      public ProtoAdapter_Images() {
        super(FieldEncoding.LENGTH_DELIMITED, Images.class);
      }

      @Override
      public int encodedSize(Images value) {
        return ProtoAdapter.BYTES.encodedSizeWithTag(1, value.imagebytes)
            + value.unknownFields().size();
      }

      @Override
      public void encode(ProtoWriter writer, Images value) throws IOException {
        ProtoAdapter.BYTES.encodeWithTag(writer, 1, value.imagebytes);
        writer.writeBytes(value.unknownFields());
      }

      @Override
      public Images decode(ProtoReader reader) throws IOException {
        Builder builder = new Builder();
        long token = reader.beginMessage();
        for (int tag; (tag = reader.nextTag()) != -1;) {
          switch (tag) {
            case 1: builder.imagebytes(ProtoAdapter.BYTES.decode(reader)); break;
            default: {
              FieldEncoding fieldEncoding = reader.peekFieldEncoding();
              Object value = fieldEncoding.rawProtoAdapter().decode(reader);
              builder.addUnknownField(tag, fieldEncoding, value);
            }
          }
        }
        reader.endMessage(token);
        return builder.build();
      }

      @Override
      public Images redact(Images value) {
        Builder builder = value.newBuilder();
        builder.clearUnknownFields();
        return builder.build();
      }
    }
  }

  private static final class ProtoAdapter_MagicData extends ProtoAdapter<MagicData> {
    public ProtoAdapter_MagicData() {
      super(FieldEncoding.LENGTH_DELIMITED, MagicData.class);
    }

    @Override
    public int encodedSize(MagicData value) {
      return Marker.ADAPTER.encodedSizeWithTag(1, value.marker)
          + Information.ADAPTER.encodedSizeWithTag(2, value.information)
          + value.unknownFields().size();
    }

    @Override
    public void encode(ProtoWriter writer, MagicData value) throws IOException {
      Marker.ADAPTER.encodeWithTag(writer, 1, value.marker);
      Information.ADAPTER.encodeWithTag(writer, 2, value.information);
      writer.writeBytes(value.unknownFields());
    }

    @Override
    public MagicData decode(ProtoReader reader) throws IOException {
      Builder builder = new Builder();
      long token = reader.beginMessage();
      for (int tag; (tag = reader.nextTag()) != -1;) {
        switch (tag) {
          case 1: builder.marker(Marker.ADAPTER.decode(reader)); break;
          case 2: builder.information(Information.ADAPTER.decode(reader)); break;
          default: {
            FieldEncoding fieldEncoding = reader.peekFieldEncoding();
            Object value = fieldEncoding.rawProtoAdapter().decode(reader);
            builder.addUnknownField(tag, fieldEncoding, value);
          }
        }
      }
      reader.endMessage(token);
      return builder.build();
    }

    @Override
    public MagicData redact(MagicData value) {
      Builder builder = value.newBuilder();
      builder.marker = Marker.ADAPTER.redact(builder.marker);
      builder.information = Information.ADAPTER.redact(builder.information);
      builder.clearUnknownFields();
      return builder.build();
    }
  }
}
