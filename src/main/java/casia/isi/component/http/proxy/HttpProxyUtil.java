package casia.isi.component.http.proxy;
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

import casia.isi.component.http.ClientConfiguration;
import casia.isi.component.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>Description: 从外部服务注册中心监听服务地址变更后调用本组件handleDiscoverHosts方法刷新负载组件地址清单</p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/20 12:47
 * @author
 * @version 1.0
 */
public class HttpProxyUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpProxyUtil.class);

	/**
	 *
	 * @param poolName
	 * @param hosts
	 */
	public static void handleDiscoverHosts(String poolName, List<HttpHost> hosts){
		if(poolName == null)
			poolName = "default";
		try {
			ClientConfiguration clientConfiguration = ClientConfiguration.getClientConfiguration(poolName);
			if (clientConfiguration != null) {
				HttpHostDiscover httpHostDiscover = null;
				HttpServiceHosts httpServiceHosts = clientConfiguration.getHttpServiceHosts();
				if (httpServiceHosts != null) {
					httpHostDiscover = httpServiceHosts.getHostDiscover();
					if (httpHostDiscover == null) {
						if (logger.isInfoEnabled()) {//Registry default HttpHostDiscover
							logger.info("Registry default HttpHostDiscover to httppool[{}]", poolName);
						}
						synchronized (HttpProxyUtil.class) {
							httpHostDiscover = httpServiceHosts.getHostDiscover();
							if (httpHostDiscover == null) {
								httpHostDiscover = new DefaultHttpHostDiscover();
								httpHostDiscover.setHttpServiceHosts(httpServiceHosts);
								httpServiceHosts.setHostDiscover(httpHostDiscover);
							}
						}
					}
					if (httpHostDiscover != null) {
						if (hosts == null || hosts.size() == 0) {
							Boolean handleNullOrEmptyHostsByDiscovery = httpHostDiscover.handleNullOrEmptyHostsByDiscovery();
							if (handleNullOrEmptyHostsByDiscovery == null) {
								handleNullOrEmptyHostsByDiscovery = httpServiceHosts.getHandleNullOrEmptyHostsByDiscovery();
							}
							if (handleNullOrEmptyHostsByDiscovery == null || !handleNullOrEmptyHostsByDiscovery) {
								if (logger.isInfoEnabled())
									logger.info(new StringBuilder().append("Discovery ")
											.append(httpServiceHosts.getClientConfiguration().getBeanName()).append(" servers : ignore with httpHosts == null || httpHosts.size() == 0").toString());
								return;
							}
						}
						httpHostDiscover.handleDiscoverHosts(hosts);
					}
				}
			}
		} catch (Exception e) {
			if (logger.isInfoEnabled())
				logger.info(new StringBuilder().append("Discovery ")
						.append(poolName).append(" servers failed:").toString(),e);
		}


	}
//	public static HttpHostDiscover getHttpHostDiscover(String poolName){
//		ClientConfiguration clientConfiguration = ClientConfiguration.getClientConfiguration(poolName);
//		if (clientConfiguration != null){
//			HttpServiceHosts httpServiceHosts = clientConfiguration.getHttpServiceHosts();
//			return httpServiceHosts != null?httpServiceHosts.getHostDiscover():null;
//		}
//		return null;
//	}

}
