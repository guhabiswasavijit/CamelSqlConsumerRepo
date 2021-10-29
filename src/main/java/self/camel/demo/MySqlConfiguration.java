package self.camel.demo;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySqlConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(MySqlConfiguration.class);
	@Autowired
	private DataSource dataSource;
	
	@Bean(name = "mySqlConn")
	public Connection buildMySqlConnection(){
		try {
			Connection conn = dataSource.getConnection();
			return conn;
		}catch(SQLException sqle) {
			LOG.error("Error while obtaining exception:{}",sqle.getMessage());
		}		
		return null;
	}

}
