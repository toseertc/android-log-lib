package com.rzrtclog.sample.httplogger;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rzrtc.log.BuildConfig;
import com.rzrtc.log.DuBIns;
import com.rzrtc.log.utils.CharacterHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.Request;

public class HttpPrinter {
    private DuBIns duBLogInstance;

    /***************************
     * 以下代码用于存储http请求
     * *****************************
     */


    /**
     * @param chainMs
     * @param isSuccessful
     * @param code
     * @param headers
     * @param contentType
     * @param bodyString
     * @param segments
     * @param message
     * @param responseUrl
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss.SSS", Locale.US);

    public HttpPrinter(DuBIns duBIns) {
        this.duBLogInstance = duBIns;
    }


    public void printJsonResponse(long chainMs,
                                  boolean isSuccessful,
                                  int code,
                                  @NonNull String headers,
                                  @Nullable MediaType contentType,
                                  @Nullable String bodyString,
                                  @NonNull List<String> segments,
                                  @NonNull String message,
                                  @NonNull final String responseUrl,
                                  boolean noPrintBody) {

        final String tag = getTag(false);
        final String[] urlLine = {URL_TAG + responseUrl, N};
        autoPrint(tag + " : " + RESPONSE_UP_LINE);
        logLines(tag, urlLine, true);

        if (noPrintBody) {
            logLines(tag, getSimPleResponse(chainMs, code), true);
        } else {
            bodyString = RequestInterceptor.isJson(contentType) ? CharacterHandler.jsonFormat(bodyString)
                    : RequestInterceptor.isXml(contentType) ? CharacterHandler.xmlFormat(bodyString) : bodyString;
            final String responseBody = LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + bodyString;
            logLines(tag, getResponse(
                    headers,
                    chainMs,
                    code,
                    isSuccessful,
                    segments
                    , message
            ), true);

            logLines(tag, responseBody.split(LINE_SEPARATOR), true);
        }
        autoPrint(tag + " : " + END_LINE);
    }

    private void autoPrint(String s) {
        if (BuildConfig.DEBUG) {
            Log.i(TAG, s); //Ts直接打印的话，debug模式下会有不必要的横线
        } else {
            if (duBLogInstance != null) {
                duBLogInstance.i(s);
            }
        }
    }

//    private static ThreadLocal<Integer> last = new ThreadLocal<Integer>() {
//        @Override
//        protected Integer initialValue() {
//            return 0;
//        }
//    };
//
//    private static final String[] ARMS = new String[]{"   ", "   ", "   ", "   "};
//
//    private static String computeKey() {
//        if (last.get() >= 4) {
//            last.set(0);
//        }
//        String s = ARMS[last.get()];
//        last.set(last.get() + 1);
//        return s;
//    }

    public void printFileResponse(long chainMs,
                                  boolean isSuccessful,
                                  int code,
                                  @NonNull String headers,
                                  @NonNull List<String> segments,
                                  @NonNull String message,
                                  @NonNull final String responseUrl,
                                  boolean noPrintBody) {


        final String tag = getTag(false);
        final String[] urlLine = {URL_TAG + responseUrl, N};
        autoPrint(tag + " : " + RESPONSE_UP_LINE);
        logLines(tag, urlLine, true);

        if (noPrintBody) {
            logLines(tag, getSimPleResponse(chainMs, code), true);
        } else {
            logLines(tag, getResponse(
                    headers,
                    chainMs,
                    code,
                    isSuccessful,
                    segments
                    , message
            ), true);
        }
        autoPrint(tag + " : " + END_LINE);
    }

    public void printJsonRequest(@NonNull Request request, @NonNull String bodyString) {
        final String requestBody = LINE_SEPARATOR + BODY_TAG + LINE_SEPARATOR + bodyString;
        final String tag = getTag(true);

        autoPrint(tag + " : " + REQUEST_UP_LINE);
        logLines(tag, new String[]{URL_TAG + request.url()}, false);
        logLines(tag, getRequest(request), true);
        logLines(tag, requestBody.split(LINE_SEPARATOR), true);
        autoPrint(tag + " : " + END_LINE);
    }

    /**
     * 打印网络请求信息, 当网络请求时 {{@link okhttp3.RequestBody}} 为 {@code null} 或不可解析的情况
     *
     * @param request
     */
    public void printFileRequest(@NonNull Request request) {
        final String tag = getTag(true);

        autoPrint(tag + " : " + REQUEST_UP_LINE);
        logLines(tag, new String[]{URL_TAG + request.url()}, false);
        logLines(tag, getRequest(request), true);
        logLines(tag, OMITTED_REQUEST, true);
        autoPrint(tag + " : " + END_LINE);
    }

    private static final String TAG = "HttpPrinter";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final String DOUBLE_SEPARATOR = LINE_SEPARATOR + LINE_SEPARATOR;

    private static final String[] OMITTED_RESPONSE = {LINE_SEPARATOR, "Omitted response body"};
    private static final String[] OMITTED_REQUEST = {LINE_SEPARATOR, "Omitted request body"};

    private static final String N = "\n";
    private static final String T = "\t";
    private static final String REQUEST_UP_LINE = "┌────── Request ────────────────────────────────────────────────────────────────────────";
    private static final String END_LINE = "└───────────────────────────────────────────────────────────────────────────────────────";
    private static final String RESPONSE_UP_LINE = "┌────── Response ───────────────────────────────────────────────────────────────────────";
    private static final String BODY_TAG = "Body:";
    private static final String URL_TAG = "URL: ";
    private static final String METHOD_TAG = "Method: @";
    private static final String HEADERS_TAG = "Headers:";
    private static final String STATUS_CODE_TAG = "Status Code: ";
    private static final String RECEIVED_TAG = "Received in: ";
    private static final String CORNER_UP = "┌ ";
    private static final String CORNER_BOTTOM = "└ ";
    private static final String CENTER_LINE = "├ ";
    private static final String DEFAULT_LINE = "│ ";

    private static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || N.equals(line) || T.equals(line) || TextUtils.isEmpty(line.trim());
    }

    /**
     * 对 {@code lines} 中的信息进行逐行打印
     *
     * @param tag
     * @param lines
     * @param withLineSize 为 {@code true} 时, 每行的信息长度不会超过110, 超过则自动换行
     */
    private void logLines(String tag, String[] lines, boolean withLineSize) {
        for (String line : lines) {
            int lineLength = line.length();
            int MAX_LONG_SIZE = withLineSize ? 110 : lineLength;
            for (int i = 0; i <= lineLength / MAX_LONG_SIZE; i++) {
                int start = i * MAX_LONG_SIZE;
                int end = (i + 1) * MAX_LONG_SIZE;
                end = end > line.length() ? line.length() : end;
                autoPrint(resolveTag(tag) + " : " + DEFAULT_LINE + line.substring(start, end));
            }
        }
    }

    /**
     * 此方法是为了解决在 AndroidStudio v3.1 以上 Logcat 输出的日志无法对齐的问题
     * <p>
     * 此问题引起的原因, 据 JessYan 猜测, 可能是因为 AndroidStudio v3.1 以上将极短时间内以相同 tag 输出多次的 log 自动合并为一次输出
     * 导致本来对称的输出日志, 出现不对称的问题
     * AndroidStudio v3.1 此次对输出日志的优化, 不小心使市面上所有具有日志格式化输出功能的日志框架无法正常工作
     * 现在暂时能想到的解决方案有两个: 1. 改变每行的 tag (每行 tag 都加一个可变化的 token) 2. 延迟每行日志打印的间隔时间
     * <p>
     * {@link #resolveTag(String)} 使用第一种解决方案
     *
     * @param tag
     */
    private static String resolveTag(String tag) {
        return tag;
    }

    private static String[] getRequest(Request request) {
        String log;
        String header = request.headers().toString();
        log = METHOD_TAG + request.method() + DOUBLE_SEPARATOR +
                (isEmpty(header) ? "" : HEADERS_TAG + LINE_SEPARATOR + dotHeaders(header));
        return log.split(LINE_SEPARATOR);
    }

    private static String[] getSimPleResponse(
            long tookMs,
            int code
    ) {
        String log;
        log = RECEIVED_TAG + tookMs + "ms" + DOUBLE_SEPARATOR + STATUS_CODE_TAG + code;
        return log.split(LINE_SEPARATOR);
    }

    private static String[] getResponse(
            String header,
            long tookMs,
            int code,
            boolean isSuccessful,
            List<String> segments
            , String message
    ) {
        String log;
        String segmentString = slashSegments(segments);
        log =
                ((!TextUtils.isEmpty(segmentString) ? segmentString + " - " : "")
                        + "is success : " + isSuccessful + " - "
                        +
                        RECEIVED_TAG + tookMs + "ms"
                        + DOUBLE_SEPARATOR + STATUS_CODE_TAG +
                        code
                        + " / " + message + DOUBLE_SEPARATOR + (isEmpty(header) ? "" : HEADERS_TAG + LINE_SEPARATOR
                        + dotHeaders(header))
                )
        ;
        return log.split(LINE_SEPARATOR);
    }

    private static String slashSegments(List<String> segments) {
        StringBuilder segmentString = new StringBuilder();
        for (String segment : segments) {
            segmentString.append("/").append(segment);
        }
        return segmentString.toString();
    }

    /**
     * 对 {@code header} 按规定的格式进行处理
     *
     * @param header
     * @return
     */
    private static String dotHeaders(String header) {
        String[] headers = header.split(LINE_SEPARATOR);
        StringBuilder builder = new StringBuilder();
        String tag = "─ ";
        if (headers.length > 1) {
            for (int i = 0; i < headers.length; i++) {
                if (i == 0) {
                    tag = CORNER_UP;
                } else if (i == headers.length - 1) {
                    tag = CORNER_BOTTOM;
                } else {
                    tag = CENTER_LINE;
                }
                builder.append(tag).append(headers[i]).append("\n");
            }
        } else {
            for (String item : headers) {
                builder.append(tag).append(item).append("\n");
            }
        }
        return builder.toString();
    }

    private String getTag(boolean isRequest) {

//        String gmtTime = sdf.format(new Date(System.currentTimeMillis()));
//        String metaJson = formatMetaJson(Process.myPid(), Thread.currentThread().getId(), Looper.getMainLooper().getThread().getId(), "version | message", BuildConfig.LIBRARY_PACKAGE_NAME);
//
//        String jsonStr = "{\"time\":\"" + gmtTime + "\"," + "\"level\":\"" + getLevelString(Log.INFO) + "\"," +
//                "\"tag\":\"" + TAG + "\",\"meta\":" + metaJson + "}";
//        if (isRequest) {
//            return jsonStr + "-Request";
//        } else {
//            return jsonStr + "-Response";
//        }

        return "";
    }


    /**
     * 格式化键值对。
     *
     * @param pid     进程号。
     * @param tid     线程号。
     * @param mainTid 主线程号。
     * @param keys    所有key 值，以'|' 分割。
     * @param values  不定参数，value 值，可以为空。
     * @return 当key长度与value长度不匹配时，返回',"key1|key2|...":"values1|valuse2|..."'
     * 当匹配时，返回',"key1":"value1","key2":"value2"...'
     * 异常时，返回空字符串
     */
    public static String formatMetaJson(int pid, long tid, long mainTid, String keys, Object... values) {
        String[] keyArray = keys != null ? keys.split("\\|") : new String[0];
        String jsonStr = "";
        if (keyArray.length != values.length) {
            StringBuilder valueStr = new StringBuilder();
            for (Object obj : values) {
                valueStr.append(obj != null ? obj.toString() : "null");
                valueStr.append("|");
            }
            if (values.length > 0) {
                valueStr.deleteCharAt(valueStr.length() - 1);
                jsonStr = "{\"ptid\":\"" + pid + "-" + tid + "-" + mainTid + "\",\"" + keys + "\":\"" + valueStr + "\"}";
            }
        } else {
            try {
                JSONObject json = new JSONObject();
                for (int i = 0; i < keyArray.length; i++) {
                    json.put(keyArray[i], values[i] != null ? values[i] : "null");
                }
                String metaStr = json.toString().replace("\\/", "/");
                jsonStr = "{\"ptid\":\"" + pid + "-" + tid + (tid == mainTid ? "*" : "") + "\""
                        + (keyArray.length > 0 ? "," : "") + metaStr.substring(1, metaStr.length());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonStr;
    }

}
