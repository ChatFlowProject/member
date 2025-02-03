package chatflow.memberservice;

//import com.netflix.discovery.EurekaClient;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@RequiredArgsConstructor
@SpringBootApplication
//@EnableDiscoveryClient
public class MemberServiceApplication {
//    private final EurekaClient eurekaClient;

    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }

//    @PreDestroy
//    public void unregister() {
//        eurekaClient.shutdown();
//    }
}
