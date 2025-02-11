package casia.isi.component.http;

import casia.isi.component.http.proxy.*;
import com.frameworkset.util.SimpleStringUtil;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.io.EmptyInputStream;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author yinbp
 * @Date:2016-11-20 11:39:59
 */
public class HttpRequestProxy {

    private static Logger logger = LoggerFactory.getLogger(HttpRequestProxy.class);
    public static void startHttpPools(String configFile){
        HttpRequestUtil.startHttpPools(configFile);
    }
    public static void startHttpPools(Map<String, Object> configs){
        HttpRequestUtil.startHttpPools(configs);
    }






    public static String httpGetforString(String url) throws HttpProxyRequestException {
        return httpGetforString(url, (String) null, (String) null, (Map<String, String>) null);
    }
    public static <T> T httpGetforObject(String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString("default",url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<T>() {

            @Override
            public T handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleResponse(  response, resultType);
            }

        });
    }
    public static String httpGetforString(String poolname, String url) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, (Map<String, String>) null);
    }

    public static <T> T httpGetforObject(String poolname, String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<T>() {

            @Override
            public T handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleResponse(  response, resultType);
            }

        });
    }

    public static <T> List<T> httpGetforList(String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString("default",url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<List<T>>() {

            @Override
            public List<T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(  response, resultType);
            }

        });
    }

    public static <K,T> Map<K,T> httpGetforMap(String url, final Class<K> keyType, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString("default",url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<Map<K,T>>() {

            @Override
            public Map<K,T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleMapResponse(  response,keyType, resultType);
            }

        });
    }

    public static <T> Set<T> httpGetforSet(String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString("default",url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<Set<T>>() {

            @Override
            public Set<T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleSetResponse(  response, resultType);
            }

        });
    }

    public static <T> List<T> httpGetforList(String poolName, String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString(  poolName,url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<List<T>>() {

            @Override
            public List<T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(  response, resultType);
            }

        });
    }

    public static <K,T> Map<K,T> httpGetforMap(String poolName, String url, final Class<K> keyType, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString(  poolName,url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<Map<K,T>>() {

            @Override
            public Map<K,T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleMapResponse(  response,keyType, resultType);
            }

        });
    }

    public static <T> Set<T> httpGetforSet(String poolName, String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpGetforString(  poolName,url, (String) null, (String) null, (Map<String, String>) null, new ResponseHandler<Set<T>>() {

            @Override
            public Set<T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleSetResponse(  response, resultType);
            }

        });
    }
    public static <T> T httpGet(String poolname, String url, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, (Map<String, String>) null,responseHandler);
    }

    public static <T> T httpGet(String poolname, String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, headers,responseHandler);
    }

    public static <T> T httpGet(String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString("default", url, (String) null, (String) null, headers,responseHandler);
    }

    public static String httpGetforString(String url, Map<String, String> headers) throws HttpProxyRequestException {
        return httpGetforString(url, (String) null, (String) null, headers);
    }

    public static String httpGetforString(String poolname, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, headers);
    }

    public static <T> T httpGetforString(String poolname, String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString(poolname, url, (String) null, (String) null, headers,responseHandler);
    }

    public static <T> T httpGetforString(String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpGetforString("default",  url, (String) null, (String) null, headers,responseHandler);
    }

    public static String httpGetforString(String url, String cookie, String userAgent, Map<String, String> headers) throws HttpProxyRequestException {
        return httpGetforString("default", url, cookie, userAgent, headers);
    }
    public static String httpGetforString(String poolname, String url, String cookie, String userAgent, Map<String, String> headers) throws HttpProxyRequestException {
        return  httpGetforString(poolname, url, cookie, userAgent, headers,new StringResponseHandler()) ;
    }
    /**
     * get请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpGetforString(String poolname, String url, String cookie, String userAgent, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
       return httpGet(  poolname,   url,   cookie,   userAgent,   headers, responseHandler);
    }
    private static Exception getException(ResponseHandler responseHandler, HttpServiceHosts httpServiceHosts ){
//        assertCheck(  httpServiceHosts );
        ExceptionWare exceptionWare = httpServiceHosts.getExceptionWare();
        if(exceptionWare != null) {
            return exceptionWare.getExceptionFromResponse(responseHandler);
        }
        return null;
    }
    /**
     * get请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpGet(String poolname, String url, String cookie, String userAgent, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // String cookie = getCookie();
        // String userAgent = getUserAgent();

        HttpClient httpClient = null;
        HttpGet httpGet = null;

        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
            do {

                try {

                    httpAddress = httpServiceHosts.getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Get call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpGet = HttpRequestUtil.getHttpGet(config, url, cookie, userAgent, headers);
                    responseBody = httpClient.execute(httpGet, responseHandler);
                    e = getException(  responseHandler,httpServiceHosts );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpGet != null)
                        httpGet.releaseConnection();
                    httpClient = null;
                }
            } while (true);
        }
        else{
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("Get call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpGet = HttpRequestUtil.getHttpGet(config, url, cookie, userAgent, headers);
                responseBody = httpClient.execute(httpGet, responseHandler);

            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpGet != null)
                    httpGet.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }

    /**
     * head请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpHead(  poolname,   url,   null, null, (Map<String, String>) null,responseHandler);

    }

    /**
     * get请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String url, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpHead(  "default",   url,   null, null, (Map<String, String>) null,responseHandler);

    }

    /**
     * head请求URL
     *
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpHead(  poolname,   url,   null, null,params, (Map<String, String>) headers,responseHandler);

    }

    /**
     * get请求URL
     * ,Map<String, Object> params,Map<String, String> headers,
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url, String cookie, String userAgent, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
       return httpHead(  poolname,   url,   cookie,   userAgent,(Map<String, Object>)null, headers,responseHandler);

    }

    /**
     * get请求URL
     * ,Map<String, Object> params,Map<String, String> headers,
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T httpHead(String poolname, String url, String cookie, String userAgent, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // String cookie = getCookie();
        // String userAgent = getUserAgent();

        HttpClient httpClient = null;
        HttpHead httpHead = null;

        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
            do {

                try {

                    httpAddress = httpServiceHosts.getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Head call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpHead = HttpRequestUtil.getHttpHead(config, url, cookie, userAgent, headers);
                    HttpParams httpParams = null;
                    if (params != null && params.size() > 0) {
                        httpParams = new BasicHttpParams();
                        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                        NameValuePair paramPair_ = null;
                        for (int i = 0; it.hasNext(); i++) {
                            Entry<String, Object> entry = it.next();
                            httpParams.setParameter(entry.getKey(), entry.getValue());
                        }
                        httpHead.setParams(httpParams);
                    }
                    responseBody = httpClient.execute(httpHead, responseHandler);
                    e = getException(  responseHandler,httpServiceHosts );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpHead != null)
                        httpHead.releaseConnection();
                    httpClient = null;
                }
            } while (true);
        }
        else{
            try {

                if(logger.isTraceEnabled()){
                    logger.trace("Head call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpHead = HttpRequestUtil.getHttpHead(config, url, cookie, userAgent, headers);
                HttpParams httpParams = null;
                if (params != null && params.size() > 0) {
                    httpParams = new BasicHttpParams();
                    Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                    NameValuePair paramPair_ = null;
                    for (int i = 0; it.hasNext(); i++) {
                        Entry<String, Object> entry = it.next();
                        httpParams.setParameter(entry.getKey(), entry.getValue());
                    }
                    httpHead.setParams(httpParams);
                }
                responseBody = httpClient.execute(httpHead, responseHandler);

            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpHead != null)
                    httpHead.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }


    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param files
     * @throws HttpProxyRequestException
     */
    public static String httpPostFileforString(String url, Map<String, Object> params, Map<String, File> files)
            throws HttpProxyRequestException {
        return httpPostFileforString("default", url, (String) null, (String) null, params, files);
    }

    public static String httpPostFileforString(String poolname, String url, Map<String, Object> params, Map<String, File> files)
            throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, (String) null, (String) null, params, files);
    }

    public static String httpPostforString(String url, Map<String, Object> params) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null);
    }

    public static <T> T httpPost(String url, Map<String, Object> params, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null, responseHandler);
    }

    public static <T> T httpPostForObject(String url, Map<String, Object> params, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null, new ResponseHandler<T>() {
            @Override
            public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleResponse(response,resultType);
            }
        });
    }

    public static <T> List<T> httpPostForList(String url, Map<String, Object> params, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null, new ResponseHandler<List<T>>() {
            @Override
            public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(response,resultType);
            }
        });
    }
    public static <T> Set<T> httpPostForSet(String url, Map<String, Object> params, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null, new ResponseHandler<Set<T>>() {
            @Override
            public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleSetResponse(response,resultType);
            }
        });
    }

    public static <K,T> Map<K,T> httpPostForMap(String url, Map<String, Object> params, final Class<K> keyType, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(url, params, (Map<String, String>) null, new ResponseHandler<Map<K,T>>() {
            @Override
            public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
            }
        });
    }

    public static <T> T httpPostForObject(String poolName, String url, Map<String, Object> params, final Class<T> resultType) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();

		httpOption.params = params;

		return httpPost(  poolName,   url,  httpOption, new ResponseHandler<T>() {
			@Override
			public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return HttpRequestProxy.handleResponse(response,resultType);
			}
		});
//        return httpPostforString(  poolName,url, params, (Map<String, String>) null);
    }

	public static <T> T httpPostForObject(String poolName, String url, Map<String, Object> params, final Class<T> resultType, DataSerialType dataSerialType) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();

		httpOption.params = params;
		httpOption.dataSerialType = dataSerialType;

		return httpPost(  poolName,   url,  httpOption, new ResponseHandler<T>() {
			@Override
			public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return HttpRequestProxy.handleResponse(response,resultType);
			}
		});
//        return httpPostforString(  poolName,url, params, (Map<String, String>) null);
	}

	public static <T> List<T> httpPostForList(String poolName, String url, Map<String, Object> params, final Class<T> resultType, DataSerialType dataSerialType) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();

		httpOption.params = params;
		httpOption.dataSerialType = dataSerialType;

		return httpPost(  poolName,   url,  httpOption, new ResponseHandler<List<T>>() {
			@Override
			public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return HttpRequestProxy.handleListResponse(response,resultType);
			}
		});

	}
	public static <T> Set<T> httpPostForSet(String poolName, String url, Map<String, Object> params, final Class<T> resultType, DataSerialType dataSerialType) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();

		httpOption.params = params;
		httpOption.dataSerialType = dataSerialType;

		return httpPost(  poolName,   url,  httpOption, new ResponseHandler<Set<T>>() {
			@Override
			public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return HttpRequestProxy.handleSetResponse(response,resultType);
			}
		});
//		return httpPostforString(  poolName,url, params, (Map<String, String>) null, new ResponseHandler<Set<T>>() {
//			@Override
//			public Set<T>  handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//				return HttpRequestProxy.handleSetResponse(response,resultType);
//			}
//		});
	}

	public static <K,T> Map<K,T> httpPostForMap(String poolName, String url, Map<String, Object> params, final Class<K> keyType, final Class<T> resultType, DataSerialType dataSerialType) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();

		httpOption.params = params;
		httpOption.dataSerialType = dataSerialType;

		return httpPost(  poolName,   url,  httpOption,new ResponseHandler<Map<K,T>>() {
			@Override
			public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
				return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
			}
		});
//    	return httpPostforString(poolName,url, params, (Map<String, String>) null, new ResponseHandler<Map<K,T>>() {
//			@Override
//			public Map<K,T>  handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//				return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
//			}
//		});
	}

    public static <T> List<T> httpPostForList(String poolName, String url, Map<String, Object> params, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(  poolName,url, params, (Map<String, String>) null, new ResponseHandler<List<T>>() {
            @Override
            public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(response,resultType);
            }
        });
    }
    public static <T> List<T> httpPostForList(String poolName, String url , final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(  poolName,url, (Map<String, Object>) null, (Map<String, String>) null, new ResponseHandler<List<T>>() {
            @Override
            public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(response,resultType);
            }
        });
    }
    public static <T> List<T> httpPostForList(String url , final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(  (String)null,url, (Map<String, Object>) null, (Map<String, String>) null, new ResponseHandler<List<T>>() {
            @Override
            public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(response,resultType);
            }
        });
    }
    public static <T> Set<T> httpPostForSet(String poolName, String url, Map<String, Object> params, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(  poolName,url, params, (Map<String, String>) null, new ResponseHandler<Set<T>>() {
            @Override
            public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleSetResponse(response,resultType);
            }
        });
    }

    public static <K,T> Map<K,T> httpPostForMap(String poolName, String url, Map<String, Object> params, final Class<K> keyType, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPostforString(poolName,url, params, (Map<String, String>) null, new ResponseHandler<Map<K,T>>() {
            @Override
            public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
            }
        });
    }
    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPostforString(String url, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpPostFileforString("default", url, (String) null, (String) null, params, (Map<String, File>) null, headers);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPostforString(String poolName, String url, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpPostFileforString(poolName, url, (String) null, (String) null, params, (Map<String, File>) null, headers);
    }



    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static  <T> T  httpPost(String url, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost("default", url, (String) null, (String) null, params, (Map<String, File>) null, headers, responseHandler);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPostforString(String url, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost("default", url, (String) null, (String) null, params, (Map<String, File>) null, headers,responseHandler);
    }

    /**
     * 公用post方法
     *
     * @param url
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPostforString(String poolName, String url, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost(poolName, url, (String) null, (String) null, params, (Map<String, File>) null, headers,responseHandler);
    }

    public static String httpPostforString(String poolname, String url, Map<String, Object> params) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();

		httpOption.params = params;

		return httpPost(  poolname,   url,  httpOption,new StringResponseHandler());
//        return httpPostFileforString(poolname, url, (String) null, (String) null, params, (Map<String, File>) null);
    }

	public static String httpPostforString(String poolname, String url, Map<String, Object> params, DataSerialType dataSerialType) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();

		httpOption.params = params;
		httpOption.dataSerialType = dataSerialType;
		return httpPost(  poolname,   url,  httpOption,new StringResponseHandler());
//        return httpPostFileforString(poolname, url, (String) null, (String) null, params, (Map<String, File>) null);
	}

    public static String httpPostforString(String url) throws HttpProxyRequestException {
        return httpPostforString("default", url);
    }

    public static <T> T  httpPost(String url, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpPost("default", url, responseHandler);
    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @throws HttpProxyRequestException
     */
    public static String httpPostforString(String poolname, String url) throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, File>) null);
    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @throws HttpProxyRequestException
     */
    public static <T> T  httpPost(String poolname, String url, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPost(  poolname,   url, (String) null, (String) null, (Map<String, Object>) null,
    			 (Map<String, File>) null, (Map<String, String>)null,responseHandler) ;

    }

    public static String httpPostforString(String url, String cookie, String userAgent,
                                           Map<String, File> files) throws HttpProxyRequestException {
        return httpPostforString("default", url, cookie, userAgent,
                files);
    }

    public static String httpPostforString(String poolname, String url, String cookie, String userAgent,
                                           Map<String, File> files) throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, cookie, userAgent, null,
                files);
    }

    public static String httpPostforString(String url, String cookie, String userAgent, Map<String, Object> params,
                                           Map<String, File> files) throws HttpProxyRequestException {
        return httpPostFileforString("default", url, cookie, userAgent, params,
                files);
    }

    public static String httpPostFileforString(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files) throws HttpProxyRequestException {
        return httpPostFileforString(poolname, url, cookie, userAgent, params,
                files, null);
    }



	public static class HttpOption{
		private String cookie;
		private String userAgent;
		private Map<String, Object> params;
		private Map<String, File> files;
		private Map<String, String> headers;
		private DataSerialType dataSerialType = DataSerialType.TEXT;
	}

	/**
	 * 公用post方法
	 *
	 * @param poolname
	 * @param url
	 * @param httpOption
	 * @throws HttpProxyRequestException
	 */
	public static <T> T httpPost(String poolname, String url, HttpOption httpOption, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
		// System.out.println("post_url==> "+url);
		// String cookie = getCookie(appContext);
		// String userAgent = getUserAgent(appContext);

		HttpClient httpClient = null;
		HttpPost httpPost = null;

//
//                .addPart("bin", bin)
//                .addPart("comment", comment)
//                .build();
//				 FileBody bin = new FileBody(new File(args[0]));
//        StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
		HttpEntity httpEntity = null;
		List<NameValuePair> paramPair = null;
		if (httpOption.files != null) {
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			// post表单参数处理
			int length = (httpOption.params == null ? 0 : httpOption.params.size()) + (httpOption.files == null ? 0 : httpOption.files.size());

			int i = 0;
			boolean hasdata = false;

			if (httpOption.params != null) {
				Iterator<Entry<String, Object>> it = httpOption.params.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, Object> entry = it.next();
					if(entry.getValue() == null)
						continue;
					if(httpOption.dataSerialType != DataSerialType.JSON || entry.getValue() instanceof String) {
						multipartEntityBuilder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), ClientConfiguration.TEXT_PLAIN_UTF_8);
					}
					else{

						multipartEntityBuilder.addTextBody(entry.getKey(), SimpleStringUtil.object2json(entry.getValue()), ClientConfiguration.TEXT_PLAIN_UTF_8);
					}
					hasdata = true;
				}
			}
			if (httpOption.files != null) {
				Iterator<Entry<String, File>> it = httpOption.files.entrySet().iterator();
				while (it.hasNext()) {
					Entry<String, File> entry = it.next();

//						parts[i++] = new FilePart(entry.getKey(), entry.getValue());
					File f = new File(String.valueOf(entry.getValue()));
					if (f.exists()) {
						FileBody file = new FileBody(f);
						multipartEntityBuilder.addPart(entry.getKey(), file);
						hasdata = true;
					} else {

					}

					// System.out.println("post_key_file==> "+file);
				}
			}
			if (hasdata)
				httpEntity = multipartEntityBuilder.build();
		} else if (httpOption.params != null && httpOption.params.size() > 0) {
			paramPair = new ArrayList<NameValuePair>();
			Iterator<Entry<String, Object>> it = httpOption.params.entrySet().iterator();
			NameValuePair paramPair_ = null;
			for (int i = 0; it.hasNext(); i++) {
				Entry<String, Object> entry = it.next();
				if(entry.getValue() == null)
					continue;
				if(httpOption.dataSerialType != DataSerialType.JSON || entry.getValue() instanceof String) {
					paramPair_ = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
				}
				else{
					paramPair_ = new BasicNameValuePair(entry.getKey(), SimpleStringUtil.object2json(entry.getValue()));
				}
				paramPair.add(paramPair_);
			}
		}

		T responseBody = null;
//        int time = 0;
		ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
//        do {
		String endpoint = null;
		Throwable e = null;
		int triesCount = 0;
		if(!url.startsWith("http://") && !url.startsWith("https://")) {
			endpoint = url;
			HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
			do {

				try {

					httpAddress = httpServiceHosts.getHttpAddress();

					url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
					if(logger.isTraceEnabled()){
						logger.trace("Post call {}",url);
					}
					httpClient = HttpRequestUtil.getHttpClient(config);
					httpPost = HttpRequestUtil.getHttpPost(config, url, httpOption.cookie, httpOption.userAgent, httpOption.headers);


					if (httpEntity != null) {
						httpPost.setEntity(httpEntity);
					} else if (paramPair != null && paramPair.size() > 0) {
						UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

						httpPost.setEntity(entity);

					}

					responseBody = httpClient.execute(httpPost, responseHandler);
					e = getException(  responseHandler,httpServiceHosts );
					break;
				} catch (HttpHostConnectException ex) {
					httpAddress.setStatus(1);
					e = new NoHttpServerException(ex);
					if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
						triesCount++;
						continue;
					} else {
						break;
					}

				} catch (UnknownHostException ex) {
					httpAddress.setStatus(1);
					e = new NoHttpServerException(ex);
					if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
						triesCount++;
						continue;
					} else {
						break;
					}

				}
				catch (NoRouteToHostException ex) {
					httpAddress.setStatus(1);
					e = new NoHttpServerException(ex);
					if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
						triesCount++;
						continue;
					} else {
						break;
					}

				}
				catch (NoHttpResponseException ex) {
					httpAddress.setStatus(1);
					e = new NoHttpServerException(ex);
					if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
						triesCount++;
						continue;
					} else {
						break;
					}

				}
				catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

					e = handleConnectionPoolTimeOutException( config,ex);
					break;
				}
				catch (ConnectTimeoutException connectTimeoutException){
					httpAddress.setStatus(1);
					e = handleConnectionTimeOutException(config,connectTimeoutException);
					if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
						triesCount++;
						continue;
					} else {
						break;
					}
				}

				catch (SocketTimeoutException ex) {
					e = handleSocketTimeoutException(config, ex);
					break;
				}
				catch (NoHttpServerException ex){
					e = ex;

					break;
				}
				catch (ClientProtocolException ex){
					throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
							.append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
				}

				catch (Exception ex) {
					e = ex;
					break;
				}
				catch (Throwable ex) {
					e = ex;
					break;
				} finally {
					// 释放连接
					if (httpPost != null)
						httpPost.releaseConnection();
					httpClient = null;
				}
			} while (true);
		}
		else{
			try {

				if(logger.isTraceEnabled()){
					logger.trace("Post call {}",url);
				}
				httpClient = HttpRequestUtil.getHttpClient(config);
				httpPost = HttpRequestUtil.getHttpPost(config, url, httpOption.cookie, httpOption.userAgent, httpOption.headers);


				if (httpEntity != null) {
					httpPost.setEntity(httpEntity);
				} else if (paramPair != null && paramPair.size() > 0) {
					UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

					httpPost.setEntity(entity);

				}

				responseBody = httpClient.execute(httpPost, responseHandler);

			} catch (Exception ex) {
				e = ex;
			} finally {
				// 释放连接
				if (httpPost != null)
					httpPost.releaseConnection();
				httpClient = null;
			}
		}
		if (e != null){
			if(e instanceof HttpProxyRequestException)
				throw (HttpProxyRequestException)e;
			throw new HttpProxyRequestException(e);
		}
		return responseBody;

	}
    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPost(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                 Map<String, File> files, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
		HttpOption httpOption = new HttpOption();
		httpOption.cookie = cookie;
		httpOption.userAgent = userAgent;
		httpOption.params = params;
		httpOption.files = files;
		httpOption.headers = headers;
    	return httpPost(  poolname,   url,  httpOption,  responseHandler);

    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPutforString(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                          Map<String, File> files, Map<String, String> headers) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.cookie = cookie;
        httpOption.userAgent = userAgent;
        httpOption.params = params;
        httpOption.files = files;
        httpOption.headers = headers;
        return httpPut(  poolname,   url,   httpOption,  new StringResponseHandler());

    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static String httpPutforString(String url, Map<String, Object> params, Map<String, String> headers ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut(  "default",   url,   httpOption,  new StringResponseHandler());
//    	return httpPut(  "default",   url,   (String)null,   (String)null,  params,
//    			( Map<String, File> )null,   headers,new StringResponseHandler());
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <T> T httpPutforObject(String url, Map<String, Object> params, Map<String, String> headers, final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut(  "default",   url,   httpOption,  new ResponseHandler<T>(){

            @Override
            public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleResponse(response,resultType);
            }
        });
//        return httpPut(  "default",   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<T>(){
//
//                    @Override
//                    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleResponse(response,resultType);
//                    }
//                });
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <T> List<T> httpPutforList(String url, Map<String, Object> params, Map<String, String> headers, final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( "default",   url,   httpOption, new ResponseHandler<List<T>>(){

            @Override
            public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(response,resultType);
            }
        });
//        return httpPut(  "default",   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<List<T>>(){
//
//                    @Override
//                    public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleListResponse(response,resultType);
//                    }
//                });
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <T> Set<T> httpPutforSet(String url, Map<String, Object> params, Map<String, String> headers, final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( "default",   url,   httpOption, new ResponseHandler<Set<T>>(){

            @Override
            public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleSetResponse(response,resultType);
            }
        });
//        return httpPut(  "default",   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<Set<T>>(){
//
//                    @Override
//                    public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleSetResponse(response,resultType);
//                    }
//                });
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <K,T> Map<K,T> httpPutforObject(String url, Map<String, Object> params, Map<String, String> headers, final Class<K> keyType , final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut(  "default",   url,   httpOption,  new ResponseHandler<Map<K,T>>(){

            @Override
            public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
            }
        });
//        return httpPut(  "default",   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<Map<K,T>>(){
//
//                    @Override
//                    public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
//                    }
//                });
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <T> T httpPutforObject(String poolName, String url, Map<String, Object> params, Map<String, String> headers, final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( poolName,   url,   httpOption, new ResponseHandler<T>(){

            @Override
            public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleResponse(response,resultType);
            }
        });
//        return httpPut(  poolName,   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<T>(){
//
//                    @Override
//                    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleResponse(response,resultType);
//                    }
//                });
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <T> List<T> httpPutforList(String poolName, String url, Map<String, Object> params, Map<String, String> headers, final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( poolName,   url,   httpOption, new ResponseHandler<List<T>>(){

            @Override
            public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(response,resultType);
            }
        });
//        return httpPut(  poolName,   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<List<T>>(){
//
//                    @Override
//                    public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleListResponse(response,resultType);
//                    }
//                });
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <T> Set<T> httpPutforSet(String poolName, String url, Map<String, Object> params, Map<String, String> headers, final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( poolName,   url,   httpOption, new ResponseHandler<Set<T>>(){

            @Override
            public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleSetResponse(response,resultType);
            }
        });
//        return httpPut(  poolName,   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<Set<T>>(){
//
//                    @Override
//                    public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleSetResponse(response,resultType);
//                    }
//                });
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @return
     * @throws HttpProxyRequestException
     */

    public static <K,T> Map<K,T> httpPutforObject(String poolName, String url, Map<String, Object> params, Map<String, String> headers, final Class<K> keyType , final Class<T> resultType ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( poolName,   url,   httpOption, new ResponseHandler<Map<K,T>>(){

            @Override
            public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
            }
        });
//        return httpPut(   poolName,   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new ResponseHandler<Map<K,T>>(){
//
//                    @Override
//                    public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
//                        return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
//                    }
//                });
    }

    public static <T> T httpPutforString(String url, Map<String, Object> params, Map<String, String> headers , ResponseHandler<T> responseHandler ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( "default",   url,   httpOption, responseHandler);
//        return httpPut(  "default",   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers ,responseHandler);
    }

    public static String httpPutforString(String poolname, String url, Map<String, Object> params, Map<String, String> headers ) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( poolname,   url,   httpOption,new StringResponseHandler());
//        return httpPut(  poolname,   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,new StringResponseHandler());
    }

    public static <T> T httpPutforString(String poolname, String url, Map<String, Object> params, Map<String, String> headers , ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.params = params;
        httpOption.headers = headers;
        return httpPut( poolname,   url,   httpOption,responseHandler);
//        return httpPut(  poolname,   url,   (String)null,   (String)null,  params,
//                ( Map<String, File> )null,   headers,responseHandler);
    }

    /**
     * 公用post方法
     *

     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, String cookie, String userAgent, Map<String, Object> params,
                                Map<String, File> files, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        httpOption.cookie = cookie;
        httpOption.userAgent = userAgent;
        httpOption.params = params;
        httpOption.headers = headers;
        httpOption.files = files;
        return httpPut( "default",   url,   httpOption,responseHandler);
//        return httpPut("default", url, cookie, userAgent, params,
//                                                    files, headers, responseHandler) ;
    }

    /**
     *
     * @param url
     * @param params
     * @param headers
     * @param responseHandler
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPut( url, (String)null, (String)null, (Map<String, Object>)params,
    							(Map<String, File>)null, headers, responseHandler) ;
    }

    /**
     *
     * @param url
     * @param params
     * @param responseHandler
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, Map<String, Object> params, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPut( url, (String)null, (String)null, (Map<String, Object>)params,
    							(Map<String, File>)null, (Map<String, String>)null, responseHandler) ;
    }

    /**
     *
     * @param url
     * @param responseHandler
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
    	return httpPut( url, (String)null, (String)null, (Map<String, Object>)null,
    							(Map<String, File>)null, (Map<String, String>)null, responseHandler) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPut(url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, File>) null, (Map<String, String>) null, new ResponseHandler<T>() {
                    @Override
                    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleResponse(response,resultType);
                    }
                }) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> List<T> httpPutForList(String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPut(url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, File>) null, (Map<String, String>) null, new ResponseHandler<List<T>>() {
                    @Override
                    public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleListResponse(response,resultType);
                    }
                }) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> Set<T> httpPutForSet(String url, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPut(url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, File>) null, (Map<String, String>) null, new ResponseHandler<Set<T>>() {
                    @Override
                    public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleSetResponse(response,resultType);
                    }
                }) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @return
     * @throws HttpProxyRequestException
     */
    public static <K,T> Map<K,T> httpPutForMap(String url, final Class<K> keyType, final Class<T> resultType) throws HttpProxyRequestException {
        return httpPut(url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, File>) null, (Map<String, String>) null, new ResponseHandler<Map<K,T>>() {
                    @Override
                    public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
                    }
                }) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String poolName, String url, final Class<T> resultType) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        return httpPut(  poolName,url, httpOption, new ResponseHandler<T>() {
                    @Override
                    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleResponse(response,resultType);
                    }
                }) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> List<T> httpPutForList(String poolName, String url, final Class<T> resultType) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        return httpPut(  poolName,url, httpOption, new ResponseHandler<List<T>>() {
                    @Override
                    public List<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleListResponse(response,resultType);
                    }
                }) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @param <T>
     * @return
     * @throws HttpProxyRequestException
     */
    public static <T> Set<T> httpPutForSet(String poolName, String url, final Class<T> resultType) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        return httpPut(  poolName,url,httpOption, new ResponseHandler<Set<T>>() {
                    @Override
                    public Set<T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleSetResponse(response,resultType);
                    }
                }) ;
    }

    /**
     *
     * @param url
     * @param resultType
     * @return
     * @throws HttpProxyRequestException
     */
    public static <K,T> Map<K,T> httpPutForMap(String poolName, String url, final Class<K> keyType, final Class<T> resultType) throws HttpProxyRequestException {
        HttpOption httpOption = new HttpOption();
        return httpPut(  poolName,url, httpOption, new ResponseHandler<Map<K,T>>() {
                    @Override
                    public Map<K,T> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                        return HttpRequestProxy.handleMapResponse(response,keyType,resultType);
                    }
                }) ;
    }
    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                Map<String, File> files, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // System.out.println("post_url==> "+url);
        // String cookie = getCookie(appContext);
        // String userAgent = getUserAgent(appContext);
        HttpOption httpOption = new HttpOption();
        httpOption.cookie = cookie;
        httpOption.userAgent = userAgent;
        httpOption.params = params;
        httpOption.files = files;
        httpOption.headers = headers;
        return httpPut(  poolname,   url,   httpOption,  responseHandler);

    }


    /**
     * 公用put方法
     *
     * @param poolname
     * @param url
     * @param httpOption
     * @param responseHandler
     * @throws HttpProxyRequestException
     */
    public static <T> T httpPut(String poolname, String url, HttpOption httpOption, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        // System.out.println("post_url==> "+url);
        // String cookie = getCookie(appContext);
        // String userAgent = getUserAgent(appContext);

        HttpClient httpClient = null;
        HttpPut httpPut = null;

//
//                .addPart("bin", bin)
//                .addPart("comment", comment)
//                .build();
//				 FileBody bin = new FileBody(new File(args[0]));
//        StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);
        HttpEntity httpEntity = null;
        List<NameValuePair> paramPair = null;
        if (httpOption.files != null) {
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
            // post表单参数处理
            int length = (httpOption.params == null ? 0 : httpOption.params.size()) + (httpOption.files == null ? 0 : httpOption.files.size());

            int i = 0;
            boolean hasdata = false;

            if (httpOption.params != null) {
                Iterator<Entry<String, Object>> it = httpOption.params.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, Object> entry = it.next();
                    if(entry.getValue() == null){
                        continue;
                    }
                    if(httpOption.dataSerialType != DataSerialType.JSON || entry.getValue() instanceof String)
                        multipartEntityBuilder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()), ClientConfiguration.TEXT_PLAIN_UTF_8);
                    else {
                        multipartEntityBuilder.addTextBody(entry.getKey(), SimpleStringUtil.object2json(entry.getValue()), ClientConfiguration.TEXT_PLAIN_UTF_8);
                    }
                    hasdata = true;
                }
            }
            if (httpOption.files != null) {
                Iterator<Entry<String, File>> it = httpOption.files.entrySet().iterator();
                while (it.hasNext()) {
                    Entry<String, File> entry = it.next();

//						parts[i++] = new FilePart(entry.getKey(), entry.getValue());
                    File f = new File(String.valueOf(entry.getValue()));
                    if (f.exists()) {
                        FileBody file = new FileBody(f);
                        multipartEntityBuilder.addPart(entry.getKey(), file);
                        hasdata = true;
                    } else {

                    }

                    // System.out.println("post_key_file==> "+file);
                }
            }
            if (hasdata)
                httpEntity = multipartEntityBuilder.build();
        } else if (httpOption.params != null && httpOption.params.size() > 0) {
            paramPair = new ArrayList<NameValuePair>();
            Iterator<Entry<String, Object>> it = httpOption.params.entrySet().iterator();
            NameValuePair paramPair_ = null;
            for (int i = 0; it.hasNext(); i++) {
                Entry<String, Object> entry = it.next();
                if(entry.getValue() == null){
                    continue;
                }
                if(httpOption.dataSerialType != DataSerialType.JSON || entry.getValue() instanceof String) {
                    paramPair_ = new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
                }
                else{
                    paramPair_ = new BasicNameValuePair(entry.getKey(), SimpleStringUtil.object2json(entry.getValue()));
                }
                paramPair.add(paramPair_);
            }
        }

        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
            do {

                try {

                    httpAddress = httpServiceHosts.getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Put call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPut = HttpRequestUtil.getHttpPut(config, url, httpOption.cookie, httpOption.userAgent, httpOption.headers);


                    if (httpEntity != null) {
                        httpPut.setEntity(httpEntity);
                    } else if (paramPair != null && paramPair.size() > 0) {
                        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

                        httpPut.setEntity(entity);

                    }
                    responseBody = httpClient.execute(httpPut, responseHandler);
                    e = getException(  responseHandler,httpServiceHosts );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpPut != null)
                        httpPut.releaseConnection();
                    httpClient = null;
                }
//        } while (time < RETRY_TIME);
            } while (true);
        }
        else
        {
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("Put call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPut = HttpRequestUtil.getHttpPut(config, url, httpOption.cookie, httpOption.userAgent, httpOption.headers);
                if (httpEntity != null) {
                    httpPut.setEntity(httpEntity);
                } else if (paramPair != null && paramPair.size() > 0) {
                    UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramPair, Consts.UTF_8);

                    httpPut.setEntity(entity);

                }
                responseBody = httpClient.execute(httpPut, responseHandler);
            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpPut != null)
                    httpPut.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }

    /**
     * 公用post方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param files
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpPostFileforString(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                               Map<String, File> files, Map<String, String> headers) throws HttpProxyRequestException {

    	return httpPost(  poolname,   url,   cookie,   userAgent,   params,
                  files,  headers,new StringResponseHandler() );


    }




    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String poolname, String url) throws HttpProxyRequestException {
       return httpDelete(  poolname,   url, (String) null, (String) null, (Map<String, Object>) null,
               (Map<String, String>) null);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String url) throws HttpProxyRequestException {
        return httpDelete(  "default",   url, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, String>) null);

    }
    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDeleteWithbody(String url, String requestBody) throws HttpProxyRequestException {
        return httpDelete(  "default",   url,requestBody, (String) null, (String) null, (Map<String, Object>) null,
                (Map<String, String>) null);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String url, Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  "default",   url, (String) null, (String) null, (Map<String, Object>) null,
                headers);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String url, String requestBody, Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  "default",   url,  requestBody, (String) null, (String) null, (Map<String, Object>) null,
                headers);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDeleteWithbody(String url, String requestBody, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  "default",   url, requestBody, (String) null, (String) null, params,
                headers);

    }

    /**
     * 公用delete方法
     *
     * @param url

     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String url, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  "default",   url, (String) null, (String) null, params,
                headers);

    }



    public static <T> T httpDelete(String url, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpDelete(  "default",   url, (String)null,(String) null, (String) null, params,
                headers, responseHandler);

    }

    public static <T> T httpDeleteWithBody (String url, String requestBody, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpDelete(  "default",   url, requestBody,(String) null, (String) null, params,
                headers, responseHandler);

    }

    public static String httpDelete(String poolname, String url, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  poolname,   url, (String) null, (String) null, params,
                headers);

    }

    public static String httpDelete (String poolname, String url, String requestBody, Map<String, Object> params, Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  poolname,   url,  requestBody,(String)null, (String) null, params,
                headers);

    }

    public static <T> T httpDelete(String poolname, String url, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpDelete(  poolname,   url,(String)null, (String) null, (String) null, params,
                headers,responseHandler);

    }

    public static <T> T httpDelete(String poolname, String url, String requestBody, Map<String, Object> params, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return httpDelete(  poolname,     url, requestBody,(String) null, (String) null, params,
                headers,responseHandler);

    }
    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String poolname, String url, String cookie, String userAgent, Map<String, Object> params,
                                    Map<String, String> headers) throws HttpProxyRequestException {
    	return httpDelete(  poolname,   url, (String)null  ,cookie,   userAgent,   params,
                  headers,new StringResponseHandler());
    }
    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static String httpDelete(String poolname, String url, String requestBody, String cookie, String userAgent, Map<String, Object> params,
                                    Map<String, String> headers) throws HttpProxyRequestException {
        return httpDelete(  poolname,   url,  requestBody, cookie,   userAgent,   params,
                headers,new StringResponseHandler());
    }
    /**
     * 公用delete方法
     *
     * @param poolname
     * @param url
     * @param cookie
     * @param userAgent
     * @param params
     * @param headers
     * @throws HttpProxyRequestException
     */
    public static <T> T httpDelete(String poolname, String url, String requestBody, String cookie, String userAgent, Map<String, Object> params,
                                   Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {


        HttpClient httpClient = null;
        HttpDeleteWithBody httpDeleteWithBody = null;
        HttpDelete httpDelete = null;


        T responseBody = null;
//        int time = 0;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        HttpEntity httpEntity = requestBody == null?null:new StringEntity(
                requestBody,
                ContentType.APPLICATION_JSON);
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
            do {

                try {

                    httpAddress = httpServiceHosts.getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("Delete call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    HttpParams httpParams = null;
                    if (params != null && params.size() > 0) {
                        httpParams = new BasicHttpParams();
                        Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                        NameValuePair paramPair_ = null;
                        for (int i = 0; it.hasNext(); i++) {
                            Entry<String, Object> entry = it.next();
                            httpParams.setParameter(entry.getKey(), entry.getValue());
                        }
                    }
                    if (httpEntity != null) {
                        httpDeleteWithBody = HttpRequestUtil.getHttpDeleteWithBody(config, url, cookie, userAgent, headers);
                        httpDeleteWithBody.setEntity(httpEntity);
                        if (httpParams != null) {

                            httpDeleteWithBody.setParams(httpParams);
                        }
                        responseBody = httpClient.execute(httpDeleteWithBody, responseHandler);
                    } else {
                        httpDelete = HttpRequestUtil.getHttpDelete(config, url, cookie, userAgent, headers);
                        if (httpParams != null) {
                            httpDelete.setParams(httpParams);
                        }
                        responseBody = httpClient.execute(httpDelete, responseHandler);
                    }

                    e = getException(  responseHandler,httpServiceHosts );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpDelete != null)
                        httpDelete.releaseConnection();
                    httpClient = null;
                }
            } while (true);
        }
        else{
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("Delete call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                HttpParams httpParams = null;
                if (params != null && params.size() > 0) {
                    httpParams = new BasicHttpParams();
                    Iterator<Entry<String, Object>> it = params.entrySet().iterator();
                    NameValuePair paramPair_ = null;
                    for (int i = 0; it.hasNext(); i++) {
                        Entry<String, Object> entry = it.next();
                        httpParams.setParameter(entry.getKey(), entry.getValue());
                    }
                }
                if (httpEntity != null) {
                    httpDeleteWithBody = HttpRequestUtil.getHttpDeleteWithBody(config, url, cookie, userAgent, headers);
                    httpDeleteWithBody.setEntity(httpEntity);
                    if (httpParams != null) {

                        httpDeleteWithBody.setParams(httpParams);
                    }
                    responseBody = httpClient.execute(httpDeleteWithBody, responseHandler);
                } else {
                    httpDelete = HttpRequestUtil.getHttpDelete(config, url, cookie, userAgent, headers);
                    if (httpParams != null) {
                        httpDelete.setParams(httpParams);
                    }
                    responseBody = httpClient.execute(httpDelete, responseHandler);
                }


            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpDelete != null)
                    httpDelete.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;

    }


    public static String sendStringBody(String poolname, String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return  sendBody(poolname,  requestBody,   url,   headers, ContentType.create(
                "text/plain", Consts.UTF_8));
    }

    public static String sendJsonBody(String poolname, String requestBody, String url) throws HttpProxyRequestException {

        return  sendBody(   poolname, requestBody,   url,   null, ContentType.APPLICATION_JSON);
    }

    public static <T> T sendJsonBody(String poolname, String requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBody(   poolname, requestBody,   url,   null, ContentType.APPLICATION_JSON,resultType);
    }
    public static <T> List<T> sendJsonBodyForList(String poolname, String requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBodyForList(   poolname, requestBody,   url,   null, ContentType.APPLICATION_JSON,resultType);
    }

    public static <T> Set<T> sendJsonBodyForSet(String poolname, String requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBodyForSet(   poolname, requestBody,   url,   null, ContentType.APPLICATION_JSON,resultType);
    }

    public static <K,T> Map<K,T> sendJsonBodyForMap(String poolname, String requestBody, String url, Class<K> keyType, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBodyForMap(   poolname, requestBody,   url,   null, ContentType.APPLICATION_JSON, keyType,resultType);
    }
    public static String sendJsonBody(String poolname, Object requestBody, String url) throws HttpProxyRequestException {

        return  sendBody(   poolname, SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON);
    }

    public static <T> T sendJsonBody(String poolname, Object requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBody(   poolname, SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON,  resultType);
    }

    public static <T> List<T> sendJsonBodyForList(String poolname, Object requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBodyForList(   poolname, SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON,  resultType);
    }
	public static <T> List<T> sendJsonBodyForList(Object requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

		return  sendBodyForList(   (String) null, SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON,  resultType);
	}
    public static <T> Set<T> sendJsonBodyForSet(String poolname, Object requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBodyForSet(   poolname, SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON,  resultType);
    }

    public static <K,T> Map<K,T> sendJsonBodyForMap(String poolname, Object requestBody, String url, Class<K> keyType, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBodyForMap(   poolname, SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON, keyType, resultType);
    }
    public static String sendStringBody(String poolname, String requestBody, String url) throws HttpProxyRequestException {
        return  sendBody(  poolname,  requestBody,   url,   null, ContentType.create(
                "text/plain", Consts.UTF_8));
    }


    public static String sendJsonBody(String poolname, Object requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {

        return  sendBody(  poolname, SimpleStringUtil.object2json(requestBody),   url,   headers, ContentType.APPLICATION_JSON);
    }
    public static String sendJsonBody(String poolname, String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {

        return  sendBody(  poolname, requestBody,   url,   headers, ContentType.APPLICATION_JSON);
    }
    public static String sendStringBody(String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return  sendBody("default",  requestBody,   url,   headers, ContentType.create(
                "text/plain", Consts.UTF_8));
    }

    public static String sendJsonBody(String requestBody, String url) throws HttpProxyRequestException {

        return  sendBody( "default", requestBody,   url,   null, ContentType.APPLICATION_JSON);
    }
    public static <T> T sendJsonBody(String requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBody( "default", requestBody,   url,   null, ContentType.APPLICATION_JSON,  resultType);
    }
    public static String sendJsonBody(String url) throws HttpProxyRequestException {

        return  sendBody( "default", (String)null,   url,   null, ContentType.APPLICATION_JSON);
    }

    public static String sendJsonBodyWithPool(String poolName, String url) throws HttpProxyRequestException {

        return  sendBody( poolName, (String)null,   url,   null, ContentType.APPLICATION_JSON);
    }

    public static String sendJsonBody(Object requestBody, String url) throws HttpProxyRequestException {

        return  sendBody( "default", SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON);
    }

    public static <T> T sendJsonBody(Object requestBody, String url, Class<T> resultType) throws HttpProxyRequestException {

        return  sendBody( "default", SimpleStringUtil.object2json(requestBody),   url,   null, ContentType.APPLICATION_JSON,resultType);
    }

    public static String sendStringBody(String requestBody, String url) throws HttpProxyRequestException {
        return  sendBody("default",  requestBody,   url,   null, ContentType.create(
                "text/plain", Consts.UTF_8));
    }

    public static String sendStringBody(String requestBody, String url, String mimeType, Charset charSet) throws HttpProxyRequestException {
        return  sendBody("default",  requestBody,   url,   null, ContentType.create(
                mimeType, charSet));
    }

    public static String sendJsonBody(String requestBody, String url, Map<String, String> headers ) throws HttpProxyRequestException {

        return  sendBody( "default", requestBody,   url,   headers, ContentType.APPLICATION_JSON);
    }
    public static <T> T sendJsonBody(String requestBody, String url, Map<String, String> headers  , ResponseHandler<T> responseHandler) throws HttpProxyRequestException {

        return  sendBody( "default", requestBody,   url,   headers, ContentType.APPLICATION_JSON, responseHandler);
    }

    public static <T> T sendJsonBody(String poolname, String requestBody, String url, Map<String, String> headers  , ResponseHandler<T> responseHandler) throws HttpProxyRequestException {

        return  sendBody( poolname, requestBody,   url,   headers, ContentType.APPLICATION_JSON, responseHandler);
    }
    public static <T> T sendBody(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        HttpClient httpClient = null;
        HttpPost httpPost = null;


        HttpEntity httpEntity = new StringEntity(
                requestBody,
                contentType);
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        T responseBody = null;
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
            do {

                try {

                    httpAddress = httpServiceHosts.getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("sendBody call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPost = HttpRequestUtil.getHttpPost(config, url, "", "", headers);
                    if (httpEntity != null) {
                        httpPost.setEntity(httpEntity);
                    }

                    responseBody = httpClient.execute(httpPost, responseHandler);
                    e = getException(  responseHandler,httpServiceHosts );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                }  finally {
                    // 释放连接
                    if (httpPost != null)
                        httpPost.releaseConnection();
                    httpClient = null;
                }


            } while (true);
        }
        else{
            try {
                if(logger.isTraceEnabled()){
                    logger.trace("sendBody call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPost = HttpRequestUtil.getHttpPost(config, url, "", "", headers);
                if (httpEntity != null) {
                    httpPost.setEntity(httpEntity);
                }

                responseBody = httpClient.execute(httpPost, responseHandler);

            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpPost != null)
                    httpPost.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;
    }
    private static HttpRequestBase getHttpEntityEnclosingRequestBase(String action, ClientConfiguration config, String url, Map<String, String> headers){
        if(action.equals(HttpRequestUtil.HTTP_POST)){
            return HttpRequestUtil.getHttpPost(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_GET)){
            return HttpRequestUtil.getHttpGet(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_PUT)){
            return HttpRequestUtil.getHttpPut(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_DELETE)){
            return HttpRequestUtil.getHttpDelete(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_HEAD)){
            return HttpRequestUtil.getHttpHead(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_TRACE)){
            return HttpRequestUtil.getHttpTrace(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_OPTIONS)){
            return HttpRequestUtil.getHttpOptions(config, url, null, null, headers);
        }
        else if(action.equals(HttpRequestUtil.HTTP_PATCH)){
            return HttpRequestUtil.getHttpPatch(config, url, null, null, headers);
        }
        throw new IllegalArgumentException("not support http action:"+action);
    }
    private static HttpProxyRequestException handleSocketTimeoutException(ClientConfiguration configuration, SocketTimeoutException ex){
        if(configuration == null){
            return new HttpProxyRequestException(ex);
        }
        else{
            StringBuilder builder = new StringBuilder();
            builder.append("Socket Timeout for ").append(configuration.getTimeoutSocket()).append("ms.");

            return new HttpProxyRequestException(builder.toString(),ex);
        }
    }

    private static HttpProxyRequestException handleConnectionPoolTimeOutException(ClientConfiguration configuration, ConnectionPoolTimeoutException ex){
        if(configuration == null){
            return new HttpProxyRequestException(ex);
        }
        else{
            StringBuilder builder = new StringBuilder();
            builder.append("Wait timeout for ").append(configuration.getConnectionRequestTimeout()).append("ms for idle http connection from http connection pool.");

            return new HttpProxyRequestException(builder.toString(),ex);
        }
    }

    private static HttpProxyRequestException handleConnectionTimeOutException(ClientConfiguration configuration, ConnectTimeoutException ex){
        if(configuration == null){
            return new HttpProxyRequestException(ex);
        }
        else{
            StringBuilder builder = new StringBuilder();
            builder.append("Build a http connection timeout for ").append(configuration.getTimeoutConnection()).append("ms.");

            return new HttpProxyRequestException(builder.toString(),ex);
        }
    }

    public static <T> T sendBody(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType,
                                 ResponseHandler<T> responseHandler, String action) throws HttpProxyRequestException {
        HttpClient httpClient = null;
        HttpEntityEnclosingRequestBase httpPost = null;


        HttpEntity httpEntity = requestBody != null?new StringEntity(
                requestBody,
                contentType):null;
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        T responseBody = null;
//        int time = 0;
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")){
            endpoint = url;
            HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
            do {

                try {

                    httpAddress = httpServiceHosts.getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(),endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("sendBody call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPost = (HttpEntityEnclosingRequestBase) getHttpEntityEnclosingRequestBase(action, config, url, headers);
                    if (httpEntity != null) {
                        httpPost.setEntity(httpEntity);
                    }

                    responseBody = httpClient.execute(httpPost, responseHandler);
                    e = getException(  responseHandler,httpServiceHosts );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                } finally {
                    // 释放连接
                    if (httpPost != null)
                        httpPost.releaseConnection();
                    httpClient = null;
                }

//        } while (time < RETRY_TIME);
            }while(true);
        }
        else{
            try {

                if(logger.isTraceEnabled()){
                    logger.trace("sendBody call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPost = (HttpEntityEnclosingRequestBase) getHttpEntityEnclosingRequestBase(action, config, url, headers);
                if (httpEntity != null) {
                    httpPost.setEntity(httpEntity);
                }

                responseBody = httpClient.execute(httpPost, responseHandler);

            } catch (Exception ex) {
                 e = ex;
            } finally {
                // 释放连接
                if (httpPost != null)
                    httpPost.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;
    }
    
    public static String sendBody(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType) throws HttpProxyRequestException {
    	return sendBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    HttpEntity entity = response.getEntity();
                    if (entity != null )
                        return EntityUtils.toString(entity);
                    else
                        throw new HttpProxyRequestException("Unexpected response status: " + status);
                }
            }

        });
        
    }
    public static <T> T converJson(HttpEntity entity, Class<T> clazz) throws IOException {
        InputStream inputStream = null;

        T var4;
        try {
            inputStream = entity.getContent();
            if(inputStream instanceof EmptyInputStream)
                return null;
            var4 = SimpleStringUtil.json2Object(inputStream, clazz);
        } finally {
            inputStream.close();
        }

        return var4;
    }

    public static <T> List<T> converJson2List(HttpEntity entity, Class<T> clazz) throws IOException {
        InputStream inputStream = null;

        List<T> var4;
        try {
            inputStream = entity.getContent();
            var4 = SimpleStringUtil.json2ListObject(inputStream, clazz);
        } finally {
            inputStream.close();
        }

        return var4;
    }

    public static <T> Set<T> converJson2Set(HttpEntity entity, Class<T> clazz) throws IOException {
        InputStream inputStream = null;

        Set<T> var4;
        try {
            inputStream = entity.getContent();
            var4 = SimpleStringUtil.json2LSetObject(inputStream, clazz);
        } finally {
            inputStream.close();
        }

        return var4;
    }

    public static <K,T> Map<K,T> converJson2Map(HttpEntity entity, Class<K> keyType, Class<T> beanType) throws IOException {
        InputStream inputStream = null;

        Map<K,T> var4;
        try {
            inputStream = entity.getContent();
            var4 = SimpleStringUtil.json2LHashObject(inputStream,  keyType, beanType);
        } finally {
            inputStream.close();
        }

        return var4;
    }
    public static <K,T> Map<K,T> handleMapResponse(HttpResponse response, Class<K> keyType, Class<T> beanType)
            throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();

        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? converJson2Map(  entity,  keyType,  beanType) : null;
        } else {
            HttpEntity entity = response.getEntity();
            if (entity != null )
                throw new HttpProxyRequestException(EntityUtils.toString(entity));
            else
                throw new HttpProxyRequestException("Unexpected response status: " + status);
        }
    }
    public static <T> List<T> handleListResponse(HttpResponse response, Class<T> resultType)
            throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();

        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? converJson2List(  entity,  resultType) : null;
        } else {
            HttpEntity entity = response.getEntity();
            if (entity != null )
                throw new HttpProxyRequestException(EntityUtils.toString(entity));
            else
                throw new HttpProxyRequestException("Unexpected response status: " + status);
        }
    }
    public static <T> Set<T> handleSetResponse(HttpResponse response, Class<T> resultType)
            throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();

        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? converJson2Set(  entity,  resultType) : null;
        } else {
            HttpEntity entity = response.getEntity();
            if (entity != null )
                throw new HttpProxyRequestException(EntityUtils.toString(entity));
            else
                throw new HttpProxyRequestException("Unexpected response status: " + status);
        }
    }
    public static <T> T handleResponse(HttpResponse response, Class<T> resultType)
            throws ClientProtocolException, IOException {
        int status = response.getStatusLine().getStatusCode();

        if (status >= 200 && status < 300) {
            HttpEntity entity = response.getEntity();
            return entity != null ? converJson(  entity,  resultType) : null;
        } else {
            HttpEntity entity = response.getEntity();
            if (entity != null )
                throw new HttpProxyRequestException(EntityUtils.toString(entity));
            else
                throw new HttpProxyRequestException("Unexpected response status: " + status);
        }
    }
    public static <T> T sendBody(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType, final Class<T> resultType) throws HttpProxyRequestException {
        return sendBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<T>() {

            @Override
            public T handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleResponse(  response, resultType);
            }

        });

    }
    public static <T> List<T> sendBodyForList(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType, final Class<T> resultType) throws HttpProxyRequestException {
        return sendBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<List<T>>() {

            @Override
            public List<T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleListResponse(  response, resultType);
            }

        });

    }

    public static <T> Set<T> sendBodyForSet(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType, final Class<T> resultType) throws HttpProxyRequestException {
        return sendBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<Set<T>>() {

            @Override
            public Set<T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleSetResponse(  response, resultType);
            }

        });

    }

    public static <K,T> Map<K,T> sendBodyForMap(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType, final Class<K> keyType, final Class<T> resultType) throws HttpProxyRequestException {
        return sendBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<Map<K,T>>() {

            @Override
            public Map<K,T> handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                return HttpRequestProxy.handleMapResponse(  response, keyType,resultType);
            }

        });

    }
    private static void assertCheck(HttpServiceHosts httpServiceHosts ){


        if(httpServiceHosts == null){
            if(logger.isWarnEnabled()){
                logger.warn("Http Request Proxy is not properly initialized, please refer to the document: https://esdoc.bbossgroups.com/#/httpproxy?id=_32-http负载均衡器配置和启动");
            }
            throw new HttpProxyRequestException("Http Request Proxy is not properly initialized, please refer to the document: https://esdoc.bbossgroups.com/#/httpproxy?id=_32-http负载均衡器配置和启动");
        }
    }
    public static <T> T putBody(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        HttpClient httpClient = null;
        HttpPut httpPost = null;


        HttpEntity httpEntity = new StringEntity(
                requestBody,
                contentType);
        ClientConfiguration config = ClientConfiguration.getClientConfiguration(poolname);
//        int RETRY_TIME = config.getRetryTime();
        T responseBody = null;
//        int time = 0;
//        do {
        String endpoint = null;
        Throwable e = null;
        int triesCount = 0;
        if(!url.startsWith("http://") && !url.startsWith("https://")) {
            endpoint = url;
            HttpAddress httpAddress = null;
			HttpServiceHosts httpServiceHosts = config.getHttpServiceHosts();
            assertCheck(  httpServiceHosts );
            do {

                try {

                    httpAddress = httpServiceHosts.getHttpAddress();

                    url = SimpleStringUtil.getPath(httpAddress.getAddress(), endpoint);
                    if(logger.isTraceEnabled()){
                        logger.trace("putBody call {}",url);
                    }
                    httpClient = HttpRequestUtil.getHttpClient(config);
                    httpPost = HttpRequestUtil.getHttpPut(config, url, "", "", headers);
                    if (httpEntity != null) {
                        httpPost.setEntity(httpEntity);
                    }

                    responseBody = httpClient.execute(httpPost, responseHandler);
                    e = getException(  responseHandler,httpServiceHosts );
                    break;
                } catch (HttpHostConnectException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                } catch (UnknownHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoRouteToHostException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (NoHttpResponseException ex) {
                    httpAddress.setStatus(1);
                    e = new NoHttpServerException(ex);
                    if (!httpServiceHosts.reachEnd(triesCount ))  {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }

                }
                catch (ConnectionPoolTimeoutException ex){//连接池获取connection超时，直接抛出

                    e = handleConnectionPoolTimeOutException( config,ex);
                    break;
                }
                catch (ConnectTimeoutException connectTimeoutException){
                    httpAddress.setStatus(1);
                    e = handleConnectionTimeOutException(config,connectTimeoutException);
                    if (!httpServiceHosts.reachEnd(triesCount )) {//失败尝试下一个地址
                        triesCount++;
                        continue;
                    } else {
                        break;
                    }
                }

                catch (SocketTimeoutException ex) {
                    e = handleSocketTimeoutException(config, ex);
                    break;
                }
                catch (NoHttpServerException ex){
                    e = ex;

                    break;
                }
                catch (ClientProtocolException ex){
                    throw new HttpProxyRequestException(new StringBuilder().append("Request[").append(url)
                            .append("] handle failed: must use http/https protocol port such as 9200,do not use other transport such as 9300.").toString(),ex);
                }

                catch (Exception ex) {
                    e = ex;
                    break;
                }
                catch (Throwable ex) {
                    e = ex;
                    break;
                }  finally {
                    // 释放连接
                    if (httpPost != null)
                        httpPost.releaseConnection();
                    httpClient = null;
                }

            } while (true);
        }
        else{
            try {

                if(logger.isTraceEnabled()){
                    logger.trace("putBody call {}",url);
                }
                httpClient = HttpRequestUtil.getHttpClient(config);
                httpPost = HttpRequestUtil.getHttpPut(config, url, "", "", headers);
                if (httpEntity != null) {
                    httpPost.setEntity(httpEntity);
                }

                responseBody = httpClient.execute(httpPost, responseHandler);
            } catch (Exception ex) {
                e = ex;
            } finally {
                // 释放连接
                if (httpPost != null)
                    httpPost.releaseConnection();
                httpClient = null;
            }
        }
        if (e != null){
            if(e instanceof HttpProxyRequestException)
                throw (HttpProxyRequestException)e;
            throw new HttpProxyRequestException(e);
        }
        return responseBody;
    }
    
    public static String putBody(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType) throws HttpProxyRequestException {
    	return putBody(  poolname,  requestBody,   url, headers,  contentType, new ResponseHandler<String>() {

            @Override
            public String handleResponse(final HttpResponse response)
                    throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();

                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    HttpEntity entity = response.getEntity();
                    if (entity != null )
                        throw new HttpProxyRequestException( EntityUtils.toString(entity));
                    else
                        throw new HttpProxyRequestException("Unexpected response status: " + status);
                }
            }

        });
        
    }
    
    public static <T> T putBody(String requestBody, String url, Map<String, String> headers, ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return putBody( "default", requestBody,   url,  headers,  contentType,  responseHandler) ;
    }
    
    public static String putBody(String requestBody, String url, Map<String, String> headers, ContentType contentType) throws HttpProxyRequestException {
    	return putBody( "default",requestBody,   url,   headers,  contentType) ;
        
    }
    
    

    public static <T> T putJson(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
       return putJson(  poolname,  requestBody,   url,   headers, ContentType.APPLICATION_JSON,  responseHandler);
    }
    
    public static String putJson(String poolname, String requestBody, String url, Map<String, String> headers, ContentType contentType) throws HttpProxyRequestException {
    	return putJson(  poolname,  requestBody,   url, headers, ContentType.APPLICATION_JSON);
        
    }
    
    public static <T> T putJson(String requestBody, String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return putBody( "default", requestBody,   url,  headers,   ContentType.APPLICATION_JSON,  responseHandler) ;
    }

    public static <T> T putJson(String poolName, String requestBody, String url, Map<String, String> headers, ResponseHandler<T> responseHandler) throws HttpProxyRequestException {
        return putBody( poolName, requestBody,   url,  headers,   ContentType.APPLICATION_JSON,  responseHandler) ;
    }


    public static String putJson(String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
    	return putBody( "default",requestBody,   url,   headers,  ContentType.APPLICATION_JSON) ;
        
    }

    public static String putJson(String poolName, String requestBody, String url, Map<String, String> headers) throws HttpProxyRequestException {
        return putBody(poolName,requestBody,   url,   headers,  ContentType.APPLICATION_JSON) ;

    }
    
    

}
