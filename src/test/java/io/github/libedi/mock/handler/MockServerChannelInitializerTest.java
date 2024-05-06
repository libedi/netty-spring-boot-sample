package io.github.libedi.mock.handler;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.libedi.mock.constant.MessageSubType;
import io.github.libedi.mock.constant.MessageType;
import io.github.libedi.mock.constant.Result;
import io.github.libedi.mock.domain.Address;
import io.github.libedi.mock.domain.Body;
import io.github.libedi.mock.domain.Header;
import io.github.libedi.mock.domain.MockMessage;
import io.github.libedi.mock.domain.ResponseBody;
import io.github.libedi.mock.domain.SendRequestBody;
import io.github.libedi.mock.util.DataConverter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * MockServerChannelInitializerTest
 *
 * @author libed
 *
 */
class MockServerChannelInitializerTest {
    
    EmbeddedChannel channel;

    @BeforeEach
    void init() {
        final int maxFrameLength      = Header.LENGTH + 140; // header + body
        final int lengthFieldOffset   = 44;
        final int lengthFieldLength   = Integer.BYTES;
        final int lengthAdjustment    = 10;                  // Header.reserved's length is 10 bytes.
        final int initialBytesToStrip = 0;                   // do not strip header

        channel = new EmbeddedChannel();
        channel.pipeline()
                .addFirst(new LoggingHandler(LogLevel.INFO))
                .addLast(new LengthFieldBasedFrameDecoder(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
                        initialBytesToStrip))
                .addLast(new ByteToMockMessageDecoder())
                .addLast(new MockMessageToByteEncoder())
                .addLast(new SendRequestInboundHandler())
                .addLast(new ResultResponseInboundHandler())
                .addLast(new OtherRequestInboundHandler());
        assertThat(channel).isNotNull();
    }
    
    @DisplayName("LINK/REQ 수신 데이터 테스트")
    @Test
    void testLinkRequest() {
        // given
        final MockMessage linkRequest = createLinkRequest();
        channel.writeInbound(linkRequest.toByteBuf());
        
        final MockMessage expectedLinkResponse = DataConverter.convertMessage(linkRequest);

        // when
        final ByteBuf actual = channel.readOutbound();
        
        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expectedLinkResponse.toByteBuf());
    }
    
    private MockMessage createLinkRequest() {
        final Header header = Header.builder()
                .messageType(MessageType.REQ)
                .messageSubType(MessageSubType.LINK)
                .source(Address.builder()
                        .cid(StringUtils.rightPad("123456789", 10, Character.MIN_VALUE))
                        .build())
                .destination(Address.builder()
                        .cid(StringUtils.rightPad("123456789", 10, Character.MIN_VALUE))
                        .build())
                .reserved(new byte[10])
                .build();
        return MockMessage.of(header, null);
    }
    
    @DisplayName("SEND/REQ 수신 데이터 테스트")
    @Test
    void testSendRequest() {
        // given
        final MockMessage sendRequest = createSendRequest();
        channel.writeInbound(sendRequest.toByteBuf());

        final MockMessage expectedSendResponse = DataConverter.convertMessage(sendRequest);

        // when
        final ByteBuf actualSendResponse = channel.readOutbound();
        
        // then
        assertThat(actualSendResponse).isNotNull();
        assertThat(MockMessage.from(actualSendResponse)).usingRecursiveComparison().ignoringFields("body.msgId")
                .isEqualTo(expectedSendResponse);

        // given
        final MockMessage expectedResultRequest = DataConverter.convertMessage(expectedSendResponse);

        // when
        final ByteBuf actualResultRequest = channel.readOutbound();

        // then
        assertThat(actualResultRequest).isNotNull();
        assertThat(MockMessage.from(actualResultRequest)).usingRecursiveComparison().ignoringFields("body.msgId")
                .isEqualTo(expectedResultRequest);
        
        // given
        final MockMessage resultResponse = createResultResponse(expectedResultRequest);
        channel.writeInbound(resultResponse.toByteBuf());

        // when
        final Object actual = channel.readOutbound();
        
        // then
        assertThat(actual).isNull();
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
    
    private MockMessage createResultResponse(final MockMessage message) {
        final Body body = ResponseBody.builder()
                .result(Result.SUCCESS)
                .build();
        final Header header = Header.builder()
                .messageType(MessageType.RES)
                .messageSubType(MessageSubType.RESULT)
                .source(message.getHeader().getSource())
                .destination(message.getHeader().getDestination())
                .bodyLength(body.getDataLength())
                .reserved(message.getHeader().getReserved())
                .build();
        return MockMessage.of(header, body);
    }

}
