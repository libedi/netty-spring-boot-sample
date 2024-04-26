package io.github.libedi.time.client.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * TimeClientDecoder
 *
 * @author libed
 *
 */
public class TimeClientDecoder extends ByteToMessageDecoder {

	/**
	 * 충분한 데이터가 모일 때까지 수신 데이터를 누적
	 */
	@Override
	protected void decode(final ChannelHandlerContext ctx, final ByteBuf in, final List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}
		out.add(in.readBytes(4));
	}

}
