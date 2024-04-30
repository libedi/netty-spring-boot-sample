package io.github.libedi.mock.config.domain;

import io.github.libedi.mock.config.constant.Result;
import io.github.libedi.mock.config.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * ResponseBody
 *
 * @author libed
 *
 */
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@ToString
@EqualsAndHashCode
public sealed class ResponseBody implements Body permits SendResponseBody {

	private final Result result;

	@Override
	public int getDataLength() {
		return Integer.BYTES;
	}

	@Override
	public ByteBuf toByteBuf() {
		return ByteBufUtil.createByteBuf(getDataLength(), result);
	}

	public static ResponseBody from(final ByteBuf buf) {
		return ResponseBody.builder()
				.result(Result.from(buf.readInt()))
				.build();
	}

	/**
	 * 전송 결과 성공 여부
	 *
	 * @return
	 */
	public boolean isSuccess() {
		return result == Result.SUCCESS;
	}

}
