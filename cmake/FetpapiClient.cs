using System;

using F2iConsulting.Fetpapi;
using F2iConsulting.Fetpapi.${FETPAPI_ETP_NS};

namespace FetpapiClient
{
    static class Program
    {
        static void Main(string[] args)
        {
            PlainClientSession session = fetpapi.createWsClientSession("127.0.0.1", "8080", "/", "");
            session.setCoreProtocolHandlers(new MyOwnClientCoreHandlers(session));
            session.setDiscoveryProtocolHandlers(new MyOwnDiscoveryProtocolHandlers(session));
            session.setStoreProtocolHandlers(new MyOwnStoreProtocolHandlers(session));
            session.run();
            Console.WriteLine("FINISHED");
        }
    }
}
