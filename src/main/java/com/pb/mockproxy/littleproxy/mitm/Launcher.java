//package com.pb.mockproxy.littleproxy.mitm;
//
//import java.io.File;
//
//import org.apache.log4j.xml.DOMConfigurator;
//import com.pb.mockproxy.littleproxy.proxy.HttpProxyServerBootstrap;
//import com.pb.mockproxy.littleproxy.proxy.impl.DefaultHttpProxyServer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class Launcher {
//
//    private static final Logger log = LoggerFactory.getLogger(Launcher.class);
//
//    public static void main(final String... args) {
//        File log4jConfigurationFile = new File(
//                "src/test/resources/log4j.xml");
//        if (log4jConfigurationFile.exists()) {
//            DOMConfigurator.configureAndWatch(
//                    log4jConfigurationFile.getAbsolutePath(), 15);
//        }
//        try {
//            final int port = 9090;
//
//            System.out.println("About to start server on port: " + port);
//            HttpProxyServerBootstrap bootstrap = DefaultHttpProxyServer
//                    .bootstrapFromFile("./littleproxy.properties")
//                    .withPort(port).withAllowLocalOnly(false);
//
//            bootstrap.withManInTheMiddle(new HostNameMitmManager());
//
//            System.out.println("About to start...");
//            bootstrap.start();
//
//        } catch (Exception e) {
//            log.error(e.getMessage(), e);
//            System.exit(1);
//        }
//    }
//
//}
