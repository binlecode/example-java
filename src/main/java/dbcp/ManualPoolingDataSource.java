package dbcp;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;

//
// construct the PoolingDataSource manually,
// just to show how the pieces fit together, but you could also
// configure it using an external conifguration file in
// JOCL format (and eventually Digester).
//
// Note that this example is very similiar to the PoolingDriver
// example.  In fact, you could use the same pool in both a
// PoolingDriver and a PoolingDataSource
//

//
// To compile this example, you'll want:
//  * commons-pool-1.5.4.jar
//  * commons-dbcp-1.2.2.jar
//  * j2ee.jar (for the javax.sql classes)
// in your classpath.
//
// To run this example, you'll want:
//  * commons-pool-1.5.4.jar
//  * commons-dbcp-1.2.2.jar
//  * j2ee.jar (for the javax.sql classes)
//  * the classes for your (underlying) JDBC driver
// in your classpath.
//
// Invoke the class using two arguments:
//  * the connect string for your underlying JDBC driver
//  * the query you'd like to execute
// You'll also want to ensure your underlying JDBC driver
// is registered.  You can use the "jdbc.drivers"
// property to do this.
//
// For example:
//  java -Djdbc.drivers=oracle.jdbc.driver.OracleDriver \
//       -classpath commons-pool-1.5.4.jar:commons-dbcp-1.2.2.jar:j2ee.jar:oracle-jdbc.jar:. \
//       ManualPoolingDataSourceExample \
//       "jdbc:oracle:thin:scott/tiger@myhost:1521:mysid" \
//       "SELECT * FROM DUAL"
//
public class ManualPoolingDataSource {

    public static void main(String[] args) {

        System.out.println("Loading underlying JDBC driver.");
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Done.");

        // TODO: use external properties file
        String jdbcUrl = "jdbc:oracle:thin:@10.253.15.6:1521:strcoor1";
        String username ="mms";
        String password ="mms";
        String sqlString = "select count(1) from user_objects";
        
        GenericObjectPool.Config poolConfig = new GenericObjectPool.Config();
        poolConfig.maxActive = 10;
        poolConfig.maxIdle = 10;
        poolConfig.minIdle = 0;
        poolConfig.maxWait = 1000;
        
        System.out.println("Setting up data source.");
        DataSource dataSource = setupDataSource(jdbcUrl, username, password, poolConfig);
        System.out.println("Done.");

        //
        // Now, we can use JDBC DataSource as we normally would.
        //
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            System.out.println("Creating connection.");
            conn = dataSource.getConnection();
            System.out.println("Creating statement.");
            stmt = conn.createStatement();
            System.out.println("Executing statement.");
            rs = stmt.executeQuery(sqlString);
            System.out.println("Results:");
            int numcols = rs.getMetaData().getColumnCount();
            while(rs.next()) {
                for(int i=1;i<=numcols;i++) {
                    System.out.print("\t" + rs.getString(i));
                }
                System.out.println("");	
            }
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch(Exception e) { }
            try { if (stmt != null) stmt.close(); } catch(Exception e) { }
            try { if (conn != null) conn.close(); } catch(Exception e) { }
        }
    }

    public static DataSource setupDataSource(
    		String connectURI,
    		String username, 
    		String password,
    		GenericObjectPool.Config poolConfig
    		) {
        //
        // First, we'll need a ObjectPool that serves as the
        // actual pool of connections.
        //
        // We'll use a GenericObjectPool instance, although
        // any ObjectPool implementation will suffice.
        //
    	GenericObjectPool connectionPool = new GenericObjectPool(null, poolConfig);



        //
        // Next, we'll create a ConnectionFactory that the
        // pool will use to create Connections.
        // We'll use the DriverManagerConnectionFactory,
        // using the connect string passed in the command line
        // arguments.
        //
//        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, username, password);
		Properties connProps = new Properties();
		connProps.setProperty("user", username);
		connProps.setProperty("password", password);
		connProps.setProperty("useUnicode", "true");
		connProps.setProperty("characterEncoding", "UTF-8");
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(connectURI, connProps);

        //
        // Now we'll create the PoolableConnectionFactory, which wraps
        // the "real" Connections created by the ConnectionFactory with
        // the classes that implement the pooling functionality.
        //
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory,connectionPool,null,null,false,true);

        //
        // Finally, we create the PoolingDriver itself,
        // passing in the object pool we created.
        //
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

        return dataSource;
    }
}
