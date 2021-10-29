package self.camel.demo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("mySqlDBHandler")
public class MySqlDBHandler {
	private static final Logger LOG = LoggerFactory.getLogger(MySqlDBHandler.class);
	@Autowired
	private Connection mySqlConn;
	
	public void onSuccess() {
		try {
			mySqlConn.commit();
		}catch(SQLException sqle) {
			LOG.error("Error while commiting transaction:{}",sqle.getMessage());
		}
	}
	public void onFailure() {
		try {
			mySqlConn.rollback();
		}catch(SQLException sqle) {
			LOG.error("Error while rollback transaction:{}",sqle.getMessage());
		}
	}
	public void execute(Exchange exchange) {
		String sqlStr = exchange.getIn().getBody(String.class);
		LOG.debug("about to run sql:{}",sqlStr);
		try {
			Statement stmt = mySqlConn.createStatement();
			stmt.executeUpdate(sqlStr);
		}catch(SQLException sqle) {
			LOG.error("Error while running sql:{}",sqle.getMessage());
		}
		
	}
}
