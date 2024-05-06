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
import io.github.libedi.mock.domain.Header;
import io.github.libedi.mock.domain.MockMessage;
import io.github.libedi.mock.util.DataConverter;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.embedded.EmbeddedChannel;

/**
 * OtherRequestInboundHandlerTest
 *
 * @author libed
 *
 */
class OtherRequestInboundHandlerTest {

    ChannelInboundHandler channelInboundHandler;
    EmbeddedChannel       channel;

    @BeforeEach
    void init() {
        channelInboundHandler = new OtherRequestInboundHandler();
        channel = new EmbeddedChannel(channelInboundHandler) {
            @Override
            protected SocketAddress localAddress0() {
                return new InetSocketAddress(8080);
            }
        };
        assertThat(channel).isNotNull();
    }
    
    @Test
    void test() {
        // given
        final MockMessage linkRequest = createLinkRequest();
        channel.writeInbound(linkRequest);
        
        final MockMessage expectedLinkResponse = DataConverter.convertMessage(linkRequest);

        // when
        final MockMessage actual = channel.readOutbound();
        
        // then
        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(expectedLinkResponse);
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
