package com.java1234.lucenedb;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Indexer {

	private Integer ids[] = { 1, 2, 3 };
	private String citys[] = { "青岛", "南京", "上海" };
	private String descs[] = { "青岛是一个美丽的城市。",
			"南京是一个有文化的城市。南京是一个文化的城市南京，简称宁，是江苏省会，地处中国东部地区，长江下游，濒江近海。全市下辖11个区，总面积6597平方公里，2013年建成区面积752.83平方公里，常住人口818.78万，其中城镇人口659.1万人。[1-4] “江南佳丽地，金陵帝王州”，南京拥有着6000多年文明史、近2600年建城史和近500年的建都史，是中国四大古都之一，有“六朝古都”、“十朝都会”之称，是中华文明的重要发祥地，历史上曾数次庇佑华夏之正朔，长期是中国南方的政治、经济、文化中心，拥有厚重的文化底蕴和丰富的历史遗存。[5-7] 南京是国家重要的科教中心，自古以来就是一座崇文重教的城市，有“天下文枢”、“东南第一学”的美誉。截至2013年，南京有高等院校75所，其中211高校8所，仅次于北京上海；国家重点实验室25所、国家重点学科169个、两院院士83人，均居中国第三。[8-10] 。",
			"上海是一个繁华的城市。" };

	private Directory dir;

	/**
	 * 获取IndexWriter实例
	 * 
	 * @return
	 * @throws Exception
	 */
	private IndexWriter getWriter() throws Exception {
		// Analyzer analyzer=new StandardAnalyzer(); // 标准分词器
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(dir, iwc);
		return writer;
	}

	/**
	 * 生成索引
	 * 
	 * @param indexDir
	 * @throws Exception
	 */
	private void index(String indexDir) throws Exception {
		dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriter writer = getWriter();
		for (int i = 0; i < ids.length; i++) {
			Document doc = new Document();
			doc.add(new IntField("id", ids[i], Field.Store.YES));
			doc.add(new StringField("city", citys[i], Field.Store.YES));
			doc.add(new TextField("desc", descs[i], Field.Store.YES));
			writer.addDocument(doc); // 添加文档
		}
		writer.close();

	}

	/**
	 * 与数据库连接生成索引
	 * 
	 * @param indexDir
	 * @throws Exception
	 */
	private void indexConDb(String indexDir) throws Exception {
		dir = FSDirectory.open(Paths.get(indexDir));
		IndexWriter writer = getWriter();
		ResultSet resultSet = getConnection();// 获取数据库数据
		while (resultSet.next()) {
			Document doc = new Document();
			doc.add(new StringField("id", resultSet.getString("id"), Field.Store.YES));
			doc.add(new TextField("cname", resultSet.getString("cname"), Field.Store.YES));
			writer.addDocument(doc);
		}
		writer.close();
	}

	/**
	 * 连接数据库
	 */
	private ResultSet getConnection() {
		// 声明Connection对象
		Connection con = null;
		// 驱动程序名
		String driver = "com.mysql.jdbc.Driver";
		// URL指向要访问的数据库名db_lucene
		String url = "jdbc:mysql://localhost:3306/db_lucene";
		// MySQL配置时的用户名
		String user = "root";
		// MySQL配置时的密码
		String password = "123456";
		ResultSet rs = null;
		// 加载驱动程序
		try {
			Class.forName(driver);
			// 1.getConnection()方法，连接MySQL数据库！！
			con = DriverManager.getConnection(url, user, password);
			// 2.创建statement类对象，用来执行SQL语句！！
			Statement statement = con.createStatement();
			// 要执行的SQL语句
			String sql = "select id,cname from sh_all_baseinfo";
			// 3.ResultSet类，用来存放获取的结果集！！
			rs = statement.executeQuery(sql);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			/*
			 * try { if(rs!=null){ rs.close(); } if(con!=null){ con.close(); } } catch
			 * (SQLException e) { e.printStackTrace(); }
			 */
		}
		return rs;
	}

	public static void main(String[] args) throws Exception {
		/* new Indexer().index("F:\\lucene6"); */
		new Indexer().indexConDb("F:\\lucene7");
		System.out.println("生成索引成功");
	}

}
