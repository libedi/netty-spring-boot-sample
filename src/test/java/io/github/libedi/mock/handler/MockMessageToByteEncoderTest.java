package io.github.libedi.mock.handler;

import static org.assertj.core.api.Assertions.assertThat;

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
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * MockMessageToByteEncoderTest
 *
 * @author libed
 *
 */
class MockMessageToByteEncoderTest {

	MockMessageToByteEncoder channelOutboundHandler;
	EmbeddedChannel channel;

	@BeforeEach
	void init() {
		channelOutboundHandler = new MockMessageToByteEncoder();
		channel = new EmbeddedChannel(channelOutboundHandler);
		assertThat(channel).isNotNull();
	}
	
	@Test
	void test() {
		// given
		final MockMessage expected = createSendRequest();
		channel.writeOutbound(expected);
		
		// when
		final ByteBuf actual = channel.readOutbound();
		
		// then
		assertThat(actual).isNotNull();
		assertThat(actual).isEqualTo(expected.toByteBuf());
	}
	
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
