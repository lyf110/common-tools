package cn.lyf.tools.io.http.config;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * X509TrustManager实现类
 *
 * @author lyf
 * @since 2023-04-24 10:39
 **/
public class CustomX509TrustManager implements X509TrustManager {
    /**
     * 里面的方法都是空的，当方法为空是默认为所有的链接都为安全，也就是所有的链接都能够访问到
     * 当然这样有一定的安全风险，可以根据实际需要写入内容
     *
     * @param x509Certificates x509Certificates
     * @param s                s
     * @throws CertificateException CertificateException
     */
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
