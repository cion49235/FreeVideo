package link.app.byunm.Util;

public class Config {
	/** 로그 출력 여부 */
	private static boolean LogVisible = false;
	//private static boolean LogVisible = true;

	/** 로그에 함께 출력할 TAG MSG */
	private static String LogTag = "QuizzMall";

	/*************************************************************
	 * 로그 출력 여부를 구한다.
	 *
	 * @return boolean
	 *************************************************************/
	public static final boolean isLogVisible() {
		return LogVisible;
	}

	/*************************************************************
	 * 로그 출력 여부를 설정한다.
	 *
	 * @param logVisible
	 *            설정 여부 (boolean)
	 *************************************************************/
	public static final void setLogVisible(boolean logVisible) {
		LogVisible = logVisible;
	}

	/*************************************************************
	 * 로그에 출력할 태그를 구한다.
	 *
	 * @return String
	 *************************************************************/
	public static final String getLogTag() {
		return LogTag;
	}

	/*************************************************************
	 * 로그에 출력할 태그를 설정한다.
	 *
	 * @param logTag
	 *            태그
	 *************************************************************/
	public static final void setLogTag(String logTag) {
		LogTag = logTag;
	}
}
