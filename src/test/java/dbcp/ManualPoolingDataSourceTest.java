package dbcp;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.junit.Before;
import org.junit.Test;

public class ManualPoolingDataSourceTest {

	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public void testPoolingDataSource() throws Exception {
		System.out.println("running testPoolingDataSource");
		
		String jdbcUrl = "jdbc:oracle:thin:@10.253.15.34:1521:dev11gr2";
		String username ="mms";
		String password ="mms";
		String sqlString = "select count(1) as count from user_objects";
		
		
		GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
		poolConfig.maxActive = 4;
		poolConfig.maxIdle = 2;
		poolConfig.minIdle = 0;
		poolConfig.maxWait = 10000;
		
		DataSource ds = ManualPoolingDataSource.setupDataSource(jdbcUrl, username, password, poolConfig);
		
		Class.forName("oracle.jdbc.OracleDriver");
		
		Connection conn = ds.getConnection();
		
		Statement stmt = conn.createStatement();
		
		ResultSet rs = stmt.executeQuery(sqlString);
		
		System.out.println("running query: " + sqlString);
		System.out.println("  returning result count: " + rs.getInt("count"));
		
		conn.close();
		
	}
	
	
	
	

}
