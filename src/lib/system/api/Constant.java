package lib.system.api;

public class Constant {
	public final static String HTTP = "http://";
	public final static String HTTPS = "https://";
	public final static String HOST = "192.168.0.45";
	private final static String URL_SPLITTER = "/";
	private final static String SERVICEURL = "PrimaryAndMiddleSchool_1_1";

	private final static String URL_API_HOST_IP = HTTP + HOST + URL_SPLITTER+ SERVICEURL + URL_SPLITTER;
	/* bug反馈 */
	public static String ExceptionLog = URL_API_HOST_IP + "APPException.ashx";
	public static String url=URL_API_HOST_IP+"EnfoService/Login_Service_V1_1.ashx?";
}
