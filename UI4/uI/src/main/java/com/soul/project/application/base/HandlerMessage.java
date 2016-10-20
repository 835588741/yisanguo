package com.soul.project.application.base;

/**
 * handle使用的信号量
 * @author soul
 * 命名规则:
 * activity名_信号业务含义
 */
public enum HandlerMessage {
	
	MainNotification(0x0001, "显示Notification"),
	
	/**
	 * 系统共用 0x01
	 */
	CommonSocketTimeout(0x0100, "服务器忙,请您稍后重试!"), 
	CommonUnsupportedEncoding(0x0102, "不支持的网络编码格式"), CommonClientProtocolException(0x0103, "HTTP协议错误"),
	CommonHttpResponseException(0x0104, "请求服务无返回,请您稍后重试!"), CommonUnsupportedUTFEncoding(0x0105, "不支持的UTF-8编码格式"), 
	CommonConnectionAbort(0x0106, "网络连接异常中断"), CommonJSONParseFail(0x0107, "请求服务无返回,请您稍后重试!"),
	CommonFileSaveFail(0x0108, "系统保存文件失败"), CommonFileNotFoundException(0x0109, "找不到制定文件"), 
	CommonIOException(0x0110, "文件操作失败"), CommonIllegalStateException(0x111, ""), 
	// 每个thread开始从服务器获取数据
	Common_Thread_Start(0x1112, "启动线程, 连接服务器获取数据或者提交数据"),
	// 
	CommonNetworkConnectionFail(0x0113,"网络连接失败"),
	CommonNetworkConnectionSucc(0x0114,"网络连接成功");
	
	// 信号量
	private final int msgWhat;
	// 信号含义
	private final String description;
	
	/**
	 * @return the msgWhat
	 */
	public int getMsgWhat() {
		return msgWhat;
	}
	
	/**
	 * @return the descript
	 */
	public String getDescription() {
		return description;
	}
	
	HandlerMessage(int msgWhat, String description) {
		this.msgWhat = msgWhat;
		this.description = description;
	}

}
