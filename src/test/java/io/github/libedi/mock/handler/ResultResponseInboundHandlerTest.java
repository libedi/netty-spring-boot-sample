package io.github.libedi.mock.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

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
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * ResultResponseInboundHandlerTest
 *
 * @author libed
 *
 */
class ResultResponseInboundHandlerTest {

    ChannelInboundHandler inboundHandler;
    EmbeddedChannel       channel;

    @BeforeEach
    void init() {
        inboundHandler = new ResultResponseInboundHandler();
        channel = new EmbeddedChannel(inboundHandler) {
            @Override
            protected SocketAddress localAddress0() {
                return new InetSocketAddress(8080);
            }
        };
        assertThat(channel).isNotNull();
    }
    
    @DisplayName("수신 데이터가 RESULT/RES 인 경우")
    @Test
    void testResultResponse() {
        // given
        final MockMessage resultResponse = createResultResponse();
        channel.writeInbound(resultResponse);
        
        // when
        final Object actual = channel.readInbound();
        
        // then
        assertThat(actual).isNull();
    }
    
    private MockMessage createResultResponse() {
        final Body body = ResponseBody.builder()
                .result(Result.SUCCESS)
                .build();
        final Header header = Header.builder()
                .messageType(MessageType.RES)
                .messageSubType(MessageSubType.RESULT)
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
    
    @DisplayName("수신 데이터가 RESULT/RES 가 아닌 경우")
    @Test
    void testNotResultResponse() {
        // given
        final MockMessage expected = createLinkRequest();
        channel.writeInbound(expected);
        
        // when
        final MockMessage actual = channel.readInbound();
        
        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expected);
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

}
