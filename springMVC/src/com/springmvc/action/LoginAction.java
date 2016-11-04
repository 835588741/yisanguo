package com.springmvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.springmvc.dao.LoginDao;

@Controller
@RequestMapping("loginAction")
public class LoginAction
{
	// 公司测试环境签名
	private String sign1 = "3082030d308201f5a00302010202047a273558300d06092a864886f70d01010b05003037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3136303231363036343632305a170d3436303230383036343632305a3037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f696420446562756730820122300d06092a864886f70d01010105000382010f003082010a0282010100909d7482f97fea4eea01ff09a864508ecb5b0d3918e3568e58305216e8f4f0fb431dbfc44a5603bb7769fe18580805423928ce03df30dbbbc1f3fe432cfccb51ff021426c41b7c7afa60fc2a21bef06b4bd4bc1dfca3eceb50eef542a065ab06a1af91ba0ad7d83ed2b00ecfad463e5dca84cf88c1fd411d0b014fb2a97c14fac9f364bf52ba018936fe9036905651e49dd00286cd03a180387a62d7001cd91fdbc01d2359d5f2e1914daffa29d8b2c5e699c474cd2cfdfa607e66b28db8b0905efdeb84018ad7212ea5b9d5ac0860c2e5bc9e55308a070877aa59c3fa00c0158caa0ddc543c5cbd89791d3c1a8f9c32333f596e6cc7be1d66c4074f1c92808d0203010001a321301f301d0603551d0e04160414e1e2c11fdfbe9017156ce53a7dfb8345990b0071300d06092a864886f70d01010b050003820101007eb91fc15ad9286bb9d2922fd842ccd31f36de4193eeab503a12940f0985049ff4e5b510447acdc819330bd4cfc9b64094bbb589c8874d79c809ddd49c11d96047d2e29ea3f02ea655e28608371f3bd4323ca8e16b4933997c8f7582319a4adca590f3f3dbf8d656b74f9c29bd6bdfdaeccbd1fcb6a8e60fd2b8fea48610dc67be63227f999e86f633a3254260fe49b6a0d897aaadf4f9ec70d99a24d6fabe303221556d3da8d14ad1aa0496e187d89fa01cd90c46ab1894a69346fa2ccbbae58bf75de9d3e7bdc3d056caa7ca855888ad2ab7712e8a08b9dabbe5e09a3f6ff6cc928bd5ce5ec93f1ebe3331919a3c53fc6b566f4fd06a93e31c09617a2ffed2";
	// 私人签名工具签名
	private String sign2 = "308201b930820122a003020102020452b50109300d06092a864886f70d010105050030213111300f060355040713087368616e67686169310c300a060355040313036d6974301e170d3133313232313032343633335a170d3338313231353032343633335a30213111300f060355040713087368616e67686169310c300a060355040313036d697430819f300d06092a864886f70d010101050003818d0030818902818100aa5e8c46f3210adedeb932afedebc257ff5c786e7e72f9601187a6231c736c28c4d2ce0f9953df592a7edc7893b166fb7068d8e38f10aeb348bd126eac56465e399305b27e81ed9099d57a7e58da5e28be35ccaa9ee8c0987c04e717dd4fac29cbb27b01a2c0123456982a98692ee2d96fd3f2bc94846668eb06848fb4f8a28b0203010001300d06092a864886f70d010105050003818100a62c67cc7c4142892be9772022d017bffe766c98405ad28e6587cf5ec1f2a9106aa12ae5202469001a6e85ba7ec83eca64fc29eca858c01d1713fc998c0e2c9d77e04d1f2e21ab6a92026babf2d006a078d793ad220dca2dd1aee806c21daf74523f327398be74428fe347214fb384af68ada96e273f6f9a8109af0fc37f88e4";
	// 住宅测试环境签名
	private String sign3 = "308201e53082014ea0030201020204561bd5bc300d06092a864886f70d01010505003037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f6964204465627567301e170d3135313031323135343630345a170d3435313030343135343630345a3037310b30090603550406130255533110300e060355040a1307416e64726f6964311630140603550403130d416e64726f696420446562756730819f300d06092a864886f70d010101050003818d0030818902818100b90b4fcc572387c25782ec7b1567d9d5361ebcdb8b37697b45996c71c34d09d9a98848ec135a7fb941287ebdb402fb305c19e6c7153a8a41bc5cba12776c7525d2e567309b084c89e81709279690cfb6d53cb8479c1722cb1b3fdf7f72bbc576a73cd11044a4fe76da774163cae1851414842d47c488a97b2136ada1dbe63b6f0203010001300d06092a864886f70d0101050500038181003dc7055985d38dea87e429a76c9a8f8194433937798e4dd0d65f004a7c173aed6dd20e8b0dea5952c822d7c6243def3764e2f3050ee89e34a128be60423155618af64b5ef7f1e3ccc8f93f64e4d1e8ac7067da7902c2dcdcd8cf696832ec316d8e8f6ea3c1c6d27c7616ef1b03b6ff1b4e93461f7ed3d9e9f90b921054a8ea25";
	
	@Autowired
	private LoginDao loginDao;

	// 登陆
	@RequestMapping(value = "login.action", method = RequestMethod.POST)
	@ResponseBody
	public void doLogin(HttpServletResponse response, HttpServletRequest request,String account, String password, String version) throws IOException
	{
		String sign = request.getHeader("sign");

		if(sign1.equals(sign) || sign2.equals(sign) || sign3.equals(sign))
		{
			String data = loginDao.validateUser(account, password, version).toString();
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(data);
		}
		else
		{
			JSONObject jsonObject = new JSONObject();
			try
			{
				jsonObject.put("code", 1024);
				jsonObject.put("message", "警告：当前签名有误！当前客户端存在游戏被篡改的可能，请退出！");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			response.setHeader("Content-type", "text/html;charset=UTF-8");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().println(jsonObject.toString());
		}
	}
	
	public String getRemortIP(HttpServletRequest request) {
		  if (request.getHeader("x-forwarded-for") == null) {
		   return request.getRemoteAddr();
		  }
		  return request.getHeader("x-forwarded-for");
		}
	
	public String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}

	// 注册
	@RequestMapping(value = "register.action", method = RequestMethod.GET)
	public void register(HttpServletResponse response, String account, String password, String niname, String gender) throws IOException
	{
		niname = new String(niname.getBytes("ISO-8859-1"), "utf-8");
		String json = loginDao.register(account, password, niname, gender);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 获取UUID
	@RequestMapping(value = "getuuid.action", method = RequestMethod.GET)
	public void getuuid(HttpServletResponse response, String account, String password) throws IOException
	{
		String json = loginDao.getuuid(account, password);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 获取密保状态
	@RequestMapping(value = "getmibaostate.action", method = RequestMethod.GET)
	public void getmibaostate(HttpServletResponse response,String uuid) throws IOException
	{
		String json = loginDao.getmibaostate(uuid);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 设置或修改密保  1|初次设置  2|修改
	@RequestMapping(value = "settingmibao.action", method = RequestMethod.POST)
	public void settingmibao(HttpServletResponse response,String uuid,String question, String answer, String confirmanswer) throws IOException
	{
		String json = loginDao.settingmibao(uuid, question, answer, confirmanswer);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 修改密码
	@RequestMapping(value = "modmima.action", method = RequestMethod.POST)
	public void modmima(HttpServletResponse response,String uuid,String newpwd,String oldpwd) throws IOException
	{
		String json = loginDao.modmima(uuid,newpwd,oldpwd);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}
	
	// 重置密码
	@RequestMapping(value = "resetmima.action", method = RequestMethod.POST)
	public void resetmima(HttpServletResponse response,String account,String answer) throws IOException
	{
		String json = loginDao.resetmima(account,answer);
		// 空，无此账号或密码
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(json);
	}

	public LoginDao getLoginDao()
	{
		return loginDao;
	}

	public void setLoginDao(LoginDao loginDao)
	{
		this.loginDao = loginDao;
	}
}
