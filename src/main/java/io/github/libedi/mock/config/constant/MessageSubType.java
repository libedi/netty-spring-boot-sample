package io.github.libedi.mock.config.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * MessageSubType
 *
 * @author libed
 *
 */
@RequiredArgsConstructor
@Getter
public enum MessageSubType implements CodeEnum<Short> {

	/** 연결: 1 */
	LINK((short) 1),
	/** 전송: 2 */
	SEND((short) 2),
	/** 결과: 3 */
	RESULT((short) 3),
	/** 잘못된 메시지 형식 */
	INVALID(null);

	private final Short code;

	private static final Map<Short, MessageSubType> map = Arrays.stream(values())
			.collect(Collectors.toMap(CodeEnum::getCode, Function.identity()));

	public static MessageSubType from(final short code) {
		return map.getOrDefault(code, INVALID);
	}
}
