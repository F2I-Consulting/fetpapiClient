# Included in ../CMakeLists.txt if csharp build is selected.

set (FETPAPI_ETP_NS etp CACHE STRING "Namespace for etp objects")

# Create the dedicated swig files
STRING(REGEX REPLACE "/" "\\\\" FETPAPI_CS_DIR ${FETPAPI_CS_DIR} )
configure_file(${CMAKE_SOURCE_DIR}/cmake/FetpapiClient.sln.template ${CMAKE_SOURCE_DIR}/cs/FetpapiClient.sln) # Overwrite if different
configure_file(${CMAKE_SOURCE_DIR}/cmake/FetpapiClient.csproj.template ${CMAKE_SOURCE_DIR}/cs/FetpapiClient.csproj) # Overwrite if different
configure_file(${CMAKE_SOURCE_DIR}/cmake/FetpapiClient.cs ${CMAKE_SOURCE_DIR}/cs/src/FetpapiClient.cs) # Overwrite if different
configure_file(${CMAKE_SOURCE_DIR}/cmake/MyOwnClientCoreHandlers.cs ${CMAKE_SOURCE_DIR}/cs/src/MyOwnClientCoreHandlers.cs) # Overwrite if different
configure_file(${CMAKE_SOURCE_DIR}/cmake/MyOwnDiscoveryProtocolHandlers.cs ${CMAKE_SOURCE_DIR}/cs/src/MyOwnDiscoveryProtocolHandlers.cs) # Overwrite if different
configure_file(${CMAKE_SOURCE_DIR}/cmake/MyOwnStoreProtocolHandlers.cs ${CMAKE_SOURCE_DIR}/cs/src/MyOwnStoreProtocolHandlers.cs) # Overwrite if different
