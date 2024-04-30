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
import io.github.libedi.mock.util.DataConverter;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * SendRequestInboundHandlerTest
 *
 * @author libed
 *
 */
public class SendRequestInboundHandlerTest {

	private ChannelInboundHandler inboundHandler;
	private EmbeddedChannel channel;

	@BeforeEach
	void init() {
		inboundHandler = new SendRequestInboundHandler();
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
		final MockMessage request = createSendRequest();
		final MockMessage expectedSendResponse = DataConverter.convertMessage(request);
		channel.writeInbound(request);
		
		// when
		final MockMessage actualSendResponse = channel.readOutbound();
		
		// then
		assertThat(actualSendResponse).isNotNull();
		assertThat(actualSendResponse.isSendResponse()).isTrue();
		assertThat(actualSendResponse).usingRecursiveComparison().ignoringFields("body.msgId", "body.sendStatus")
				.isEqualTo(expectedSendResponse);

		// given
		final MockMessage expectedResultRequest = DataConverter.convertMessage(actualSendResponse);

		// when
		final MockMessage actualResultRequest = channel.readOutbound();

		// then
		assertThat(actualResultRequest).isNotNull();
		assertThat(actualResultRequest.isResultRequest()).isTrue();
		assertThat(actualResultRequest).usingRecursiveComparison().ignoringFields("body.sendStatus")
				.isEqualTo(expectedResultRequest);
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
