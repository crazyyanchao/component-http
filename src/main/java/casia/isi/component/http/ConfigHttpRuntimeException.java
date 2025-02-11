/*
 *  Copyright 2008
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package casia.isi.component.http;

/**
 * <p>Title: HttpRuntimeException.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2007</p>
 * @Date 2010-9-16 下午12:33:15
 * @author
 * @version 1.0
 */
public class ConfigHttpRuntimeException extends HttpRuntimeException {


	public ConfigHttpRuntimeException(){

	}


	public ConfigHttpRuntimeException(String message, Throwable cause) {
		super(message, cause,-1);

	}

	public ConfigHttpRuntimeException(String message ) {
		super(message,-1);

	}

	public ConfigHttpRuntimeException(Throwable cause) {
		super(cause,-1);
	}


}
