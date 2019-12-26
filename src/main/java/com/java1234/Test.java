package com.java1234;

import java.util.ArrayList;
import java.util.List;




public class Test {
	public static boolean regex(String str){
   	 String regex="^[\u4e00-\u9fa5_a-zA-Z0-9]{4,}$";
		 String[] data={"江苏","企业","公司","集团","中心","有限","控股","经营部","股份","置业","医院","门诊","银行","分行","支行","事务所","酒店","研究","学校",
			          "大学","学院","宾馆","中学","小学","幼儿园","电视","电台","政府","指挥部","办公室","派出所","办事处","公安局","海关","税务","管理","服务",
			          "合作社","社区","委员会","委会","广场","协会","基金会","直属","报社","禅寺","房产","地产","科技","纺织","化工","新区","商行","单位","市","中国",
			          "县","街道","镇","乡","村"};
		 if(str.matches(regex)) {
			 System.out.println("sss");
				for(int i=0;i<data.length;i++){
					if(str.matches("/[厂]$/")||str.matches("/[院|局|处|站|所|部|店|广场]$/")||str.indexOf(data[i])>-1){
						return true;
					}
				}
			}
		return false;
    }
	

	
	public static void main(String[] args) {
		
//		System.out.println(regex("上海有限"));
		String coms="上海明冠装饰有限公司%上海超祥汽车销售服务有限公司%上海福五实业有限公司%陕西新源石化设备科技发展有限公司%";
		test2(coms);
		/*String str="abc";
		if(str.contains("a")){
			System.out.println("xxx");
		}*/
//		test3();
	}
	
	
	
//	/杨华刚:per%.%杨华刚:per%.%上海寿高投资管理中心(有限合伙):com%.%常丽阳:per%.%杨华刚:per%.%
	public static void test1(){
		String names="杨华刚:per%.%杨华刚:per%.%上海寿高投资管理中心(有限合伙):shareholders%.%常丽阳:per%.%杨华刚:per%.%";
		String[] pcnames = names.split("%.%");
		StringBuilder cnames=new StringBuilder();
		for(int i=0;i<pcnames.length;i++){
			cnames.append(pcnames[i]);
			if(pcnames[i].indexOf("shareholders")>-1){
				
			}
		}
	}
	
	public static void test2(String coms){
		List<String> relateComVos=new ArrayList<String>();
		relateComVos.add("上海明冠装饰有限公司");
		relateComVos.add("xxxx");
		relateComVos.add("上海福五实业有限公司");
		String[] companys = coms.split("%");
		 for(int i=0;i<companys.length;i++){
			 for(int j=0;j<relateComVos.size();j++){
					 if(relateComVos.get(j).equals(companys[i])){
						 relateComVos.remove(j);
						 j--;
					 }
			 }
		 }
		 System.out.println(relateComVos);
	}
	
	public static void test3(){
 			 String[] combineComs="1度,商丘市梁园区万达建材经营部%1度,上海儒辉餐饮管理有限公司%1度,上海儒辉投资管理有限公司%2度,上海市杨浦区艾肤蕊服装店%2度,%".split("%");
 			 String[] strings = combineComs[4].split(",");
 			 System.out.println(combineComs[4].split(","));
	}
}
