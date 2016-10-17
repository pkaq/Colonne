package pkaq.colonne;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created with IntelliJ IDEA.
 * Author: S.PKAQ
 * Datetime: 2016-10-17 15:54
 */
@SpringBootApplication
@ComponentScan("pkaq.colonne.restful")
public class Booter implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("spring started");
    }

    public static void main(String[] args) {
        SpringApplication.run(Booter.class,args);
    }
}
