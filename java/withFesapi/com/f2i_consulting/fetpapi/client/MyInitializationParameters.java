package com.f2i_consulting.fetpapi.client;

import com.f2i_consulting.fesapi.common.DataObjectRepository;
import com.f2i_consulting.fetpapi.client.handlers.MyOwnCoreHandlers;
import com.f2i_consulting.fetpapi.client.handlers.MyOwnDiscoveryProtocolHandlers;
import com.f2i_consulting.fetpapi.client.handlers.MyOwnStoreProtocolHandlers;
import com.f2i_consulting.fetpapi.etp.AbstractSession;
import com.f2i_consulting.fetpapi.etp.InitializationParameters;

public class MyInitializationParameters extends InitializationParameters {
	private DataObjectRepository repo_;
	
	public MyInitializationParameters(String host, int port, DataObjectRepository repo) {
		super("37554bda-ed4f-43bf-9fa8-21630eda223f", host, port);
		repo_ = repo;
	}

	@Override
	public String getApplicationName() {
		return "F2I JAVA ETP CLIENT powered by FESAPI";
	}
	
	@Override
	public void postSessionCreationOperation(AbstractSession session) {
		session.setCoreProtocolHandlers(new MyOwnCoreHandlers(session, repo_));
		session.setDiscoveryProtocolHandlers(new MyOwnDiscoveryProtocolHandlers(session));
		session.setStoreProtocolHandlers(new MyOwnStoreProtocolHandlers(session, repo_));
	}
}
