package ua.shplusplus.restapidevelopment;

import java.util.Locale;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestApiDevelopmentApp {

  private static final Logger logger = LoggerFactory.getLogger(RestApiDevelopmentApp.class);

  public static void main(String[] args) {

    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
    logger.info(bundle.getString("programStart"));

    SpringApplication.run(RestApiDevelopmentApp.class, args);
  }

}
