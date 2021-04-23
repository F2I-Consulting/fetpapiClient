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
#pragma once

#include <fetpapi/etp/InitializationParameters.h>

#include <fetpapi/etp/ProtocolHandlers/DataArrayHandlers.h>
#include <fetpapi/etp/ProtocolHandlers/StoreNotificationHandlers.h>
#include <fetpapi/etp/ProtocolHandlers/DataspaceHandlers.h>
#include <fetpapi/etp/ProtocolHandlers/TransactionHandlers.h>

#include "MyOwnCoreProtocolHandlers.h"
#include "MyOwnDiscoveryProtocolHandlers.h"
#include "MyOwnStoreProtocolHandlers.h"

class MyInitializationParameters final : public ETP_NS::InitializationParameters
{
private:
	COMMON_NS::DataObjectRepository* repo_;

public:
	MyInitializationParameters(COMMON_NS::DataObjectRepository* repo, boost::uuids::uuid instanceUuid, const std::string & host, unsigned short port) : ETP_NS::InitializationParameters(instanceUuid, host, port), repo_(repo) {}
	~MyInitializationParameters() = default;

	void postSessionCreationOperation(ETP_NS::AbstractSession* session) const final {
		session->setCoreProtocolHandlers(std::make_shared<MyOwnCoreProtocolHandlers>(session, repo_));
		session->setDiscoveryProtocolHandlers(std::make_shared<MyOwnDiscoveryProtocolHandlers>(session, repo_));
		session->setStoreProtocolHandlers(std::make_shared<MyOwnStoreProtocolHandlers>(session, repo_));
		session->setDataArrayProtocolHandlers(std::make_shared<ETP_NS::DataArrayHandlers>(session));
		session->setStoreNotificationProtocolHandlers(std::make_shared<ETP_NS::StoreNotificationHandlers>(session));
		session->setDataspaceProtocolHandlers(std::make_shared<ETP_NS::DataspaceHandlers>(session));
		session->setTransactionProtocolHandlers(std::make_shared<ETP_NS::TransactionHandlers>(session));
	}
};
