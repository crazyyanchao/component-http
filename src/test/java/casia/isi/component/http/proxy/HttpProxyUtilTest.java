package casia.isi.component.http.proxy;

import casia.isi.component.http.ClientConfiguration;
import casia.isi.component.http.HttpHost;
import casia.isi.component.http.HttpRequestProxy;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
/**
 * 　　　　　　　 ┏┓       ┏┓+ +
 * 　　　　　　　┏┛┻━━━━━━━┛┻┓ + +
 * 　　　　　　　┃　　　　　　 ┃
 * 　　　　　　　┃　　　━　　　┃ ++ + + +
 * 　　　　　　 █████━█████  ┃+
 * 　　　　　　　┃　　　　　　 ┃ +
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　 ┃ + +
 * 　　　　　　　┗━━┓　　　 ┏━┛
 * ┃　　  ┃
 * 　　　　　　　　　┃　　  ┃ + + + +
 * 　　　　　　　　　┃　　　┃　Code is far away from     bug with the animal protecting
 * 　　　　　　　　　┃　　　┃ +
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃　　+
 * 　　　　　　　　　┃　 　 ┗━━━┓ + +
 * 　　　　　　　　　┃ 　　　　　┣┓
 * 　　　　　　　　　┃ 　　　　　┏┛
 * 　　　　　　　　　┗┓┓┏━━━┳┓┏┛ + + + +
 * 　　　　　　　　　 ┃┫┫　 ┃┫┫
 * 　　　　　　　　　 ┗┻┛　 ┗┻┛+ + + +
 */

/**
 * @author YanchaoMa yanchaoma@foxmail.com
 * @PACKAGE_NAME: casia.isi.component.http.proxy
 * @Description: TODO(Describe the role of this JAVA class)
 * @date 2019/12/18 16:49
 */
public class HttpProxyUtilTest {

    private final static String ipPorts="39.97.167.206:9210";

    @Before
    public void setUp() throws Exception {
        PropertyConfigurator.configureAndWatch("config" + File.separator + "log4j.properties");
    }

    @Test
    public void register() {
        register(ipPorts);
        discover(HttpPoolSym.DEFAULT, ipPorts);
    }

    @Test
    public void handleDiscoverHosts() {
        discover(HttpPoolSym.DEFAULT, ipPorts);
    }

    /**
     * @param ipPorts:用逗号分隔的多个:IP:PORT,IP:PORT,IP:PORT,IP:PORT
     * @return
     * @Description: TODO(加载Map属性配置启动负载均衡器)
     */
    public static void register(String ipPorts) {

        Map<String, Object> configs = new HashMap<>();
        configs.put("http.poolNames", HttpPoolSym.DEFAULT.getSymbolValue());

        /**
         * 设置服务发现组件 - 通过discoverService服务发现的地址都会加入到清单中
         *
         * **/
        configs.put("http.discoverService", new HttpDiscover());

        /**
         * health监控检查地址必须配置，否则将不会启动健康检查机制
         * 这个服务可以是一个静态图片或者html网页，也可以是一个自己实现的其他http服务（例如自己实现/health服务）
         *
         * **/
        configs.put("http.health", "/");

        /**
         * 注册地址
         *
         * **/
        configs.put("http.hosts", ipPorts.replace(" ", ""));
        HttpRequestProxy.startHttpPools(configs);
    }

    /**
     * @param httpPool:HTTP          POOL
     * @param ipPorts:逗号分隔的IP:PORT地址
     * @return
     * @Description: TODO(被动发现服务地址)
     */
    public static boolean discover(HttpSymbol httpPool, String ipPorts) {
        List<HttpHost> httpHosts = packHosts(ipPorts);

        /**
         * 被动发现模式：例如监听消息中间件等数据变化，适用于发布订阅模式
         *
         * **/
        HttpProxyUtil.handleDiscoverHosts(httpPool.name(), httpHosts);

        ClientConfiguration config = ClientConfiguration.getClientConfiguration(httpPool.name());

        HttpAddress httpAddress = config.getHttpServiceHosts().getHttpAddress();
        int status = httpAddress.getStatus();

        String[] arrayChar = httpAddress.getAddress().split("|");
        StringBuilder builder = new StringBuilder();
        for (String cypher : arrayChar) {
            builder.append(cypher);
        }
        String ipPort = builder.toString().replace("http://", "").replace("https://", "");
        return status == 0 && httpHosts.contains(new HttpHost(ipPort));
    }

    /**
     * @param ipPorts:逗号分隔的IP:PORT地址
     * @return
     * @Description: TODO(封装HTTP HOSTS)
     */
    public static List<HttpHost> packHosts(String ipPorts) {
        List<HttpHost> httpHosts = new ArrayList<>();
        String[] servers = ipPorts
                .replace(" ", "")
                .split(Symbol.COMMA_CHARACTER.getSymbolValue());

        for (int i = 0; i < servers.length; i++) {
            String server = servers[i];
            httpHosts.add(new HttpHost(server));
        }
        return httpHosts;
    }

    public enum Symbol {

//    SPACE_CHARACTER("&"),

        COMMA_CHARACTER(",");

        private String symbol;

        Symbol(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbolValue() {
            return this.symbol;
        }
    }


    public enum HttpPoolSym implements HttpSymbol {

        /**
         * 多个集群时才需要新增指定HTTP连接池
         * **/

        /**
         * 默认连接池
         */
        DEFAULT("default"),

        REPORT("report");

        private String symbol;

        HttpPoolSym(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbolValue() {
            return this.symbol;
        }

    }

    public interface HttpSymbol {
        /**
         * Returns the name of the label. The name uniquely identifies a
         * label, i.e. two different HttpSymbol instances with different object identifiers
         * (and possibly even different classes) are semantically equivalent if they
         * have {@link String#equals(Object) equal} names.
         *
         * @return the name of the label
         */
        String name();
    }

}