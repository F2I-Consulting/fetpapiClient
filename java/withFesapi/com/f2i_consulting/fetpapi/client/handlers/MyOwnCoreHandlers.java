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

import java.util.Scanner;

import com.f2i_consulting.fesapi.SWIGTYPE_p_double;
import com.f2i_consulting.fesapi.fesapi;
import com.f2i_consulting.fesapi.common.DataObjectRepository;
import com.f2i_consulting.fesapi.resqml2.PointSetRepresentation;
import com.f2i_consulting.fetpapi.Energistics.Etp.v12.Datatypes.Object.ContextScopeKind;
import com.f2i_consulting.fetpapi.Energistics.Etp.v12.Protocol.Core.OpenSession;
import com.f2i_consulting.fetpapi.Energistics.Etp.v12.Protocol.Discovery.GetResources;
import com.f2i_consulting.fetpapi.etp.AbstractSession;
import com.f2i_consulting.fetpapi.etp.CoreHandlers;

public class MyOwnCoreHandlers extends CoreHandlers {
	private DataObjectRepository repo_;

	public MyOwnCoreHandlers(AbstractSession mySession, DataObjectRepository repo) {
		super(mySession);
		repo_ = repo;
	}

	class UserQuestions implements Runnable {

		@Override
		public void run() {
			System.out.println("What is your command (\\\"quit\\\" for closing connection)?");

			String command ="";
			long msgId = 0;
			while (!"quit".equals(command))
			{
				command = new Scanner(System.in).nextLine();  // Read user input

				if (command.isEmpty()) {
					System.out.println("List of available commands :");
					System.out.println("\tGetAll" + System.lineSeparator() + "\t\tGet all the dataobjetcs from the server." + System.lineSeparator());
					System.out.println("\tGetXyz" + System.lineSeparator() + "\t\tGet the XYZ points of the first point set rep of the repo." + System.lineSeparator());
					continue;
				}
				if ("quit".equals(command)) {
					continue;
				}

				if ("GetAll".equals(command)) {				
					System.out.println("Asking all dataobjects");
					GetResources mb = new GetResources();
					mb.getContext().setUri("eml:///");
					mb.getContext().setDepth(1);
					mb.setScope(ContextScopeKind.self);
					mb.setCountObjects(true);

					msgId = getSession().send(mb, 0, 0x02);
				}
				else if ("GetXyz".equals(command)) {
					if (msgId == 0) {
						System.out.println("You must first get the dataobjects");
						continue;
					}
					while (getSession().isMessageStillProcessing(msgId)) {}

					PointSetRepresentation psRep = repo_.getPointSetRepresentation(0);
					long pointCount = psRep.getXyzPointCountOfAllPatches();
					SWIGTYPE_p_double nativePoints = fesapi.new_DoubleArray(pointCount*3);
					try {
						psRep.getXyzPointsOfAllPatches(nativePoints);
						for (int ptIndex = 0; ptIndex < pointCount; ptIndex++) {
							System.out.println("X=" + Double.toString(fesapi.DoubleArray_getitem(nativePoints, ptIndex*3)) +
									"Y=" + Double.toString(fesapi.DoubleArray_getitem(nativePoints, ptIndex*3 + 1)) +
									"Z=" + Double.toString(fesapi.DoubleArray_getitem(nativePoints, ptIndex*3 + 2)));
						}
					}
					finally {
						fesapi.delete_DoubleArray(nativePoints);
					}
				}
			}

			long hdfProxyCount = repo_.getHdfProxyCount();
			for (long i = 0; i < hdfProxyCount; i++) {
				repo_.getHdfProxy(i).close();
			}

			System.out.println("Asking to close");
			getSession().close();
		}

	}

	@Override
	public void on_OpenSession(OpenSession os, long correlationId) {
		System.out.println("Opened a session");

		Thread th = new Thread(new UserQuestions());
		th.start();
	}
}
