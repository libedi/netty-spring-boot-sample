package io.github.libedi.time.client.handler;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

/**
 * TimeClientHandler
 *
 * @author libed
 *
 */
@Slf4j
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

//	private ByteBuf buf; // 2.

	/*
	 * 2. ChannelHandler의 수명주기 메소드, handlerAdded()와 handlerRemoved()를 통해 초기화 작업 수행
	 * 가능
	 */
//	@Override
//	public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
//		buf = ctx.alloc().buffer(4);
//	}
//
//	@Override
//	public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
//		buf.release();
//		buf = null;
//	}

	@Override
	public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
		/*
		 * 1. TCP/IP와 같은 스트림 기반 전송에서 수신된 데이터는 패킷 단위가 아닌 바이트 단위로 수신된다.
		 * 따라서 원하는 패킷 길이로 오지 않아 조각화 될 가능성이 있어,
		 * 아래 코드는 IndexOutOfBoundsException가 발생할 수 있다.
		 */
		final ByteBuf m = (ByteBuf) msg;
		/*
		 * 3. 조각화를 방지하기 이해 원하는 크기의 데이터로 수신 데이터 누적
		 */
//		buf.writeBytes(m);
//		m.release();

		// 4.
		try {
			final long currentTimeMills = (m.readUnsignedInt() - 2208988800L) * 1000L;
			log.debug("{}", new Date(currentTimeMills));
			ctx.close();
		} finally {
			m.release();
		}

		/*
		 * 4. 충분한 데이터가 있는지 확인하고 비즈니스 로직 수행
		 */
//		if (buf.readableBytes() >= 4) {
//			final long currentTimeMills = (m.readUnsignedInt() - 2208988800L) * 1000L;
//			log.debug("{}", new Date(currentTimeMills));
//			ctx.close();
//		}

		/*
		 * 5. 하지만 코드가 깔끔하지 못하고, 가변 길이 같은 복잡한 프로토콜 사용시 이 핸들러는 매우 빠르게 유지보수가 어려워질 것이다. 따라서
		 * ChannelPipeline 에 하나 이상의 모듈로 분리하여 복잡성을 줄여보자. ==> TImeClientDecoder
		 */
		// 6. TimeClientDecoder에 패킷 단편화 처리를 위임하고, 위 코드들은 비즈니스 코드만 남기고 원복
	}

	@Override
	public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
		log.error(cause.getMessage(), cause);
		ctx.close();
	}

}
