package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Config.Config;
import Model.DT_CLEAR_DATA;
import Model.DT_RAW_DATA;


public class Dao {

	DBConnection dbConnection;
	Connection conn;
	ResultSet rs;
	PreparedStatement ps;
	
	public static int PAGESIZE = Integer.parseInt(Config.getValue("PAGESIZE"));
	
	public Connection getConnection(){
		dbConnection = new DBConnection();
		
		Connection connection = dbConnection.getConnect();
	
		return connection;
	}
	
	public int getStart(String tablename){
		
		conn = getConnection();
		try {
			Statement stmt = conn.createStatement();
			String sql = "select max(rawid) from " + tablename;
			rs = stmt.executeQuery(sql);
			while (rs.next()){
				if (rs.getString(1) == null)
					return 0;
				else
					return Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
		return 0;
	}
	
	public List getRAWDATA(int offset, int page){
		List dataResult = new ArrayList();
		conn = getConnection();
//		ArrayList<DT_RAW_DATA> result = new ArrayList<DT_RAW_DATA>();
		Statement stmt;
		try {
			stmt = conn.createStatement();
			int start = page*PAGESIZE + offset;
//			int end = (page+1)*PAGESIZE;
			String sql = "select * from DT_RAW_DATA where id > "+ start +" order by id limit " + PAGESIZE;
			rs = stmt.executeQuery(sql);
//			while (rs.next()){
//				DT_RAW_DATA temp = new DT_RAW_DATA(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), 
//						rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), 
//						rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));
//				result.add(temp);
//			}
			dataResult.add(rs);
			dataResult.add(conn);
		} catch (Exception e) {
			try {
				System.out.println("id " + rs.getInt(1));
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally{
		//dbConnection.connectClose(conn, ps, rs);
		}
		
		return dataResult;
	}
	
	public void saveCLEARDATA(ArrayList<DT_CLEAR_DATA> result){
		
		conn = getConnection();
		
		String sql = "insert into DT_CLEAR_DATA values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			ps = conn.prepareStatement(sql);
			for (DT_CLEAR_DATA data : result){
				ps.setString(1, data.getID());
				ps.setInt(2, data.getRawID());
				ps.setString(3, data.getListID());
				ps.setString(4, data.getCategory());
				ps.setString(5, data.getSource());
				ps.setString(6, data.getSiteName());
				ps.setString(7, data.getChannel());
				ps.setString(8, data.getUrlName());
				ps.setString(9, data.getUrl());
				ps.setString(10, data.getTitle());
				ps.setString(11, data.getHerfText());
				ps.setString(12, data.getText());
				ps.setString(13, data.getHtmlText());
				ps.setString(14, data.getTextCode());
				ps.setString(15, data.getInputTime());
				ps.setString(16, data.getPubDate());
				ps.setString(17, data.getBatchNo());
				
				ps.addBatch();
			}
			
			ps.executeBatch();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			dbConnection.connectClose(conn, ps, rs);
		}
	}
	
	public int getAndSetBatchNo() {
		int bacthno = 0;
		int newbatchno = 0;
		conn = getConnection();
		try {

			String sql = "select paravalue from DT_MANAGE where parano = 'ClearBatchNo' order by paravalue asc" ;
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()){
				bacthno = Integer.parseInt(rs.getString(1));
			}
			newbatchno = bacthno + 1;
			sql = "update DT_MANAGE set paravalue = " + newbatchno
					+ " where parano = 'ClearBatchNo'";
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbConnection.connectClose(conn, ps, rs);
		}
		return newbatchno;
	}

	
//	public static void main(String[] args) {
//		System.out.print(new Dao().getStart("DT_CLEAR_DATA"));
//	}
	
//	public void saveCLEARDATA(DT_CLEAR_DATA result){
//		Connection conn = getConnection();
//		String sql;
//		ResultSet set;
//		Statement stmt;
//		try {
//			FileWriter fw = new FileWriter("log.txt",true);
//			stmt = conn.createStatement();
////			sql = "select titleCode from DT_CLEAR_DATA where titleCode = " + result.getTitleCode();
////			set = stmt.executeQuery(sql);
////			if (set.next()){
////				System.out.println("---标题重复：" + result.getTitleCode() + " " + result.getTitle());
////				//fw.write("---标题重复：" + result.getTitleCode() + " " + result.getTitle() + "\r\n");
////				fw.close();
////				return;
////			}
//			
//			sql = "select ListID, context from DT_CLEAR_DATA where contextCode = " + result.getContextCode();
//			set = stmt.executeQuery(sql);
//			if (set.next()){
////				System.out.println("---内容重复：" + result.getContextCode() + " " + result.getContext());
//				fw.write("---内容重复:" + result.getListID() + " " + result.getContext()+ "\r\n");
//				fw.write("            " + set.getInt(1) + " " + set.getString(2) + "\r\n");
//				fw.close();
//				return;
//			}
//			fw.close();
//			sql = "insert into DT_CLEAR_DATA (rawID, listID, mediaName, channelName, url, handIn, " +
//					"mediaType, title, context, contextHtml, titleCode, contextCode) " +
//					"values (?,?,?,?,?,?,?,?,?,?,?,?)";
//			
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setInt(1, result.getRawID());
//			ps.setInt(2, result.getListID());
//			ps.setString(3, result.getMediaName());
//			ps.setString(4, result.getChannelName());
//			ps.setString(5, result.getUrl());
//			ps.setString(6, result.getHandIn());
//			ps.setString(7, result.getMediaType());
//			ps.setString(8, result.getTitle());
//			ps.setString(9, result.getContext());
//			ps.setString(10, result.getContextHTML());
//			ps.setInt(11, result.getTitleCode());
//			ps.setInt(12, result.getContextCode());
//			ps.addBatch();
//			ps.executeBatch();
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
	
//	public Connection getConnection() {
//		Connection conn = null;
//		try {
//			Class.forName("oracle.jdbc.driver.OracleDriver");
//			conn = DriverManager.getConnection("jdbc:oracle:thin:@114.212.80.14:1521:ORCL", "scott", "tiger");
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		return conn;
//	}

//	
//	public ArrayList<String> getHtml(int offset, int page) {
//		Connection conn = getConnection();
//		ArrayList<String> result = new ArrayList<String>();
//
//		try {
//			Statement stmt = conn.createStatement();
//			int start = page*1000 +1;
//			int end = (page+1)*1000;
////			String sql1 = "select * from DT_RAW_DATA where id > "+ offset +" order by id limit " + start + ",1000";
//			String sql = "SELECT * FROM ( SELECT A.*, ROWNUM RN FROM (SELECT * FROM DT_RAW_DATA where id > " + offset + " ) A WHERE ROWNUM <= "+end+" ) WHERE RN >= " + start;
//			ResultSet rs = stmt.executeQuery(sql);
//			while (rs.next()) {
//				String temp = "";
//				
//				temp = temp + rs.getString(1) + "|&|" 
//						+ rs.getString(2) + "|&|" 
//						+ rs.getString(3) + "|&|" 
//						+ rs.getString(4) + "|&|" 
//						+ rs.getString(5) + "|&|" 
//						+ rs.getString(6) + "|&|" 
//						+ rs.getString(7) + "|&|" 
//						+ rs.getString(8) + "|&|" 
//						+ rs.getString(9) + "|&|" 
//						+ rs.getString(10) + "|&|" 
//						+ rs.getString(11) + "|&|" 
//						+ rs.getString(12);
//				//System.out.println(temp);
//				result.add(temp);
//			}
//			conn.close();
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//		
//		return result;
//	}
//	
//	public void SaveData2(ArrayList<String> result, String tablename,
//			String fieldTerminator) {
//		Connection conn=getConnection();
//		String sql = "insert into DT_CLEAR_DATA (RAWID,LISTID,CATEGORY,SITENAME,CHANNEL,URL,TITLE,HERFTEXT,TEXT,INPUTTIME,URLNAME,SCORE)"
//				+" values (?,?,?,?,?,?,?,?,?,?,?,?)";
//		try {
//			PreparedStatement ps=conn.prepareStatement(sql);
//			for (String s : result){
//				//System.out.println(s);
//				String[] temp = s.split(fieldTerminator);
//				if (temp.length != 12){
//					System.out.println("数据格式错误");
//					return;
//				}
//				ps.setInt(1, Integer.parseInt(temp[0]));
//				ps.setInt(2, Integer.parseInt(temp[1]));
//				ps.setString(3,temp[2]);
//				ps.setString(4,temp[3]);
//				ps.setString(5,temp[4]);
//				ps.setString(6,temp[5]);
//				ps.setString(7,temp[6]);
//				ps.setString(8,temp[7]);
//				ps.setObject(9, temp[8]);
//				ps.setString(10,temp[9]);
//				ps.setString(11,temp[10]);
//				if (temp[11].equals("null"))
//					ps.setString(12,"");
//				else
//					ps.setString(12,temp[11]);
//				ps.addBatch();
//			}
//			ps.executeBatch();
//			conn.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
