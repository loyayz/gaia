package sample;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author loyayz (loyayz@foxmail.com)
 */
@SpringBootApplication
@MapperScan("sample.mapper")
public class SampleMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(SampleMybatisApplication.class, args);
    }

}
