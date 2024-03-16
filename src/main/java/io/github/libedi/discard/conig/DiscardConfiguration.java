package io.github.libedi.discard.conig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.libedi.discard.handler.DiscardChannelInitializer;
import io.github.libedi.discard.handler.DiscardServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * DiscardConfiguration
 *
 * @author libed
 *
 */
@Configuration
public class DiscardConfiguration {

    /**
     * 들어오는 연결을 받아들이는 이벤트 루프. 연결을 수락하면 수락된 연결을 workerGroup에 등록한다.
     *
     * @return
     */
    @Bean(destroyMethod = "shutdownGracefully")
    NioEventLoopGroup discardBossGroup() {
        return new NioEventLoopGroup();
    }

    /**
     * boss가 수락한 연결의 트래픽을 처리하는 이벤트 루프
     *
     * @return
     */
    @Bean(destroyMethod = "shutdownGracefully")
    NioEventLoopGroup discardWorkerGroup() {
        return new NioEventLoopGroup();
    }

    @Bean
    DiscardChannelInitializer discardChannelInitializer(final DiscardServerHandler discardServerHandler) {
        return new DiscardChannelInitializer(discardServerHandler);
    }

    /**
     * 서버를 생성하는 헬퍼 클래스
     *
     * @param discardChannelInitializer
     * @return
     */
    @Bean
    ServerBootstrap discardServerBootstrap(final DiscardChannelInitializer discardChannelInitializer) {
        return new ServerBootstrap()
                .group(discardBossGroup(), discardWorkerGroup())
                .channel(NioServerSocketChannel.class) // 들어오는 연결을 수락하기 위한 채널 지정
                .childHandler(discardChannelInitializer) // 채널의 요청을 처리하기 위한 파이프라인을 구성하는 ChannelHandler 지정
                .option(ChannelOption.SO_BACKLOG, 128) // 서버 소켓 채널용 옵션
                .childOption(ChannelOption.SO_KEEPALIVE, true); // 소켓 채널용 옵션
    }
    
}