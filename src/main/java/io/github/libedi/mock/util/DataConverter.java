package io.github.libedi.mock.util;

import org.apache.commons.lang3.RandomUtils;

import io.github.libedi.mock.constant.MessageSubType;
import io.github.libedi.mock.constant.MessageType;
import io.github.libedi.mock.constant.Result;
import io.github.libedi.mock.constant.SendStatus;
import io.github.libedi.mock.domain.Body;
import io.github.libedi.mock.domain.Header;
import io.github.libedi.mock.domain.MockMessage;
import io.github.libedi.mock.domain.ResponseBody;
import io.github.libedi.mock.domain.ResultRequestBody;
import io.github.libedi.mock.domain.SendResponseBody;

/**
 * DataConverter
 *
 * @author libed
 *
 */
@SuppressWarnings("deprecation")
public final class DataConverter {

    private DataConverter() {
        throw new UnsupportedOperationException();
    }

    /**
     * 메시지 형식에 따른 데이터 변환
     *
     * @param message
     * @return
     */
    public static MockMessage convertMessage(final MockMessage message) {
        if (message.isLinkRequest()) {
            return convertLinkRequestToResponse(message.getHeader());
        }
        if (message.isSendRequest()) {
            return convertSendRequestToResponse(message.getHeader());
        }
        if (message.isSendResponse()) {
            return convertSendResponseToResultRequest(message);
        }
        return convertErrorResponse(message.getHeader());
    }

    /**
     * LINK Request에 대한 Response 전문 생성
     *
     * @param header
     * @return
     */
    private static MockMessage convertLinkRequestToResponse(final Header header) {
        return convertRequestToResponse(header, ResponseBody.builder()
                .result(Result.SUCCESS)
                .build());
    }

    /**
     * SEND Request에 대한 Response 전문 생성
     *
     * @param header
     * @return
     */
    private static MockMessage convertSendRequestToResponse(final Header header) {
        return convertRequestToResponse(header, SendResponseBody.sendBuilder()
                .result(generateResult())
                .build());
    }

    /**
     * SEND Response에 대한 RESULT Request 전문 생성
     *
     * @param message
     * @return
     */
    private static MockMessage convertSendResponseToResultRequest(final MockMessage message) {
        final SendResponseBody responseBody = (SendResponseBody) message.getBody();
        final Body body = ResultRequestBody.builder()
                .sendStatus(generateSendStatus())
                .msgId(responseBody.getMsgId())
                .build();
        final Header header = message.getHeader();
        return MockMessage.of(
                Header.builder()
                        .messageType(MessageType.REQ)
                        .messageSubType(MessageSubType.RESULT)
                        .source(header.getSource())
                        .destination(header.getDestination())
                        .bodyLength(body.getDataLength())
                        .reserved(header.getReserved())
                        .build(),
                body);
    }

    /**
     * 오류 응답 전문 생성
     *
     * @param header
     * @return
     */
    private static MockMessage convertErrorResponse(final Header header) {
        return convertRequestToResponse(header, ResponseBody.builder()
                .result(Result.FAIL)
                .build());
    }

    /**
     * 1 / 100_000 확률로 전송결과 오류 발생
     *
     * @return
     */
    private static SendStatus generateSendStatus() {
        if (generateRandomBoolean()) {
            return SendStatus.SUCCESS;
        }
        final SendStatus[] values = SendStatus.values();
        return values[RandomUtils.nextInt(1, values.length)];
    }

    /**
     * 1 / 100,000 의 확률로 FAIL 발생
     *
     * @return
     */
    private static Result generateResult() {
        return generateRandomBoolean() ? Result.SUCCESS : Result.FAIL;
    }

    /**
     * 1 / 100,000 의 확률로 false
     *
     * @return
     */
    private static boolean generateRandomBoolean() {
        return RandomUtils.nextInt(0, 100_000) != 1;
    }

    /**
     * Request 에 대한 Response 전문 생성
     *
     * @param header
     * @param body
     * @return
     */
    private static MockMessage convertRequestToResponse(final Header header, final Body body) {
        return MockMessage.of(
                Header.builder()
                        .messageType(MessageType.RES)
                        .messageSubType(header.getMessageSubType())
                        .source(header.getSource())
                        .destination(header.getDestination())
                        .bodyLength(body.getDataLength())
                        .reserved(header.getReserved())
                        .build(),
                body);
    }

}
