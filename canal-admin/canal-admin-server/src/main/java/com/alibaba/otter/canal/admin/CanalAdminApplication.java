package com.alibaba.otter.canal.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 启动入口
 *
 * @author rewerma @ 2018-10-20
 * @version 1.0.0
 */
@SpringBootApplication
public class CanalAdminApplication {
    private static final Logger log = LoggerFactory.getLogger(CanalAdminApplication.class);

    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(CanalAdminApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        ConfigurableApplicationContext context = application.run(args);

        Environment env = context.getEnvironment();
        String host = ip4().get(0);
        String port = env.getProperty("server.port");
        log.info("\n----------------------------------------------------------\n\t" +
                        "Application is running! Access URLs:\n\t" +
                        "External: \thttp://{}:{}\n" +
                        "----------------------------------------------------------\n"
                , host, port
        );
    }

    public static List<String> ip4() {
        List<String> ip4List = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                if (ni == null || ni.getMTU() <= 0) {
                    continue;
                }
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (inetAddress == null
                            || inetAddress instanceof Inet4Address == false
                            || inetAddress.getHostAddress().endsWith(".1")
                    ) {
                        continue;
                    }
                    ip4List.add(inetAddress.getHostAddress());
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return ip4List;
    }
}
