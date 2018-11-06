package model;

import java.awt.print.Book;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BooksDAO {

	public List<Book> search(Book book){
		Connection conn = null;

		String isbn = book.getIsbn();
		String name = book.getName();
		String authorName = book.getAuthorName();
		String publisherName = book.getPublisherName();
		List<Book> bookList = new ArrayList<Book>();
		String whereOrAnd = " WHERE";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/record", "root", "root");
			String sql = "select BOOK_T.ISBM,BOOK_T.BOOK_NAME, AUTHOR_T.AUTHOR_NAME,PUBLISHER_T.PUB_NAME,BOOK_T.PRICE,BOOK_T.PUB_DAY" +
					"from BOOK_T" +
					"join AUTHOR_T" +
					"on BOOK_T.AUTHOR_ID = AUTHOR_T.AUTHOR_ID" +
					"join PUBLISHER_T" +
					"on BOOK_T.PUBLISH_ID =PUBLISHER_T.PUB_ID";

			PreparedStatement pStmt = conn.prepareStatement(sql);
			if(isbn != null && isbn != "") {
				sql  += whereOrAnd + " ISBN=?";
				pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, isbn);
				whereOrAnd =" AND";
			}
			if(name != null && name != "") {
				sql  += whereOrAnd + " BOOK_NAME LIKE ?";
				pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, "%" + name + "%");
				whereOrAnd =" AND";
			}
			if(authorName != null && authorName != "") {
				sql  += whereOrAnd + " AUTHOR_NAME LIKE ?";
				pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, "%" + authorName + "%");
				whereOrAnd =" AND";
			}
			if(publisherName != null && publisherName != "") {
				sql  += whereOrAnd + " PUB_NAME LIKE ?";
				pStmt = conn.prepareStatement(sql);
				pStmt.setString(1, "%" + publisherName + "%");
				whereOrAnd =" AND";
			}

			ResultSet rs = pStmt.executeQuery();
			while(rs.next()) {
				isbn = rs.getString("ISBN");
				name = rs.getString("BOOK_NAME");
				authorName = rs.getString("AUTHOR_NAME");
				publisherName = rs.getString("PUB_NAME");
				int price = Integer.parseInt(rs.getString("PRICE"));
				String date = rs.getString("PUB_DAY");

				book = new Book(isbn, name, authorName, publisherName, price, date);
				bookList.add(book);
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} finally {
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
		}
		return bookList;
	}

}
