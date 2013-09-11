package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletRequest;

import bo.PumaBO;

import util.Messages;

import model.Member;

/**
 * @author Glaydston Veloso
 * @since 1.0.0
 * @date 08/09/2013
 * @mail glaydston.veloso@plansis.com.br
 */
public class MemberDao {
	private final boolean lookup = false;
	private ConnectionFactory factory;
	private StringBuffer sql;

	public MemberDao() {
		factory = new ConnectionFactory();
		factory.connect(lookup);		
	}

	/**
	 * Set member in db
	 * 
	 * @param m
	 * @return String
	 */
	public String setMember(Member m) {
		String message = Messages.INIT;
		sql = new StringBuffer();
		sql.append("INSERT INTO MEMBER(NAME, EMAIL) VALUES( ?, ?)");

		// Get connection from ConnectionFactory()
		Connection con = factory.returnConnection();
		try {
			// Prepare the sql to execute in ConnectionFactory
			PreparedStatement stmt = con.prepareStatement(sql.toString());

			stmt.setString(1, m.getName());
			stmt.setString(2, m.getEmail());

			factory.insertQuery(stmt);
			stmt.close();
			message = Messages.SCS01.replace("@", m.getName());
		} catch (SQLException e) {
			System.out.println("-> ConnectionFactory() > SQLException caught: " + e.getMessage());
			message = Messages.ERR01;
		} catch (Exception e) {
			System.out.println("-> ConnectionFactory() > Exception caught: " + e.getMessage());
			message = Messages.ERR02;
		} finally {
			factory.closeConnection();
		}

		return message;
	}
	
	/**
	 * Set all members aren't in db
	 * @param request
	 */
	public void setMembers(PortletRequest request){
		PumaBO puma = new PumaBO();
		// Get portlet's request
		puma.setPortletRequest(request);
		List<Member> members = puma.getAllMembers();

		for (Member m : members){ 
			factory.connect(lookup);
			setMember(m);				
		}
	}
	
	/**
	 * Get just member in db
	 * 
	 * @param uid
	 * @return Member
	 */
	public Member getMember(long uid) {
		sql = new StringBuffer();
		sql.append("SELECT UID, NAME, EMAIL FROM MEMBER WHERE UID = " + uid);
		
		Member m = null;
		try {
			ResultSet rs = factory.executeQuery(sql);

			m = new Member();
			rs.next();
			m.setUid(rs.getLong("UID"));
			m.setName(rs.getString("NAME"));
			m.setEmail(rs.getString("EMAIL"));
			
			rs.close();

		} catch (SQLException e) {
			System.out.println("-> ConnectionFactory() > SQLException caught: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("-> ConnectionFactory() > Exception caught: " + e.getMessage());
		} finally {
			factory.closeConnection();
		}
		return m;

	}
	
	/**
	 * Get just member in db
	 * 
	 * @param uid
	 * @return Member
	 */
	public Member getMember(String name) {
		sql = new StringBuffer();
		sql.append("SELECT UID, NAME, EMAIL FROM MEMBER WHERE NAME = " + name);

		Member m = null;
		try {

			ResultSet rs = factory.executeQuery(sql);
			
			m = new Member();
			rs.next();
			m.setUid(rs.getLong("UID"));
			m.setName(rs.getString("NAME"));
			m.setEmail(rs.getString("EMAIL"));

			rs.close();

		} catch (SQLException e) {
			System.out.println("-> ConnectionFactory() > SQLException caught: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("-> ConnectionFactory() > Exception caught: " + e.getMessage());
		} finally {
			factory.closeConnection();
		}
		return m;
	}

	/**
	 * Get all members in db
	 * 
	 * @return List<Member>
	 */
	public List<Member> getMembers() {
		sql = new StringBuffer();
		sql.append("SELECT UID, NAME, EMAIL FROM MEMBER");
		List<Member> members = new ArrayList<Member>();
		try {
			Member m = null;

			ResultSet rs = factory.executeQuery(sql);
			while (rs.next()) {
				m = new Member();
				m.setUid(rs.getLong("UID"));
				m.setName(rs.getString("NAME"));
				m.setEmail(rs.getString("EMAIL"));

				members.add(m);
			}

			rs.close();

		} catch (SQLException e) {
			System.out.println("-> ConnectionFactory() > SQLException caught: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("-> ConnectionFactory() > Exception caught: " + e.getMessage());
		} finally {
			factory.closeConnection();
		}
		return members;
	}
	
	/**
	 * See if member is in db
	 * 
	 * @param name
	 * @return boolean
	 */
	public boolean getIsMember(long uid) {
		boolean retorno = false;
		sql = new StringBuffer();
		sql.append("SELECT UID, NAME, EMAIL FROM MEMBER WHERE UID = " + uid);

		try {

			ResultSet rs = factory.executeQuery(sql);			
			retorno = rs.next();
			rs.close();
			
		} catch (SQLException e) {
			System.out.println("-> ConnectionFactory() > SQLException caught: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("-> ConnectionFactory() > Exception caught: " + e.getMessage());
		} finally {
			factory.closeConnection();
		}
		return retorno;
	}
	
	/**
	 * See if member is in db
	 * 
	 * @param name
	 * @return boolean
	 */
	public boolean getIsMember(String name) {
		boolean retorno = false;
		sql = new StringBuffer();
		sql.append("SELECT UID, NAME, EMAIL FROM MEMBER WHERE NAME = '" + name +"'");
		
		try {

			ResultSet rs = factory.executeQuery(sql);	
			retorno = rs.next();
			rs.close();
			
		} catch (SQLException e) {
			System.out.println("-> ConnectionFactory() > SQLException caught: " + e.getMessage());
		} catch (Exception e) {
			System.out.println("-> ConnectionFactory() > Exception caught: " + e.getMessage());
		} finally {
			factory.closeConnection();
		}
		return retorno;
	}
}
