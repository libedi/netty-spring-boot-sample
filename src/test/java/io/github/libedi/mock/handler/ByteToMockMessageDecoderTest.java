package io.github.libedi.mock.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.libedi.mock.constant.MessageSubType;
import io.github.libedi.mock.constant.MessageType;
import io.github.libedi.mock.domain.Address;
import io.github.libedi.mock.domain.Body;
import io.github.libedi.mock.domain.Header;
import io.github.libedi.mock.domain.MockMessage;
import io.github.libedi.mock.domain.SendRequestBody;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * ByteToMockMessageDecoderTest
 *
 * @author libed
 *
 */
class ByteToMockMessageDecoderTest {

	private ChannelInboundHandler inboundHandler;
	private EmbeddedChannel channel;

	@BeforeEach
	void init() {
		inboundHandler = new ByteToMockMessageDecoder();
		channel = new EmbeddedChannel(inboundHandler) {
			@Override
			protected SocketAddress localAddress0() {
				return new InetSocketAddress(8080);
			}
		};
		assertThat(inboundHandler).isNotNull();
		assertThat(channel).isNotNull();
	}

	@Test
	void test() {
		// given
		final MockMessage expected = createSendRequest();
		channel.writeInbound(expected.toByteBuf());

		// when
		final MockMessage actual = channel.readInbound();

		// then
		assertThat(actual).isNotNull();
		assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
	}

	/**
	 * @return
	 */
	private MockMessage createSendRequest() {
		final String message = "Test Message";
		final Body body = SendRequestBody.builder()
				.callbackNumber(StringUtils.rightPad("cid", 10, Character.MIN_VALUE))
				.messageLength((short) message.getBytes().length)
				.message(StringUtils.rightPad(message, 128))
				.build();
		final Header header = Header.builder()
				.messageType(MessageType.REQ)
				.messageSubType(MessageSubType.SEND)
				.source(Address.builder()
						.cid(StringUtils.rightPad("123456789", 10, Character.MIN_VALUE))
						.callNumber(StringUtils.rightPad("010114", 10, Character.MIN_VALUE))
						.build())
				.destination(Address.builder()
						.cid(StringUtils.rightPad("010", 10, Character.MIN_VALUE))
						.callNumber(StringUtils.rightPad("12345678", 10, Character.MIN_VALUE))
						.build())
				.bodyLength(body.getDataLength())
				.reserved(new byte[10])
				.build();
		return MockMessage.of(header, body);
	}

}
