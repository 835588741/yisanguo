package com.yisanguo.app.api;

public interface API {

	// http://wlgac420108.jsp.jspee.org/requestAction/login.action?&account=15984168872&password=CC79F987A74A7EB42A01185086FD212BC76C7D26
	// http://192.168.245.1:8080/springMVC/requestAction/battle.action?&table=post&condition=null&type=1&page=1&pagesize=2
	//.http://wlgac420108.jsp.jspee.org/requestAction/sendmessage.action?&account=835588741&password=xsy341341
	//"192.168.30.136:8080/springMVC/"
	// http://192.168.30.136:8080/springMVC/requestGroup/getgrouplist.action?
	//http://112.124.109.206:8080/springMVC/requestAction/ranking.action?&order=grade
	// 112.124.109.206   192.168.30.136
	/** HOST 主机**/
	String Host = "112.124.109.206:8080/springMVC";//"wlgac420108.jsp.jspee.org";  // ||localhost:8080
	String TEAM_REQUEST = "http://"+API.Host+"/organizeteamAction/"; 
	String FORM_REQUEST = "http://"+API.Host+"/formAction/";
	String MESSAGE_REQUEST = "http://"+API.Host+"/messageAction/";
	String GROUP_REQUEST = "http://"+API.Host+"/requestGroup/";
	/** 登录注册*/
	String LOGIN_OR_REGISTERT = "http://"+API.Host+"/loginAction/";
	/** 交易系统*/
	String TRANSACTION = "http://"+API.Host+"/requestTransaction/";
	String URL = "http://"+API.Host+"/requestAction/";
	String URL_RANK = "http://"+API.Host+"/rankAction/";
	/** 物品*/
	String URL2 = "http://"+API.Host+"/requestThings/";
	/** 监控*/
	String URL3 = "http://"+API.Host+"/requestMonitorAction/";
	/** 战场*/
	String URL4 = "http://"+API.Host+"/requestBattleGroup/";
	/** 其他所有请求*/
	String URL_OTHER = "http://"+API.Host+"/otherAction/";
	/** 任务请求*/
	String URL_TASK = "http://"+API.Host+"/taskAction/";
	/** 装备相关请求*/
	String URL_EQU = "http://"+API.Host+"/equipmentAction/";
	/** 注册的请求URL **/
	String URL_REGISTER = URL+"RegisterServlet";

	/** 登录的请求URL **/
	String URL_LOGIN = URL+"/login.action?";
	String DOWN_LOAD = "http://"+Host+"/sanguo.apk";
	//米雪
	//AppID：wxa48cb0786b3a7cfe
	//AppSecret：d4624c36b6795d1d99dcf0547af5443d
	
	// 微信
	String AppID = "wxa48cb0786b3a7cfe";
	String AppSecret = "d4624c36b6795d1d99dcf0547af5443d";
	
	// QQ
	String QQAPPID = "1104954694";
	String QQAPPKEY = "4Bp2b1agSswJBUQo";
	String SCOPE = "all";///"get_user_info,get_simple_userinfo,get_user_profile,get_app_friends";

	/** 蓝牙适配 UUID	**/
	String BLUE_TOOTH_API_UUID = "00001101-0000-1000-8000-00805F9B34FB";//"11223344-5566-7788-99AA-BBCCDDEEFF00";
//	"00001101-0000-1000-8000-00805F9B34FB"
	/** 登陆API	**/  
	String apiLogin = "/signin";
	
	/** 注册 API  **/
	String apiRegister = "/signup";
	

}
