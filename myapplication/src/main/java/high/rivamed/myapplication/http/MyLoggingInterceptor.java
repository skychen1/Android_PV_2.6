package high.rivamed.myapplication.http;

import com.lzy.okgo.utils.OkLogger;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;

/**
 * 项目名称:    Android_PV_2.6.8.0
 * 创建者:      DanMing
 * 创建时间:    2019/9/18 0018 16:49
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */
public class MyLoggingInterceptor implements Interceptor {

   //一行字符最大数量
   private static final int MAX_LENGTH = 1024;

   private static final Charset                     UTF8  = Charset.forName("UTF-8");
   private final        MyLoggingInterceptor.Logger logger;
   private volatile     LoggingLevel                level = LoggingLevel.NONE;

   public MyLoggingInterceptor() {
	this(LoggingLevel.SINGLE);
   }

   public MyLoggingInterceptor(LoggingLevel level) {
	this(level, MyLoggingInterceptor.Logger.DEFAULT);
   }

   public MyLoggingInterceptor(LoggingLevel level, MyLoggingInterceptor.Logger logger) {
	this.level = level;
	if (this.level == null) {
	   this.level = LoggingLevel.SINGLE;
	}
	this.logger = logger;
   }

   /**
    * 更改log等级，见 {@link LoggingLevel}.
    *
    * @param level 新的拦截等级
    * @return {@link MyLoggingInterceptor}
    */
   public MyLoggingInterceptor setLevel(LoggingLevel level) {
	this.level = level;
	if (this.level == null) {
	   this.level = LoggingLevel.BODY;
	}

	return this;
   }

   /**
    * 返回拦截等级
    *
    * @return {@link #level}
    */
   public LoggingLevel getLevel() {
	return level;
   }

   @Override
   public Response intercept(Chain chain) throws IOException {
	return logIntercept(chain);
   }

   /**
    * 拦截日志方法.
    *
    * @param chain {@link Interceptor.Chain}
    * @return {@link Response}
    * @throws IOException
    */
   private Response logIntercept(Chain chain) throws IOException {
	Request request = chain.request();

	long startNs = System.nanoTime();
	Response response;
	try {
	   response = chain.proceed(request);
	} catch (IOException e) {
	   print(String.format("┣━━━ [HTTP FAILED] url:%s exception:%s", request.url(),
				     e.getMessage()));
	   throw e;
	}
	long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

	String httpHeaderString = getHttpHeaderString(request, response, tookMs, chain);

	switch (level) {
	   //do nothing
	   //            case NONE:
	   //                break;
	   case URL_BODY:
		printUrlBody(request, response);
		break;
	   case SINGLE:
		printSingle(request, response, httpHeaderString);
		break;
	   case STATE:
		printState(httpHeaderString);
		break;
	   case HEADERS:
		printHeaders(request, response, httpHeaderString);
		break;
	   case BODY:
		printBody(request, response, httpHeaderString);
		break;
	   case ALL:
		printAll(request, response, httpHeaderString);
		break;
	}

	return response;
   }

   /**
    * 打印 {@link LoggingLevel#URL_BODY} 类型的log
    *
    * @param request  {@link Request}
    * @param response {@link Response}
    */
   private void printUrlBody(Request request, Response response) throws IOException {
	String responseBody = getResponseBody(request, response);
	String format = String.format("%s %s %s", getHeaderSymbol(), request.url(), responseBody);
	if (format.length() > MAX_LENGTH) {
	   print(String.format("%s %s", getHeaderSymbol(), request.url(), request));
	   printLong(responseBody);
	   printEnd();
	} else {
	   print(format);
	}
   }

   /**
    * 打印内容过长文本，超过一行长度，折行显示
    *
    * @param text 要打印的文本
    */
   private void printLong(String text) {
	int length = text.length();
	if (length <= MAX_LENGTH) {
	   print(text);
	} else {
	   int lineNum = length / MAX_LENGTH;
	   if (length % MAX_LENGTH != 0) {
		lineNum++;
	   }

	   for (int i = 1; i <= lineNum; i++) {
		if (i < lineNum) {
		   print(text.substring((i - 1) * MAX_LENGTH, i * MAX_LENGTH));
		} else {
		   print(text.substring((i - 1) * MAX_LENGTH, length));
		}
	   }
	}
   }

   /**
    * 打印 {@link LoggingLevel#HEADERS} 类型的log
    *
    * @param request          {@link Request}
    * @param response         {@link Response}
    * @param httpHeaderString http头字符串
    */

   private void printHeaders(Request request, Response response, String httpHeaderString) throws
	   IOException {
	print(httpHeaderString);
	printHttpHeaders(request, response);
	printEnd();
   }

   /**
    * 通过 {@link #logger} 打印字符串 s
    *
    * @param s 要打印的字符串
    */
   private void print(String s) {
	logger.log(s);
   }

   /**
    * 打印 {@link LoggingLevel#BODY} 类型的log
    *
    * @param response         {@link Response}
    * @param httpHeaderString http头字符串
    */
   private void printBody(Request request, Response response, String httpHeaderString) throws
	   IOException {
	print(httpHeaderString);
	print("");
	printLong(getResponseBody(request, response));
	printEnd();
   }

   /**
    * 打印 {@link LoggingLevel#SINGLE} 类型的log
    *
    * @param request          {@link Request}
    * @param response         {@link Response}
    * @param httpHeaderString http头字符串
    */
   private void printSingle(Request request, Response response, String httpHeaderString) throws
	   IOException {
	String responseBody = getResponseBody(request, response);
	String format = String.format("%s %s", httpHeaderString, responseBody);
	if (format.length() > MAX_LENGTH) {
	   print(httpHeaderString);
	   printLong(responseBody);
	} else {
	   print(format);
	}
   }

   /**
    * 打印 {@link LoggingLevel#STATE} 类型的log
    *
    * @param httpHeaderString http头字符串
    */
   private void printState(String httpHeaderString) {
	print(httpHeaderString);
   }

   /**
    * 打印log尾.
    */
   private void printEnd() {
	print("┗━============  HTTP请求结束  ============");
	print("");
   }

   /**
    * 返回Http请求状态字符串，见 {@link #getHttpStateString(Request, Response, long, Chain)}
    *
    * @param request  {@link Request}
    * @param response {@link Response}
    * @param tookMs   请求花费时长
    * @param chain    {@link Chain}
    * @return 请求状态字符串.
    */
   private String getHttpHeaderString(
	   Request request, Response response, long tookMs, Chain chain) {
	return String.format("%s %s %s", getHeaderSymbol(),"\n",
				   getHttpStateString(request, response, tookMs, chain));
   }

   /**
    * 返回请求状态字符串，包括 Http method, response code, response message, protocol, 请求花费时长，url.
    *
    * @param request  {@link Request}
    * @param response {@link Response}
    * @param tookMs   请求花费时长
    * @param chain    {@link okhttp3.Interceptor.Chain}
    * @return 请求状态字符串.
    */
   private String getHttpStateString(Request request, Response response, long tookMs, Chain chain) {
	Connection connection = chain.connection();
	Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
	return String.format(Locale.getDefault(), "[%s %d %s][%s %dms] %s %s", request.method(),
				   response.code(), response.message(), protocol, tookMs, request.url(),bodyToString(request));
   }

   private String bodyToString(Request request) {
	try {
	   Request copy = request.newBuilder().build();
	   RequestBody body = copy.body();
	   if (body == null) {
		return "";
	   }
	   Buffer buffer = new Buffer();
	   body.writeTo(buffer);
	   Charset charset = getCharset(body.contentType());
	   return "\n请求体BODY："+buffer.readString(charset);
	} catch (Exception e) {
	   OkLogger.printStackTrace(e);
	   return "\n异常";
	}

   }

   private static Charset getCharset(MediaType contentType) {
	Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
	if (charset == null) {
	   charset = UTF8;
	}
	return charset;
   }

   /**
    * 返回log头符号
    *
    * @return log头符号
    */
   private String getHeaderSymbol() {
	return (level == LoggingLevel.SINGLE || level == LoggingLevel.URL_BODY ||
		  level == LoggingLevel.STATE) ? "┣━" : "┏━============  HTTP请求开始  ============";
   }

   /**
    * 打印所有请求状态
    *
    * @param request          {@link Request}
    * @param response         {@link Response}
    * @param httpHeaderString http头字符串
    */
   private void printAll(Request request, Response response, String httpHeaderString) throws
	   IOException {
	print(httpHeaderString);
	printLong(getResponseBody(request, response));
	printHttpHeaders(request, response);
	printEnd();
   }

   /**
    * 打印请求头和响应头，如果有的话.
    *
    * @param request  {@link Request}
    * @param response {@link Response}
    */
   private void printHttpHeaders(Request request, Response response) throws IOException {
	RequestBody requestBody = request.body();
	boolean hasRequestBody = requestBody != null;
	if (hasRequestBody) {
	   print("Content-Length: " + requestBody.contentLength());
	   MediaType mediaType = requestBody.contentType();
	   // Request body headers are only present when installed as a network interceptor. Force
	   // them to be included (when available) so there values are known.
	   if (mediaType != null) {
		print("Content-Type: " + mediaType);
	   }

	   Headers headers = request.headers();
	   for (int i = 0, count = headers.size(); i < count; i++) {
		String name = headers.name(i);
		// Skip headers from the request body as they are explicitly logged above.
		if (!"Content-Type".equalsIgnoreCase(name) &&
		    !"Content-Length".equalsIgnoreCase(name)) {
		   print(name + ": " + headers.value(i));
		}
	   }
	}

	Headers headers = response.headers();
	if (headers != null) {
	   for (int i = 0, count = headers.size(); i < count; i++) {
		print(headers.name(i) + ": " + headers.value(i));
	   }
	}

	if (isPlaintext(requestBody.contentType())) {
	   print( bodyToString(request));
	} else {
	   print("\tbody: maybe [binary body], omitted!");
	}
   }

   /**
    * 返回响应体字符串
    *
    * @param request  {@link Request}
    * @param response {@link Response}
    * @return 响应体字符串
    */
   private String getResponseBody(Request request, Response response) throws IOException {
	String body = "[No Response Body]";
	if (HttpHeaders.hasBody(response)) {
	   ResponseBody responseBody = response.body();
	   BufferedSource source = responseBody.source();
	   source.request(Long.MAX_VALUE); // Buffer the entire body.
	   Buffer buffer = source.buffer();

	   if (isEncoded(request.headers())) {
		body = "[Body: Encoded]";
	   } else if (!isPlaintext(buffer)) {
		String url = request.url().toString();
		if (!url.contains("?")) {
		   body = String.format("[File:%s]", url.substring(url.lastIndexOf("/") + 1));
		} else {
		   body = "[Body: Not readable]";
		}
	   } else {
		Charset charset = UTF8;
		MediaType contentType = responseBody.contentType();
		if (contentType != null) {
		   charset = contentType.charset(UTF8);
		}
		body =String.format("%s %s %s", "返回体URL："+request.url(),"\n","返回体BODY："+buffer.clone().readString(charset));
	   }
	}

	return body;
   }

   /**
    * log打印等级，默认提供了 {@link #DEFAULT}，{@link #WARN} 两种等级，差别在Android Logcat中可以体现出来
    */
   public interface Logger {

	void log(String message);
	/**
	 * A {@link MyLoggingInterceptor.Logger} defaults output appropriate for the current platform.
	 */
	MyLoggingInterceptor.Logger DEFAULT = new MyLoggingInterceptor.Logger() {
	   @Override
	   public void log(String message) {
		Platform.get().log(Platform.INFO, message, null);
	   }
	};

	/**
	 * A {@link MyLoggingInterceptor.Logger} warn level output appropriate for the current platform.
	 */
	MyLoggingInterceptor.Logger WARN = new MyLoggingInterceptor.Logger() {
	   @Override
	   public void log(String message) {
		Platform.get().log(Platform.WARN, message, null);
	   }
	};
   }

   /**
    * Returns true if the body in question probably contains human readable text. Uses a small sample
    * of code points to detect unicode control characters commonly used in binary file signatures.
    */
   private boolean isPlaintext(Buffer buffer) {
	try {
	   Buffer prefix = new Buffer();
	   long byteCount = buffer.size() < 64 ? buffer.size() : 64;
	   buffer.copyTo(prefix, 0, byteCount);
	   for (int i = 0; i < 16; i++) {
		if (prefix.exhausted()) {
		   break;
		}
		int codePoint = prefix.readUtf8CodePoint();
		if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
		   return false;
		}
	   }
	   return true;
	} catch (EOFException e) {
	   return false; // Truncated UTF-8 sequence.
	}
   }
   private static boolean isPlaintext(MediaType mediaType) {
	if (mediaType == null) return false;
	if (mediaType.type() != null &&( mediaType.type().equals("text")|| mediaType.type().equals("image"))) {
	   return true;
	}
	String subtype = mediaType.subtype();
	if (subtype != null) {
	   subtype = subtype.toLowerCase();
	   if (subtype.contains("x-www-form-urlencoded") || subtype.contains("json") || subtype.contains("xml") || subtype.contains("html")) //
		return true;
	}
	return false;
   }
   /**
    * 判断 Headers 是不是编码过的
    *
    * @param headers {@link Headers}
    * @return {@code true } 编码过的
    */
   private boolean isEncoded(Headers headers) {
	String contentEncoding = headers.get("Content-Encoding");
	return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
   }
}
