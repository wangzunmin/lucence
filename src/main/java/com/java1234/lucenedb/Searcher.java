package com.java1234.lucenedb;

import java.io.StringReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

	public static void search(String indexDir,String q)throws Exception{
		Directory dir=FSDirectory.open(Paths.get(indexDir));
		IndexReader reader=DirectoryReader.open(dir);
		IndexSearcher is=new IndexSearcher(reader);
		// Analyzer analyzer=new StandardAnalyzer(); // 标准分词器
		SmartChineseAnalyzer analyzer=new SmartChineseAnalyzer();
		QueryParser parser=new QueryParser("cname", analyzer);
		Query query=parser.parse(q);
		long start=System.currentTimeMillis();
		TopDocs hits=is.search(query, 10);
		long end=System.currentTimeMillis();
		System.out.println("匹配 "+q+" ，总共花费"+(end-start)+"毫秒"+"查询到"+hits.totalHits+"个记录");
		
		QueryScorer scorer=new QueryScorer(query);
		Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simpleHTMLFormatter=new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
		Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
		highlighter.setTextFragmenter(fragmenter);
		for(ScoreDoc scoreDoc:hits.scoreDocs){
			Document doc=is.doc(scoreDoc.doc);
			String cname=doc.get("cname");
			if(cname!=null){
				TokenStream tokenStream=analyzer.tokenStream("cname", new StringReader(cname));
				System.out.println(highlighter.getBestFragment(tokenStream, cname));
			}
		}
		reader.close();
	}
	
	public static void main(String[] args) {
		String indexDir="F:\\lucene7";
		String q="贸易";  //SELECT * FROM `sh_all_baseinfo` WHERE CNAME LIKE "%贸易%";  11s
		try {
			search(indexDir,q); //15ms 左右
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
