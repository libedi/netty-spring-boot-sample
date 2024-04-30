package io.github.libedi.mock.config.domain;

import org.apache.commons.lang3.RandomStringUtils;

import io.github.libedi.mock.config.constant.Result;
import io.github.libedi.mock.config.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * SendResponseBody
 *
 * @author libed
 *
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public final class SendResponseBody extends ResponseBody {

	private String msgId; // 10 bytes

	@Builder(builderMethodName = "sendBuilder")
	private SendResponseBody(final Result result) {
		super(result);
		if (result == Result.SUCCESS) {
			msgId = RandomStringUtils.randomAlphanumeric(10);
		}
	}

	private SendResponseBody(final Result result, final String msgId) {
		super(result);
		this.msgId = msgId;
	}

	@Override
	public int getDataLength() {
		return isSuccess() ? super.getDataLength() + 10 : super.getDataLength();
	}

	@Override
	public ByteBuf toByteBuf() {
		return ByteBufUtil.createByteBuf(getDataLength(), getResult(), msgId);
	}

	public static SendResponseBody from(final ByteBuf buf) {
		final Result result = Result.from(buf.readInt());
		if (result == Result.SUCCESS) {
			return new SendResponseBody(result, ByteBufUtil.toString(buf, 10));
		}
		return new SendResponseBody(result);
	}

}
