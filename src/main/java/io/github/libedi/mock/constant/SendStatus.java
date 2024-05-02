package io.github.libedi.mock.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * SendStatus
 *
 * @author libed
 *
 */
@RequiredArgsConstructor
@Getter
public enum SendStatus implements CodeEnum<Byte> {

    /** 전송 성공 : 0 */
    SUCCESS((byte) 0),
    /** 타임아웃 : 1 */
    TIMEOUT((byte) 1),
    /** 미가입 사용자 : 2 */
    NOT_FOUND((byte) 2),
    /** 스팸 차단 : 3 */
    BLOCKED((byte) 3);

    private final Byte code;

    private static final Map<Byte, SendStatus> map = Arrays.stream(values())
            .collect(Collectors.toMap(CodeEnum::getCode, Function.identity()));

    public static SendStatus from(final byte code) {
        return map.get(code);
    }
}
