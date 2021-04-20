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
package com.f2i_consulting.fetpapi.client.handlers;

import com.f2i_consulting.fetpapi.MapStringString;
import com.f2i_consulting.fetpapi.ResourceVector;
import com.f2i_consulting.fetpapi.Energistics.Etp.v12.Datatypes.Object.Resource;
import com.f2i_consulting.fetpapi.Energistics.Etp.v12.Protocol.Discovery.GetResourcesResponse;
import com.f2i_consulting.fetpapi.Energistics.Etp.v12.Protocol.Store.GetDataObjects;
import com.f2i_consulting.fetpapi.etp.AbstractSession;
import com.f2i_consulting.fetpapi.etp.DiscoveryHandlers;

public class MyOwnDiscoveryProtocolHandlers extends DiscoveryHandlers {
	public MyOwnDiscoveryProtocolHandlers(AbstractSession mySession) {
		super(mySession);
	}

	@Override
	public void on_GetResourcesResponse(GetResourcesResponse msg, long correlationId){
        MapStringString uriMap = new MapStringString();
        
		ResourceVector resources = msg.getResources();
		System.out.println(resources.size() + " resources received.");
		long index = 0;
		for (Resource resource : msg.getResources()) {
			System.out.println("*************************************************");
			System.out.println("uri : " + resource.getUri());
			System.out.println("data type : " + resource.getDataObjectType());
			System.out.println("name : " + resource.getName());
			System.out.println("*************************************************");
	        uriMap.put(Long.toString(index), resource.getUri());
	        index++;
		}
		
        GetDataObjects gdo = new GetDataObjects();
        gdo.setUris(uriMap);
        getSession().send(gdo, 0, 0x02);
	}
}
