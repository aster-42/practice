package wheel;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * way to doGet/doPost
 * */
public class IHttpClientUtil {

    public static String doGet(String url, Map<String, String> params) {
        HttpURLConnection connection = null;
        String httpResponse;
        try {
            connection = initConnection(new URL(url), "GET", params);

            if (connection != null) {
                byte[] content = new byte[]{};
                // automatic resource management
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(content);
                }
                String charset = parseCharset(connection.getContentType());
                httpResponse = getResponse(connection, charset);
                return httpResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    public static String doPost(String url, Map<String, String> params) {
        HttpURLConnection connection = null;
        String httpResponse;
        try {
            connection = initConnection(new URL(url), "POST", params);

            if (connection != null) {
                byte[] content = {};
                // automatic resource management
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(content);
                }
                String charset = parseCharset(connection.getContentType());
                httpResponse = getResponse(connection, charset);
                return httpResponse;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // is not keep-alive
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null;
    }

    private static String getResponse(HttpURLConnection connection, String charset) throws IOException {
        InputStream errorStream = connection.getErrorStream();
        if (errorStream == null) {
            return streamToStr(connection.getInputStream(), charset);
        } else {
            String error = streamToStr(errorStream, charset);
            if (StringUtils.isNotEmpty(error)) {
                throw new IOException(error);
            } else {
                throw new IOException(connection.getResponseCode() + ":" + connection.getResponseMessage());
            }
        }
    }

    private static String streamToStr(InputStream inputStream, String charset) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));
            StringWriter writer = new StringWriter();

            char[] chars = new char[256];
            int count = 0;
            while ((count = bufferedReader.read(chars)) > 0) {
                writer.write(chars, 0, count);
            }
            return writer.toString();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }


    private static String parseCharset(String contentType) {
        String charset = "UTF-8";
        if (StringUtils.isNotEmpty(contentType)) {
            String[] params = contentType.split(";");
            for (String param : params) {
                if (param.startsWith("charset")) {
                    String[] kv = param.split("=", 2);
                    if (kv.length == 2) {
                        if (StringUtils.isNotEmpty(kv[1])) {
                            charset = kv[1].trim();
                        }
                    }
                    break;
                }
            }
        }
        return charset;
    }

    private static HttpURLConnection initConnection(URL url, String method, Map<String, String> headers) throws IOException {
        HttpURLConnection connection = null;
        if (url.getProtocol().equals("https")) {
            try {
                SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
                sslContext.init(new KeyManager[0], new TrustManager[]{new TrustAnyManager()}, new SecureRandom());

                HttpsURLConnection httpsConnection = (HttpsURLConnection) url.openConnection();
                httpsConnection.setSSLSocketFactory(sslContext.getSocketFactory());
                httpsConnection.setHostnameVerifier((hostname, session) -> true);

                connection = httpsConnection;
            } catch (NoSuchAlgorithmException | KeyManagementException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }

        connection.setRequestMethod(method);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36");
        connection.setRequestProperty("Content-Type", "text/html;charset=utf-8");

        connection.setConnectTimeout(3000);
        connection.setReadTimeout(3000);

        // headers
        if (MapUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }
        return connection;
    }

    public static void main(String[] args) {
        String test = IHttpClientUtil.doGet("https://www.github.com", new HashMap<>());
        System.out.println(test);
    }
}
