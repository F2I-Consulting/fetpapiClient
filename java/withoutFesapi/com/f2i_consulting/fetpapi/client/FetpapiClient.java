/*-----------------------------------------------------------------------
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"; you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-----------------------------------------------------------------------*/
package com.f2i_consulting.fetpapi.client;

import com.f2i_consulting.fetpapi.fetpapi;
import com.f2i_consulting.fetpapi.etp.PlainClientSession;

public class FetpapiClient {

	/**
	 * Loading the FesapiCpp native library
	 */
	static {
		try {
			System.loadLibrary("Fetpapi.0.1.0.0");
		}
		catch (UnsatisfiedLinkError e) {
			System.out.println("UnsatisfiedLinkError : " + e.toString());
		}
	}

	public static void main(String[] args) {
		MyInitializationParameters initParam = new MyInitializationParameters("etp.f2i-consulting.com", 80);
		PlainClientSession session = fetpapi.createWsClientSession(initParam, "/", "");
		session.run();
		System.out.println("FINISHED");
	}
}
