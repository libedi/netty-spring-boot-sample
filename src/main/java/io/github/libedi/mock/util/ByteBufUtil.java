package io.github.libedi.mock.util;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.commons.lang3.ClassUtils;

import io.github.libedi.mock.constant.CodeEnum;
import io.github.libedi.mock.domain.Convertible;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * ByteBufUtil
 *
 * @author libed
 *
 */
public final class ByteBufUtil {

    private ByteBufUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * <code>ByteBuf</code>에서 length 만큼 <code>byte[]</code> 읽어오기
     *
     * @param buf
     * @param length
     * @return
     */
    public static byte[] readBytes(final ByteBuf buf, final int length) {
        final byte[] dest = new byte[length];
        buf.readBytes(dest);
        return dest;
    }

    /**
     * <code>ByteBuf</code>에서 length 만큼 byte를 <code>String</code>으로 읽어오기
     *
     * @param buf
     * @param length
     * @return
     */
    public static String toString(final ByteBuf buf, final int length) {
        return new String(readBytes(buf, length));
    }

    /**
     * <code>ByteBuf</code>에서 length 만큼 byte를 <code>String</code>으로 읽어오기
     *
     * @param buf
     * @param length
     * @param charset
     * @return
     */
    public static String toString(final ByteBuf buf, final int length, final Charset charset) {
        return new String(readBytes(buf, length), charset);
    }

    /**
     * ByteBuf 생성
     *
     * @param capacity 할당할 buffer 용량
     * @param values   buffer에 넣을 데이터
     * @return
     */
    public static ByteBuf createByteBuf(final int capacity, final Object... values) {
        final ByteBuf buf = Unpooled.buffer(capacity, capacity);
        if (values != null) {
            for (final Object value : values) {
                if (value == null) {
                    continue;
                }
                if (value instanceof byte[]) {
                    buf.writeBytes((byte[]) value);
                } else if (ClassUtils.isPrimitiveWrapper(value.getClass())) {
                    writePrimitiveType(buf, value);
                } else if (value instanceof CharSequence) {
                    buf.writeBytes(value.toString().getBytes());
                } else if (value instanceof ByteBuf) {
                    buf.writeBytes((ByteBuf) value);
                } else if (value instanceof Convertible) {
                    buf.writeBytes(((Convertible) value).toByteBuf());
                } else if (value instanceof CodeEnum) {
                    writePrimitiveType(buf, ((CodeEnum<?>) value).getCode());
                } else if (value instanceof short[]) {
                    for (final short shortValue : (short[]) value) {
                        buf.writeShort(shortValue);
                    }
                } else if (value instanceof int[]) {
                    for (final int intValue : (int[]) value) {
                        buf.writeInt(intValue);
                    }
                } else if (value instanceof ByteBuffer) {
                    buf.writeBytes((ByteBuffer) value);
                }
            }
        }
        return buf;
    }

    private static void writePrimitiveType(final ByteBuf buf, final Object value) {
        final Class<?> primitiveType = ClassUtils.wrapperToPrimitive(value.getClass());
        if (Short.TYPE.equals(primitiveType)) {
            buf.writeShort((short) value);
        } else if (Integer.TYPE.equals(primitiveType)) {
            buf.writeInt((int) value);
        } else if (Long.TYPE.equals(primitiveType)) {
            buf.writeLong((long) value);
        } else if (Float.TYPE.equals(primitiveType)) {
            buf.writeFloat((float) value);
        } else if (Double.TYPE.equals(primitiveType)) {
            buf.writeDouble((double) value);
        } else if (Character.TYPE.equals(primitiveType)) {
            buf.writeChar((char) value);
        } else if (Byte.TYPE.equals(primitiveType)) {
            buf.writeByte((byte) value);
        }
    }

}
