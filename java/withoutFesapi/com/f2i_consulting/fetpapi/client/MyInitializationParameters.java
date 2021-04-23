package com.f2i_consulting.fetpapi.client;

import com.f2i_consulting.fetpapi.client.handlers.MyOwnClientCoreHandlers;
import com.f2i_consulting.fetpapi.client.handlers.MyOwnDiscoveryProtocolHandlers;
import com.f2i_consulting.fetpapi.client.handlers.MyOwnStoreProtocolHandlers;
import com.f2i_consulting.fetpapi.etp.AbstractSession;
import com.f2i_consulting.fetpapi.etp.InitializationParameters;

public class MyInitializationParameters extends InitializationParameters {	
	public MyInitializationParameters(String host, int port) {
		super("a12e80f2-18d6-4766-8f73-7cd373a29e7f", host, port);
	}

	@Override
	public String getApplicationName() {
		return "F2I JAVA ETP CLIENT not powered by FESAPI";
	}
	
	@Override
	public void postSessionCreationOperation(AbstractSession session) {
		session.setCoreProtocolHandlers(new MyOwnClientCoreHandlers(session));
		session.setDiscoveryProtocolHandlers(new MyOwnDiscoveryProtocolHandlers(session));
		session.setStoreProtocolHandlers(new MyOwnStoreProtocolHandlers(session));
	}
}
