package io.github.libedi;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

/**
 * EchoServerTest
 *
 * @author libed
 *
 */
@Slf4j
@SpringBootTest
class EchoServerTest {

    @Test
    void test() throws Exception {
        // given
        log.info("Create Client Socket");
        try (final Socket socket = new Socket("127.0.0.1", 18080);
                final InputStream is = socket.getInputStream();
                final OutputStream os = socket.getOutputStream();) {
            log.info("Send Data");
            final String data = "test";
            os.write(data.getBytes());

            final byte[] recv = new byte[data.length()];
            is.read(recv);

            assertThat(recv).asString().isEqualTo(data);
        }
        log.info("Close Client Socket");
    }

}
