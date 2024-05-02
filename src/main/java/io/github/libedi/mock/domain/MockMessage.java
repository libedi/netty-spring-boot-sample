package io.github.libedi.mock.domain;

import io.github.libedi.mock.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * MockMessage
 *
 * @author libed
 *
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
@EqualsAndHashCode
public class MockMessage implements Convertible {

    private final Header header;
    private final Body   body;

    @Override
    public int getDataLength() {
        return header.getDataLength() + body.getDataLength();
    }

    @Override
    public ByteBuf toByteBuf() {
        return ByteBufUtil.createByteBuf(getDataLength(), header, body);
    }

    public static MockMessage from(final ByteBuf buf) {
        final Header header = Header.from(buf);
        return new MockMessage(header, Body.from(header, buf));
    }

    public static MockMessage of(final Header header, final Body body) {
        return new MockMessage(header, body);
    }

    public static int getTotalLength(final ByteBuf buf) {
        return Header.LENGTH + buf.getInt(44);
    }

    public boolean isLinkRequest() {
        return header.isLinkRequest();
    }

    public boolean isLinkResponse() {
        return header.isLinkResponse();
    }

    public boolean isSendRequest() {
        return header.isSendRequest();
    }

    public boolean isSendResponse() {
        return header.isSendResponse();
    }

    public boolean isResultRequest() {
        return header.isResultRequest();
    }

    public boolean isResultResponse() {
        return header.isResultResponse();
    }

}
