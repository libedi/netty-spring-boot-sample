package io.github.libedi.mock.config.domain;

import io.github.libedi.mock.config.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Message
 *
 * @author libed
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class Message implements Convertible {

	private final Header header;
	private final Body body;

	@Override
	public int getDataLength() {
		return header.getDataLength() + body.getDataLength();
	}

	@Override
	public ByteBuf toByteBuf() {
		return ByteBufUtil.createByteBuf(getDataLength(), header, body);
	}

	public static Message from(final ByteBuf buf) {
		final Header header = Header.from(buf);
		return new Message(header, Body.from(header, buf));
	}
}
