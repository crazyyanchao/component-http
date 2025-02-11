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
import org.frameworkset.spi.assemble.GetProperties;

import java.util.List;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2019/6/20 12:41
 * @author
 * @version 1.0
 */
public class DefaultHttpHostDiscover extends HttpHostDiscover {
	@Override
	protected List<HttpHost> discover(HttpServiceHostsConfig httpServiceHostsConfig,
									  ClientConfiguration configuration,
									  GetProperties context) {
		return null;
	}
}
