package io.github.libedi.mock.domain;

import io.github.libedi.mock.constant.MessageSubType;
import io.github.libedi.mock.constant.MessageType;
import io.github.libedi.mock.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Header
 *
 * @author libed
 *
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode
public final class Header implements Convertible {

	private static final int LENGTH = 58;

	private MessageType messageType; // 2 bytes
	private MessageSubType messageSubType; // 2 bytes
	private Address source; // 20 bytes
	private Address destination; // 20 bytes
	private int bodyLength; // 4 bytes
	private byte[] reserved; // 10 bytes

	@Override
	public int getDataLength() {
		return LENGTH;
	}

	@Override
	public ByteBuf toByteBuf() {
		return ByteBufUtil.createByteBuf(getDataLength(), messageType, messageSubType, source, destination);
	}

	public static Header from(final ByteBuf buf) {
		return Header.builder()
				.messageType(MessageType.from(buf.readShort()))
				.messageSubType(MessageSubType.from(buf.readShort()))
				.source(Address.builder()
						.cid(ByteBufUtil.toString(buf, 10))
						.callNumber(ByteBufUtil.toString(buf, 10))
						.build())
				.destination(Address.builder()
						.cid(ByteBufUtil.toString(buf, 10))
						.callNumber(ByteBufUtil.toString(buf, 10))
						.build())
				.bodyLength(buf.readInt())
				.reserved(ByteBufUtil.readBytes(buf, 10))
				.build();
	}

	public boolean isLinkRequest() {
		return messageType == MessageType.REQ && messageSubType == MessageSubType.LINK;
	}

	public boolean isLinkResponse() {
		return messageType == MessageType.RES && messageSubType == MessageSubType.LINK;
	}

	public boolean isSendRequest() {
		return messageType == MessageType.REQ && messageSubType == MessageSubType.SEND;
	}

	public boolean isSendResponse() {
		return messageType == MessageType.RES && messageSubType == MessageSubType.SEND;
	}

	public boolean isResultRequest() {
		return messageType == MessageType.REQ && messageSubType == MessageSubType.RESULT;
	}

	public boolean isResultResponse() {
		return messageType == MessageType.RES && messageSubType == MessageSubType.RESULT;
	}

}
