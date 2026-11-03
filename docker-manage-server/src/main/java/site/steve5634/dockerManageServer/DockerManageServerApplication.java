package site.steve5634.dockerManageServer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("site.steve5634.dockerManageServer.mapper")
@SpringBootApplication
public class DockerManageServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DockerManageServerApplication.class, args);
    }

}
