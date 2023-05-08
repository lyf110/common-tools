package cn.lyf.tools.io.http.config;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * HostnameVerifier实现类
 *
 * @author lyf
 * @since 2023-04-24 10:39
 **/
public class TrustAnyHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
