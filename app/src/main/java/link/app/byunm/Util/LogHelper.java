package link.app.byunm.Util;

import android.util.Log;

import java.util.Locale;
import java.util.MissingFormatArgumentException;

public class LogHelper {
    /**
     * 로그 출력 모드 - 디버그
     */
    private static byte DEBUG = 0x00;

    /**
     * 로그 출력 모드 - 에러
     */
    private static byte ERROR = 0x01;

    /**
     * 로그 출력 모드 - 정보
     */
    private static byte INFO = 0x02;

    /**
     * 로그 출력 모드 - 경고
     */
    private static byte WARRING = 0x03;

    /*************************************************************
     * 로그 출력
     *
     * @param type       로그 유형 (LogHelper.DEBUG, LogHelper.ERROR, LogHelper.INFO)
     * @param clsName 클래스 이름
     * @param msg       로그 메시지
     *************************************************************/
    private static void print(byte type, String clsName, String msg) {
        if (Config.isLogVisible()) {

            String logMsg = String.format(Locale.getDefault(), "[%s] %s", clsName, msg);

            if (type == DEBUG) {
                Log.d(Config.getLogTag(), logMsg);
            } else if (type == ERROR) {
                Log.e(Config.getLogTag(), logMsg);
            } else if (type == WARRING) {
                Log.w(Config.getLogTag(), logMsg);
            } else {
                Log.i(Config.getLogTag(), logMsg);
            }
        }
    }

    /*************************************************************
     * 디버그 로그 출력
     *
     * @param cls 메소드를 호출한 클래스
     * @param format  로그 메시지 포멧
     * @param args  파라미터
     *************************************************************/
    public static void debug(Object cls, String format,
                             Object... args) {
        try{
            print(DEBUG, cls.getClass().getSimpleName(),
                    String.format(Locale.getDefault(), format, args));
        }catch (MissingFormatArgumentException e){
        }catch (Exception e){
        }
    }

    /*************************************************************
     * 에러 로그 출력
     *
     * @param cls   메소드를 호출한 클래스
     * @param format 로그 메시지 포멧
     * @param args    파라미터
     *************************************************************/
    public static void error(Object cls, String format,
                             Object... args) {
        print(ERROR, cls.getClass().getSimpleName(),
                String.format(Locale.getDefault(), format, args));
    }

    /*************************************************************
     * 태그가 있는 정보 로그 출력
     *
     * @param cls   메소드를 호출한 클래스
     * @param format 로그 메시지 포멧
     * @param args    파라미터
     *************************************************************/
    public static void info(Object cls, String format,
                            Object... args) {
        print(INFO, cls.getClass().getSimpleName(),
                String.format(Locale.getDefault(), format, args));
    }

    /*************************************************************
     * 태그가 있는 경고 로그 출력
     *
     * @param cls 메소드를 호출한 클래스
     * @param format  로그 메시지 포멧
     * @param args  파라미터
     *************************************************************/
    public static void warring(Object cls, String format,
                               Object... args) {
        print(WARRING, cls.getClass().getSimpleName(),
                String.format(Locale.getDefault(), format, args));
    }
}
