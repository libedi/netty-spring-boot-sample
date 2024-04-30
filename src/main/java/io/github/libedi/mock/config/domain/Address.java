package io.github.libedi.mock.config.domain;

import java.util.Objects;

import io.github.libedi.mock.config.util.ByteBufUtil;
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
		Objects.requireNonNull(callNumber, () -> "callNumber must not be null.");
	}

	@Override
	public int getDataLength() {
		return LENGTH;
	}

	@Override
	public ByteBuf toByteBuf() {
		return ByteBufUtil.createByteBuf(getDataLength(), cid, callNumber);
	}

}

