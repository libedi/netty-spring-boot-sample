package io.github.libedi.mock.config.domain;

import java.util.Objects;

import io.github.libedi.mock.config.constant.SendStatus;
import io.github.libedi.mock.config.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Builder;

/**
 * ResultRequestBody
 *
 * @author libed
 *
 */
public record ResultRequestBody(SendStatus sendStatus, String msgId) implements Body {

	@Builder
	public ResultRequestBody {
		Objects.requireNonNull(sendStatus, () -> "sendStatus must not be null.");
		Objects.requireNonNull(msgId, () -> "msgId must not be null.");
	}

	@Override
	public int getDataLength() {
		return 11;
	}

	@Override
	public ByteBuf toByteBuf() {
		return ByteBufUtil.createByteBuf(getDataLength(), sendStatus, msgId);
	}
	
	public static ResultRequestBody from(final ByteBuf buf) {
		return ResultRequestBody.builder()
				.sendStatus(SendStatus.from(buf.readByte()))
				.msgId(ByteBufUtil.toString(buf, 10))
				.build();
	}

}
