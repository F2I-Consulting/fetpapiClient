import fetpapi

class MyOwnStoreProtocolHandlers(fetpapi.StoreHandlers):

    def __init__(self, my_session):
        fetpapi.StoreHandlers.__init__(self, my_session)

    def on_GetDataObjectsResponse(self, msg, correlation_id):
        print(type(msg.dataObjects))
        for dataObj in msg.dataObjects.values():
            print("*************************************************")
            print("Resource received : ")
            print("uri : ", dataObj.resource.uri)
            print("datatype : ", dataObj.resource.dataObjectType)
            print("name : ", dataObj.resource.name)
            print("xml : ", dataObj.data)
            print("*************************************************")

        print("Asking to close")
        self.getSession().close()
