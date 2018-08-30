package com.Splunk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SplunkProcessing implements Runnable {

	String path;
	Thread t;

	private static Connection conn;
	private static CallableStatement callableStatement;

	static {
		try {
			conn = getConnection();
			callableStatement = conn.prepareCall("{call PROCESS_SPLUNK_USERS(?)}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SplunkProcessing(String threadName) {
		path = threadName;
		t = new Thread(this, path);
		System.out.println("New Thread : " + t);
		t.start();
	}

	public void run() {
		try {
			insertUserIdFromExcel(path);
			// insertUserIdFromExcel("C:\\\\Users\\\\dhisinha\\\\Desktop\\\\SPLUNK\\\\3.xlsx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (callableStatement != null) {
					callableStatement.close();
					callableStatement = null;
					System.out.println("cstmt closed");
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
					conn = null;
					System.out.println("Connection closed in finally block");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		try {
			insertUserIdFromExcel("C:\\\\Users\\\\dhisinha\\\\Desktop\\\\SPLUNK\\\\3.xlsx");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (callableStatement != null) {
					callableStatement.close();
					callableStatement = null;
					System.out.println("cstmt closed");
				}
				if (conn != null && !conn.isClosed()) {
					conn.close();
					conn = null;
					System.out.println("Connection closed in finally block");
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void insertUserIdFromExcel(String excelFilePath) throws Exception {
		Row row;
		String uniqueIdString, uniqueId;
		int c = 0;
		FileInputStream inputStream = new FileInputStream(new File(excelFilePath));
		Workbook workbook = new XSSFWorkbook(inputStream);
		Sheet firstSheet = workbook.getSheetAt(0);

		if (firstSheet.getPhysicalNumberOfRows() > 0) {
			System.out.println("No of rows to insert : " + firstSheet.getPhysicalNumberOfRows());
			while (c < firstSheet.getPhysicalNumberOfRows()) {
				row = firstSheet.getRow(c);
				uniqueIdString = row.getCell(0).toString();
				uniqueId = uniqueIdString.substring(uniqueIdString.lastIndexOf("---") + 3);
				System.out.println("Unique Id from Excel :" + uniqueId);
				if (!uniqueId.isEmpty()) {
					processDataToDB(uniqueId);
					row.createCell(0).setCellValue(uniqueId);
					row.createCell(1).setCellValue("Persisted");
				}
				c++;
			}
			System.out.println(c + "Rows completed");
		}

		FileOutputStream os = new FileOutputStream(excelFilePath);
		workbook.write(os);
		// Close workbook, OutputStream and Excel file to prevent leak
		os.close();
		workbook.close();
		inputStream.close();
	}

	private static void processDataToDB(String uniqueId) {
		try {
			callableStatement.setInt(1, Integer.parseInt(uniqueId));
			callableStatement.execute();
			callableStatement.clearParameters();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws Exception {
		String userName = null; // the user name to log in as
		String password = null; // the password to log in with
		String connectionString = null; // the URL of the intended database
		Connection connection = null;
		userName = "swc";
		password = "Qp2P2z7O";
		connectionString = "jdbc:oracle:thin:@dbc-plt-3021-vip.cisco.com:1528:ESMSTG1";
		System.out.println("1..........");
		Properties properties = new Properties();
		properties.put("user", userName);
		properties.put("password", password);
		System.out.println("2..........");
		Class.forName("oracle.jdbc.driver.OracleDriver");
		connection = DriverManager.getConnection(connectionString, properties);
		System.out.println("...Connection Established.......");
		return connection;
	}

}
