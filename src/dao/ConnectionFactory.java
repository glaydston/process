package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.sql.DataSource;

import util.Messages;

/**
 * @author Glaydston Veloso
 * @since 1.0.0
 * @date 08/09/2013
 * @mail glaydston.veloso@plansis.com.br
 */
public class ConnectionFactory {
	private final String DRIVER = "com.ibm.db2.jcc.DB2Driver";

	private Connection connection = null;
	private Statement stmt = null;

	private String lookup = "jdbc/db2";

	private String server;
	private String port;
	private String db;
	private String user;
	private String pass;

	/**
	 * Constructor
	 */
	public ConnectionFactory() {
	}

	/**
	 * Seta as informacoes de conexao para conexao via URL
	 * 
	 * @param server
	 * @param port
	 * @param db
	 * @param user
	 * @param pass
	 */
	private void setURLAccess() {
		server = "localhost";
		port = "50000";
		db = "LMDB";
		user = "db2inst1";
		pass = "db2inst1";
	}

	/**
	 * Seta a informacao de lookup para conexao
	 * 
	 * @param lookup
	 */
	public void setLookupAccess(String lookup) {
		this.lookup = lookup;
	}

	/**
	 * Conecta no banco de dados
	 * 
	 * @param lookup
	 *            (true -> conexao via lookup / false -> conexao via URL )
	 * @throws Exception
	 */
	public void connect(boolean lookup) {
		try {
			if (lookup) {
				System.out.println(Messages.LOOKUP_CON);
				connectLookup();
			} else {
				System.out.println(Messages.DB_CON);
				setURLAccess();
				connectURL();
			}
		} catch (Exception e) {
			System.out.println(Messages.ERR00);
		}
	}

	private void connectLookup() throws Exception {
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup(this.lookup);
			connection = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (connection == null) {
			throw new Exception(Messages.DBERR01);
		}
	}

	private void connectURL() throws Exception {
		try {
			Class.forName(DRIVER);
			String url = "jdbc:db2://" + server + ":" + port + "/" + db;
			connection = DriverManager.getConnection(url, user, pass);
			if (connection == null) {
				throw new Exception(Messages.DBERR01);
			}
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver de conexão não encontrado: "+ e.getMessage());
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Fecha a conexao com o banco
	 */
	public void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				System.out.println("Falha ao fechar conexao: " + e.getMessage()
						+ ": " + e.getLocalizedMessage());
			}
		}
	}

	/**
	 * Executa uma query SELECT
	 * 
	 * @param (StringBuffer) sql
	 * @return
	 * @throws Exception
	 */
	public final ResultSet executeQuery(StringBuffer sql) throws Exception {
		ResultSet rs = null;
		try {
			System.out.println("Query: " + sql.toString());
			stmt = null;
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql.toString());
		} catch (SQLException e) {
			throw new Exception("Falha ao executar query (executeQuery) : "
					+ sql.toString(),  e);
		} catch (Exception e) {
			throw new Exception("Falha ao executar query (executeQuery) : "
					+ sql.toString(), e);
		}
		return rs;
	}

	/**
	 * Executa uma query SELECT
	 * 
	 * @param (String) sql
	 * @return
	 * @throws Exception
	 */
	public final ResultSet executeQuery(String sql) throws Exception {
		ResultSet rs = null;
		try {
			System.out.println("Query: " + sql.toString());
			stmt = null;
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			throw new Exception("Falha ao executar query (executeQuery) : "
					+ sql, e);
		} catch (Exception e) {
			throw new Exception("Falha ao executar query (executeQuery) : "
					+ sql, e);
		}
		return rs;
	}

	/**
	 * Executa uma query Select
	 * 
	 * @param (PreparedStatement) stmt
	 * @throws Exception
	 */
	public final ResultSet executeQuery(PreparedStatement stmt)
			throws Exception {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
		} catch (SQLException se) {
			throw new Exception("Falha ao executar query (executeQuery) : "
					+ se);
		} catch (Exception e) {
			throw new Exception("Falha ao executar query (executeQuery) : " + e);
		}
		return rs;
	}

	/**
	 * Executa uma query UPDATE
	 * 
	 * @param (StringBuffer) sql
	 * @throws Exception
	 */
	public final void updateQuery(StringBuffer sql) throws Exception {
		try {
			connection.setAutoCommit(false);
			stmt = null;
			stmt = connection.createStatement();
			stmt.executeUpdate(sql.toString());
			connection.commit();
		} catch (SQLException se) {
			connection.rollback();
			throw new Exception("Falha ao executar query (updateQuery) :" + sql, se);
		} catch (Exception e) {
			connection.rollback();
			throw new Exception("Falha ao executar query (updateQuery) :" + sql, e);
		}
	}

	/**
	 * Executa uma query Update
	 * 
	 * @param (PreparedStatement) stmt
	 * @throws Exception
	 */
	public final void updateQuery(PreparedStatement stmt) throws Exception {
		try {
			connection.setAutoCommit(false);
			stmt.executeUpdate();
			connection.commit();
		} catch (SQLException se) {
			connection.rollback();
			throw new Exception("Falha ao executar query (updateQuery) : " + se);
		} catch (Exception e) {
			connection.rollback();
			throw new Exception("Falha ao executar query (updateQuery) : " + e);
		}
	}

	/**
	 * Executa uma query INSERT
	 * 
	 * @param (StringBuffer) sql
	 * @throws Exception
	 */
	public final void insertQuery(StringBuffer sql) throws Exception {
		try {
			connection.setAutoCommit(false);
			stmt = null;
			stmt = connection.createStatement();
			stmt.execute(sql.toString());
			connection.commit();
		} catch (SQLException se) {
			connection.rollback();
			throw new Exception("Falha ao executar query (insertQuery) : "
					+ sql, se);
		} catch (Exception e) {
			connection.rollback();
			throw new Exception("Falha ao executar query (insertQuery) : "
					+ sql, e);
		}
	}

	/**
	 * Executa uma query INSERT
	 * 
	 * @param (PreparedStatement) stmt
	 * @throws Exception
	 */
	public final void insertQuery(PreparedStatement stmt) throws Exception {
		try {
			connection.setAutoCommit(false);
			stmt.execute();
			connection.commit();
		} catch (SQLException se) {
			connection.rollback();
			throw new Exception("Falha ao executar query (insertQuery) : " + se);
		} catch (Exception e) {
			connection.rollback();
			throw new Exception("Falha ao executar query (insertQuery) : " + e);
		}
	}

	/**
	 * @return
	 */
	public final Connection returnConnection() {
		return this.connection;
	}
}
