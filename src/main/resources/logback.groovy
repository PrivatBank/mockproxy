import ch.qos.logback.classic.jul.LevelChangePropagator
import org.slf4j.bridge.SLF4JBridgeHandler

import java.nio.charset.StandardCharsets

//use instead configuration debug="true"
statusListener(OnConsoleStatusListener)

//java.util.logging to slf4j
def lcp = new LevelChangePropagator()
lcp.context = context
lcp.resetJUL = true
context.addListener(lcp)
SLF4JBridgeHandler.install()

context.name = "MockProxy"

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        charset = StandardCharsets.UTF_8
        pattern = "%d %contextName [%thread] %-5level %logger - %msg %mdc%n"
    }
}
root(INFO, ["STDOUT"])