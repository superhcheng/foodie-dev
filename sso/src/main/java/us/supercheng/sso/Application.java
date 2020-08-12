package us.supercheng.sso;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@ComponentScan(basePackages = {"us.supercheng", "org.n3r.idworker"})
//@MapperScan(basePackages = "us.supercheng.mapper")

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}