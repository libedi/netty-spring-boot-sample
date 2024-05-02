package io.github.libedi.mock.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MessageType
 *
 * @author libed
 *
 */
@RequiredArgsConstructor
@Getter
public enum MessageType implements CodeEnum<Short> {

    /** 요청: 1 */
    REQ((short) 1),
    /** 응답: 2 */
    RES((short) 2),
    /** 잘못된 메시지 형식 */
    INVALID(null);

    private final Short code;

    private static final Map<Short, MessageType> map = Arrays.stream(values())
            .collect(Collectors.toMap(CodeEnum::getCode, Function.identity()));

    public static MessageType from(final short code) {
        return map.getOrDefault(code, INVALID);
    }
}
