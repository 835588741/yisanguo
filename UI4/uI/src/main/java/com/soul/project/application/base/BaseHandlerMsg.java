package com.soul.project.application.base;

/**
 * 
 * @author skydog
 * 
 */
public class BaseHandlerMsg {

	/**
	 * 系统共用 0x01
	 */

	// 每个thread开始从服务器获取或提交数据, 启动线程, 连接服务器获取数据或者提交数据
	public static final int Common_Thread_Start_Code = 0x1112;

	// 服务器异常或者中断
	public static final int CommonServerFail_Code = 0x0101;
	public static final String CommonServerFail_Msg = "网络异常或者中断, 请您稍后重试";

	/**
	 * 版本更新下载 0x00
	 */
	public static final int UpdateVersion_Start_Code = 0x0001;
	// 下载成功
	public static final int UpdateVersion_Succ_Code = 0x0002;
	// 下载失败
	public static final int UpdateVersion_Fail_Code = 0x0003;

	/**
	 * SpalshActivity 0x02
	 */
	public static final int SplashStart_Code = 0x0151;
	public static final int SplashFinish_Code = 0x0152;
	public static final int SplashNetworkCheckStart_Code = 0x0200;
	public static final int SplashNetworkCheckSucc_Code = 0x0201;
	public static final int SplashNetworkCheckFail_Code = 0x0203;
	public static final int SplashUpdateCheckStart_Code = 0x0204;
	public static final int SplashUpdateCheckNeed_Code = 0x0205;
	public static final int SplashUpdateCheckUnneed_Code = 0x0206;
	public static final int SplashAttachUserInfoStart_Code = 0x0207;
	public static final int SplashAttachUserInfoFinish_Code = 0x0208;
	public static final int SplashSwitchLoginActionStart_Code = 0x0209;
	public static final int SplashSwitchLoginActionFinish_Code = 0x0210;
	public static final int SplashSDCARDCheckStart_Code = 0x0210;
	public static final int SplashSDCARDAvaliable_Code = 0x0211;
	public static final int SplashSDCARDUnavaliable_Code = 0x0212;
	public static final int SplashFirstLauncher = 0x0213;
	/**
	 * SpalshActivity LoginActivity 共用 0x03
	 */
	// 开始登录
	public static final int LoginUserLoginStart_Code = 0x0301;
	// 登录成功
	public static final int LoginUserLoginSucc_Code = 0x0302;
	// 登录失败
	public static final int LoginUserLoginFail_Code = 0x0303;
	// 成功获取用户的基本信息
	public static final int LoginAttachUserBaseDataSucc_Code = 0x0304;

	/**
	 * LoginActivity 0x04
	 */
	// 开始thread
	public static final int LoginStart_Code = 0x0401;
	// 结束thread
	public static final int LoginFinish_Code = 0x0402;
	// 登录帐号为空
	public static final int LoingUsernameNull_Code = 0x0403;
	// 密码为空
	public static final int LoginPasswordNull_Code = 0x0404;
	// 忘记密码
	public static final int ForgetPwdWait_Code = 0x0405;
	// 修改密码完成
	public static final int UpdateNewPassword_Finish_Code = 0x0406;

	/**
	 * BorrowGoodsActivity 0x05
	 */
	// 开始thread
	public static final int BorrowGoodsStart_Code = 0x0501;
	// 借出成功
	public static final int BorrowGoods_Succ_Code = 0x0502;
	// 借出失败
	public static final int BorrowGoods_Fail_Code = 0x0503;
	// 完成thread
	public static final int BorrowGoods_Finish_Code = 0x0504;
	// 教师账号空
	public static final int TeacherIdNull_Code = 0x0505;
	// 二维码为空
	public static final int QRCodeNull_Code = 0x0506;
	// 获取物资信息成功
	public static final int BorrowGoods_GetInfo_Succ_Code = 0x0507;
	// 获取物资信息失败
	public static final int BorrowGoods_GetInfo_Fail_Code = 0x0508;
	// 二维码格式不对
	public static final int QRCodeError = 0x0509;
	// 物品编号不存在
	public static final int BorrowGoods_GetInfo_No_Exist_Code = 0x0510;
	// 获取用户信息失败
	public static final int GetUserInfo_Fail_Code = 0x0511;
	// 借取物品成功
	public static final int ReturnGoods_Succ_Code = 0x0512;
	// 借取物品失败
	public static final int ReturnGoods_Fail_Code = 0x0513;
	
	public static final int ReturnGoodsStart_Code = 0x0514;
	/**
	 * NewsActivity 0x06
	 */
	// 开始获取新闻信息
	public static final int GetNewsStart_Code = 0x0601;
	// 获取信息失败
	public static final int GetNewsFail_Code = 0x0602;
	// 获取新闻更新
	public static final int GetNewsUpdate_Code = 0x0603;
	// 获取新闻信息结束
	public static final int GetNewsEnd_Code = 0x0604;
	// 第一次获取成功
	public static final int GetNewsFirstSucc_Code = 0x0605;
	// 获取成功
	public static final int GetNewsSucc_Code = 0x0606;
	/**
	 * NewsDetails 0x062
	 */
	public static final int GetNewsDetailsStart_Code = 0x0621;
	public static final int GetNewsDetailsFail_Code = 0x0622;
	public static final int GetNewsDetailsEnd_Code = 0x0623;
	public static final int GetNewsDetailsSucc_Code = 0x0624;
	/**
	 * LeaveActivity 0x065
	 */
	public static final int LeaveStart_Code = 0x0651;
	public static final int LeaveGetTeacherInfo_Code = 0x0652;
	public static final int LeaveGetTeacherInfo_Succ_Code = 0x0654;
	public static final int Leave_Finish_Code = 0x0653;
	public static final int Leave_Add_Succ_Code = 0x0655;
	public static final int Leave_Add_Fail_Code = 0x0656;
	public static final int Leave_Date_Null_Code = 0x0657;
	public static final int Leave_Reason_Null_Code = 0x0658;
	public static final int Leave_tId_Null_Code = 0x0659;
	public static final int Leave_sId_Null_Code = 0x0660;
	public static final int Leave_Get_History_Succ_Code = 0x0661;
	public static final int Leave_GeT_History_Fail_Code = 0x0662;
	/**
	 * VideoList  0x068
	 */
	public static final int Video_Start_Code = 0x0681;
	public static final int Video_Get_List_Fail_Code = 0x0682;
	public static final int Video_Get_List_Succ_Code = 0x0683;
	public static final int Video_Get_Class_Info_Fail_Code = 0x0684;
	public static final int Video_Get_Class_Info_Succ_Code = 0x0685;
	public static final int Video_Finish_Code = 0x0686;
}
