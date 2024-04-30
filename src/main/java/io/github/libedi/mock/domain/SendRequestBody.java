package io.github.libedi.mock.domain;

import java.util.Arrays;

import io.github.libedi.mock.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Builder;

/**
 * SendRequestBody
 * 
 * <pre>
 * - callbackNumber : 10 bytes
 * - messageLength  : unsigned byte
 * - message        : 128 bytes
 * - reserved       : 1 byte
 * </pre>
 *
 * @author libed
 *
 */
public record SendRequestBody(String callbackNumber, short messageLength, String message, byte reserved)
		implements Body {

	@Builder
	public SendRequestBody {
	}

	@Override
	public int getDataLength() {
		return 140;
	}

	@Override
	public ByteBuf toByteBuf() {
		final byte[] tempCallback = new byte[10];
		Arrays.fill(tempCallback, (byte) 0);
		final byte[] callbackBytes = callbackNumber.getBytes();
		System.arraycopy(callbackBytes, 0, tempCallback, 0, callbackBytes.length);

		final byte[] tempMessage = new byte[128];
		Arrays.fill(tempMessage, (byte) 0);
		final byte[] messageBytes = message.getBytes();
		System.arraycopy(messageBytes, 0, tempMessage, 0, messageBytes.length);

		return ByteBufUtil.createByteBuf(getDataLength(), tempCallback, (byte) messageLength, tempMessage, reserved);
	}

	public static SendRequestBody from(final ByteBuf buf) {
		return SendRequestBody.builder()
				.callbackNumber(ByteBufUtil.toString(buf, 10))
				.messageLength(buf.readUnsignedByte())
				.message(ByteBufUtil.toString(buf, 128))
				.reserved(buf.readByte())
				.build();
	}

}
