package self.camel.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HelloMain {
    public static void main(String[] args) throws Exception {
    	ConfigurableApplicationContext ctx = SpringApplication.run(HelloMain.class, args);
		/*
		 * RouteStartUpController controller =
		 * (RouteStartUpController)ctx.getBean("routeStartUpController");
		 * controller.startRoutes();
		 */
    }
}
