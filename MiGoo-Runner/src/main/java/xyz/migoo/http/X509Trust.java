package xyz.migoo.http;

import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * 搬运 http://blog.csdn.net/just_young/article/details/47664315
* @author xiaomi
 */
public class X509Trust implements X509TrustManager {

    private Certificate cert = null;

    public X509Trust(File crt) {
        if (null != crt){
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(crt))) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                while (bis.available() > 0) {
                    this.cert = cf.generateCertificate(bis);
                }
            } catch (CertificateException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (cert == null ){
            return;
        }
        for (X509Certificate cert : chain) {
            if (cert.toString().equals(this.cert.toString())) {
                return;
            }
        }
        throw new CertificateException("certificate is illegal");
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        if (cert == null ){
            return new X509Certificate[] {};
        }
        return new X509Certificate[] { (X509Certificate) cert };
    }

}