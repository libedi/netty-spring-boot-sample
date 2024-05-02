package io.github.libedi.mock.domain;

import io.netty.buffer.ByteBuf;

/**
 * Body
 *
 * @author libed
 *
 */
public sealed interface Body extends Convertible permits SendRequestBody, ResultRequestBody, ResponseBody {

    /**
     * <code>ByteBuf</code> 에서 Body 데이터 변환
     *
     * @param header
     * @param buf
     * @return
     */
    static Body from(final Header header, final ByteBuf buf) {
        if (header == null || buf == null || header.getBodyLength() == 0 || !buf.isReadable()) {
            return null;
        }
        if (header.isSendRequest()) {
            return SendRequestBody.from(buf);
        }
        if (header.isLinkResponse() || header.isResultResponse()) {
            return ResponseBody.from(buf);
        }
        if (header.isSendResponse()) {
            return SendResponseBody.from(buf);
        }
        if (header.isResultRequest()) {
            return ResultRequestBody.from(buf);
        }
        return null;
    }

}
