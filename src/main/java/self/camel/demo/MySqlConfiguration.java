package self.camel.demo;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySqlConfiguration {
	private static final Logger LOG = LoggerFactory.getLogger(MySqlConfiguration.class);
	@Value("${spring.datasource.driver-class-name}")
	private String driverClassName;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String userName;
	@Value("${spring.datasource.password}")
	private String password;
	
	
	@Bean(name = "mySqlConn")
	public Connection buildMySqlConnection(){
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(driverClassName);
		ds.setUrl(url);
		ds.setUsername(userName);
		ds.setPassword(password);
		LOG.debug("Created DataSource:{}",ds.getUrl());
		try {
			Connection conn = ds.getConnection();
			return conn;
		}catch(SQLException sqle) {
			LOG.error("Error while obtaining exception:{}",sqle.getMessage());
		}		
		return null;
	}
}
