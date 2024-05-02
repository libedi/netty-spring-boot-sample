package io.github.libedi.mock.domain;

import io.netty.buffer.ByteBuf;

/**
 * NettyMessage
 *
 * @author libed
 *
 */
public interface Convertible {

    int getDataLength();

    ByteBuf toByteBuf();

}
