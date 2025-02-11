package casia.isi.component.http;
/**
 * Copyright 2008
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/19 15:25
 * @author
 * @version 1.0
 */
public class HttpRequestProxyTest {
	@Before
	public void startPool(){
//		HttpRequestProxy.startHttpPools("application.properties");
		/**
		 * 1.服务健康检查
		 * 2.服务负载均衡
		 * 3.服务容灾故障恢复
		 * 4.服务自动发现（zk，etcd，consul，eureka，db，其他第三方注册中心）
		 * 配置了两个连接池：default,report
		 */
		Map<String,Object> configs = new HashMap<String,Object>();
		configs.put("http.poolNames","default,report");
//		configs.put("http.poolNames","report");
//		configs.put("report.http.health","/health");//health监控检查地址必须配置，否则将不会启动健康检查机制
//		configs.put("report.http.discoverService","org.frameworkset.http.client.DemoHttpHostDiscover");
		configs.put("http.health","/health");//health监控检查地址必须配置，否则将不会启动健康检查机制
		/**
		# 指定本地区信息，系统按地区部署时，指定地区信息，
		# 不同的地区请求只路由到本地区（beijing）对应的服务器，shanghai的服务器作为backup服务器，
		# 当本地(beijing)的服务器都不可用时，才将请求转发到可用的上海服务器
		 */
		configs.put("http.routing","beijing");//指定
//		configs.put("http.hosts","1111:90222,http://1111:90222,https://1111:90222");//health监控检查地址必须配置，否则将不会启动健康检查机制
		DemoHttpHostDiscover demoHttpHostDiscover = new DemoHttpHostDiscover();
		configs.put("http.discoverService",demoHttpHostDiscover);


		configs.put("report.http.health","/health");//health监控检查地址必须配置，否则将不会启动健康检查机制
//		configs.put("report.http.hosts","1111:90222,http://1111:90222,https://1111:90222");//health监控检查地址必须配置，否则将不会启动健康检查机制
		configs.put("report.http.discoverService","org.frameworkset.http.client.DemoHttpHostDiscover");
		/**
		 # 指定本地区信息，系统按地区部署时，指定地区信息，
		 # 不同的地区请求只路由到本地区（beijing）对应的服务器，shanghai的服务器作为backup服务器，
		 # 当本地(beijing)的服务器都不可用时，才将请求转发到可用的上海服务器
		 */
		configs.put("report.http.routing","beijing");//指定
		HttpRequestProxy.startHttpPools(configs);
	}
	@Test
	public void testGet(){
		String data = HttpRequestProxy.httpGetforString("report","/testBBossIndexCrud");
		System.out.println(data);
		do {
			try {
				data = HttpRequestProxy.httpGetforString("report","/testBBossIndexCrud");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				Thread.sleep(3000l);
			} catch (Exception e) {
				break;
			}
			try {
				data = HttpRequestProxy.httpGetforString("/testBBossIndexCrud");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				data = HttpRequestProxy.httpGetforString("/testBBossIndexCrud");
			} catch (Exception e) {
				e.printStackTrace();
			}
//			break;
		}
		while(true);
	}
}
