import fetpapi

class MyOwnClientCoreHandlers(fetpapi.CoreHandlers):

    def __init__(self, my_session):
        fetpapi.CoreHandlers.__init__(self, my_session)

    def on_OpenSession(self, os, correlation_id):
        print("Opened a session")

        print("Asking all dataobjects")
        mb = fetpapi.GetResources()
        mb.context.uri = "eml:///"
        mb.context.depth = 1
        mb.scope = fetpapi._self
        mb.countObjects = True
        self.getSession().send(mb, 0, 0x02)
