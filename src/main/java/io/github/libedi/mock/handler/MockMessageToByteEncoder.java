package io.github.libedi.mock.handler;

import io.github.libedi.mock.domain.MockMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * MockMessageToByteEncoder
 *
 * @author libed
 *
 */
public class MockMessageToByteEncoder extends MessageToByteEncoder<MockMessage> {

	@Override
	protected void encode(final ChannelHandlerContext ctx, final MockMessage msg, final ByteBuf out) throws Exception {
		out.writeBytes(msg.toByteBuf());
	}

}
