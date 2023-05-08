package cn.lyf.tools.io.http;

import cn.lyf.tools.core.constant.CommonConstant;
import cn.lyf.tools.io.http.config.CustomX509TrustManager;
import cn.lyf.tools.io.http.config.TrustAnyHostnameVerifier;
import cn.lyf.tools.str.StringUtil;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 访问http和https的工具类
 *
 * @author lyf
 * @since 2023-04-24 10:39
 **/
@Slf4j
public final class HttpClientUtil {
    /**
     * 获取请求的url
     *
     * @param urlStr urlStr
     * @return URLConnection对象
     */
    public static URLConnection getUrlConnection(String urlStr) {
        if (StringUtil.isEmpty(urlStr)) {
            log.error("urlStr is empty");
            return null;
        }

        if (!HttpUtil.isHttpOrHttps(urlStr)) {
            log.error("{} is not http or https url", urlStr);
            return null;
        }

        URLConnection urlConnection = null;
        if (HttpUtil.isHttpsUrl(urlStr)) {
            try {
                // 创建SSLContext
                SSLContext sslContext = SSLContext.getInstance(CommonConstant.SSL);
                TrustManager[] tm = {new CustomX509TrustManager()};
                // 初始化
                sslContext.init(null, tm, new SecureRandom());
                // 获取SSLSocketFactory对象
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                // url对象
                URL url = new URL(urlStr);
                // 打开连接
                urlConnection = url.openConnection();

                if (urlConnection instanceof HttpsURLConnection) {
                    HttpsURLConnection conn = (HttpsURLConnection) urlConnection;
                    /*
                     * 这一步的原因: 当访问HTTPS的网址。您可能已经安装了服务器证书到您的JRE的keystore
                     * 但是服务器的名称与证书实际域名不相等。这通常发生在你使用的是非标准网上签发的证书。
                     *
                     * 解决方法：让JRE相信所有的证书和对系统的域名和证书域名。
                     *
                     * 如果少了这一步会报错:java.io.IOException: HTTPS hostname wrong: should be <localhost>
                     */
                    conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
                    conn.addRequestProperty(CommonConstant.USER_AGENT, CommonConstant.USER_AGENT_VALUE);
                    // 设置一些参数
                    conn.setConnectTimeout(5 * 1000);
                    conn.setReadTimeout(30 * 1000);
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setUseCaches(false);
                    // 设置当前实例使用的SSLSoctetFactory
                    conn.setSSLSocketFactory(ssf);
                    conn.connect();
                    return conn;
                }

                log.info("HttpsURLConnection is null");
            } catch (NoSuchAlgorithmException | KeyManagementException | IOException e) {
                log.info("HttpsURLConnection get failed, {}", e.getMessage());
            }
        } else {
            try {
                URL url = new URL(urlStr);
                urlConnection = url.openConnection();
                if (urlConnection instanceof HttpURLConnection) {
                    HttpURLConnection conn = (HttpURLConnection) urlConnection;
                    // 设置超时时间为5秒
                    int timeOut = 5 * 1000;
                    conn.setConnectTimeout(timeOut);
                    // 防止屏蔽程序抓取而返回403错误
                    conn.setRequestProperty(CommonConstant.USER_AGENT, CommonConstant.USER_AGENT_VALUE);
                    conn.connect();
                    return conn;
                }
            } catch (IOException e) {
                log.error("HttpURLConnection {} is get failed", urlStr);
            }
        }
        return null;
    }

    /**
     * 获取HttpsConnection
     *
     * @param urlStr urlStr
     * @return HttpsURLConnection
     */
    public static HttpsURLConnection getHttpsUrlConnection(String urlStr) {
        return (HttpsURLConnection) getUrlConnection(urlStr);
    }

    /**
     * 获取httpConnection
     *
     * @param urlStr urlStr
     * @return HttpUrlConnection
     */
    public static HttpURLConnection getHttpUrlConnection(String urlStr) {
        return (HttpURLConnection) getUrlConnection(urlStr);
    }

    /**
     * 执行Https请求
     *
     * @param urlStr https 路径
     * @return 请求的结果
     */
    public static String request(String urlStr) {
        URLConnection conn = getUrlConnection(urlStr);

        if (conn == null) {
            log.info("HttpsURLConnection is null");
            return null;
        }

        StringBuilder sb = null;
        try (InputStream inputStream = conn.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            log.error("", e);
        }

        return sb == null ? null : sb.toString();
    }
}
