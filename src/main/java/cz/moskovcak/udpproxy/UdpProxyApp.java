package cz.moskovcak.udpproxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class UdpProxyApp {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
        UdpProxyService proxyService = context.getBean(UdpProxyService.class);
        System.out.println("Starting udpproxy service");
        try {
            proxyService.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
