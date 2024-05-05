package io.github.libedi.mock.domain;

import java.util.Arrays;
import java.util.Objects;

import io.github.libedi.mock.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Builder;

/**
 * Address
 *
 * @author libed
 *
 */
public record Address(String cid, String callNumber) implements Convertible {

    private static final int LENGTH = 20;

    @Builder
    public Address {
        Objects.requireNonNull(cid, () -> "cid must not be null.");
    }

    @Override
    public int getDataLength() {
        return LENGTH;
    }

    @Override
    public ByteBuf toByteBuf() {
        final byte[] destCid = new byte[10];
        Arrays.fill(destCid, (byte) 0);
        final byte[] cidBytes = cid.getBytes();
        System.arraycopy(cidBytes, 0, destCid, 0, cidBytes.length);

        final byte[] destCallNumber = new byte[10];
        Arrays.fill(destCallNumber, (byte) 0);
        if (callNumber != null) {
            final byte[] callNumberBytes = callNumber.getBytes();
            System.arraycopy(callNumberBytes, 0, destCallNumber, 0, callNumberBytes.length);
        }

        return ByteBufUtil.createByteBuf(getDataLength(), destCid, destCallNumber);
    }

}
