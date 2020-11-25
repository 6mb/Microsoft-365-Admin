package cn.itbat.microsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Microsoft 365 管理平台
 *
 * @author mjj
 */
@EnableAsync
@SpringBootApplication
public class MicrosoftApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicrosoftApplication.class, args);
    }

}
