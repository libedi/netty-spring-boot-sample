package io.github.libedi.mock.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Result
 *
 * @author libed
 *
 */
@RequiredArgsConstructor
@Getter
public enum Result implements CodeEnum<Integer> {

    /** 성공: 0 */
    SUCCESS(0),
    /** 실패: 10 */
    FAIL(10);

    private final Integer code;

    private static final Map<Integer, Result> map = Arrays.stream(values())
            .collect(Collectors.toMap(CodeEnum::getCode, Function.identity()));

    public static Result from(final int code) {
        return map.get(code);
    }
}
