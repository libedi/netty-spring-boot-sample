package io.github.libedi;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;

/**
 * DiscardServerTest
 *
 * @author libed
 *
 */
@Slf4j
@SpringBootTest
class DiscardServerTest {

    @Test
    void test() throws Exception {
        // given
        log.info("Create Client Socket");
        try (final Socket socket = new Socket("127.0.0.1", 18080);
                final InputStream is = socket.getInputStream();
                final OutputStream os = socket.getOutputStream();) {
            log.info("Send Data");
            os.write("test".getBytes());
        }
        log.info("Close Client Socket");
    }

    public static void main(final String[] args) throws Exception {
        log.info("Create Client Socket");
        try (final Socket socket = new Socket("127.0.0.1", 18080);
                final InputStream is = socket.getInputStream();
                final OutputStream os = socket.getOutputStream();) {
            log.info("Send Data");
            os.write("test".getBytes());
        }
        log.info("Close Client Socket");
    }
}
