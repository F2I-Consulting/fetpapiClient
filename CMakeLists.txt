cmake_minimum_required(VERSION 3.5)

project(Fetpapi_Client)

set (APPLICATION_NAME fetpapiClient)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_POSITION_INDEPENDENT_CODE TRUE)

IF (CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)
	set (CMAKE_INSTALL_PREFIX ${CMAKE_CURRENT_BINARY_DIR}/install CACHE PATH "${LIBRARY_NAME} install prefix" FORCE)
ENDIF (CMAKE_INSTALL_PREFIX_INITIALIZED_TO_DEFAULT)

# required to define CMAKE_INSTALL_BINDIR, CMAKE_INSTALL_LIBDIR and CMAKE_INSTALL_INCLUDEDIR
include(GNUInstallDirs) 

add_executable (${APPLICATION_NAME} "")

# ============================================================================
# some useful variables
# ============================================================================

set (WITH_FESAPI OFF CACHE BOOL "Build Fetpapi with Fesapi support.")
# SSL

# ============================================================================
# checking for required dependencies
# ============================================================================

find_package(Fetpapi REQUIRED)

if (WIN32)
	target_link_libraries (${APPLICATION_NAME} PRIVATE bcrypt.lib)
	
	set_target_properties (${APPLICATION_NAME} PROPERTIES
		LINK_FLAGS "/INCREMENTAL:NO"
		RUNTIME_OUTPUT_DIRECTORY ${CMAKE_CURRENT_BINARY_DIR})
endif (WIN32)

target_compile_definitions(${APPLICATION_NAME} PRIVATE BOOST_ALL_NO_LIB)
target_link_libraries (${APPLICATION_NAME} PRIVATE ${Boost_SYSTEM_LIBRARY} ${CMAKE_THREAD_LIBS_INIT})
if (WIN32)
	if (EXISTS ${APPLICATION_NAME} AND EXISTS ${AVRO_LIBRARY_DEBUG})
		target_link_libraries (${APPLICATION_NAME} PRIVATE optimized ${AVRO_LIBRARY_RELEASE} debug ${AVRO_LIBRARY_DEBUG})
	elseif (EXISTS ${AVRO_LIBRARY_RELEASE})
		target_link_libraries (${APPLICATION_NAME} PRIVATE ${AVRO_LIBRARY_RELEASE})
	elseif (EXISTS ${AVRO_LIBRARY_DEBUG})
		target_link_libraries (${APPLICATION_NAME} PRIVATE ${AVRO_LIBRARY_DEBUG})
	endif ()
else (WIN32)
	target_link_libraries (${APPLICATION_NAME} PRIVATE ${AVRO_LIBRARY_RELEASE})
endif (WIN32)

# SSL

if (WITH_FESAPI)
	set (FESAPI_INCLUDE_DIR FESAPI_INCLUDE_DIR-NOTFOUND CACHE PATH "Path to the directory which contains the fesapi header files")
	if (NOT IS_DIRECTORY ${FESAPI_INCLUDE_DIR})
		message(WARNING "The Fesapi include dir (FESAPI_INCLUDE_DIR variable) does not look to be a valid directory. Please modify it.")
	endif ()
	set (FESAPI_LIBRARY_RELEASE FESAPI_LIBRARY_RELEASE-NOTFOUND CACHE FILEPATH "Path to the file which contains the fesapi library release")
	if (NOT EXISTS ${FESAPI_LIBRARY_RELEASE})
		message(WARNING "The fesapi library (FESAPI_LIBRARY_RELEASE variable) does not look to be a valid file. Please modify it.")
	endif ()
	set (FESAPI_LIBRARY_DEBUG FESAPI_LIBRARY_DEBUG-NOTFOUND CACHE FILEPATH "Path to the file which contains the fesapi library DEBUG")
	if (NOT EXISTS ${FESAPI_LIBRARY_DEBUG})
		message(WARNING "The fesapi library (FESAPI_LIBRARY_DEBUG variable) does not look to be a valid file. Please modify it.")
	endif ()
	
	if (WIN32)
		if (EXISTS ${FESAPI_LIBRARY_RELEASE} AND EXISTS ${FESAPI_LIBRARY_DEBUG})
			target_link_libraries (${APPLICATION_NAME} PRIVATE optimized ${FESAPI_LIBRARY_RELEASE} debug ${FESAPI_LIBRARY_DEBUG})
		elseif (EXISTS ${FESAPI_LIBRARY_RELEASE})
			target_link_libraries (${APPLICATION_NAME} PRIVATE ${FESAPI_LIBRARY_RELEASE})
		elseif (EXISTS ${FESAPI_LIBRARY_DEBUG})
			target_link_libraries (${APPLICATION_NAME} PRIVATE ${FESAPI_LIBRARY_DEBUG})
		endif ()
	else (WIN32)
		target_link_libraries (${APPLICATION_NAME} PRIVATE ${FESAPI_LIBRARY_RELEASE})
	endif (WIN32)
endif (WITH_FESAPI)

target_link_libraries(${APPLICATION_NAME} PRIVATE Fetpapi::Fetpapi)

# ============================================================================
# Getting source files
# ============================================================================

if (WITH_FESAPI)
	file (GLOB ALL_CPP_FILES ${CMAKE_CURRENT_SOURCE_DIR}/src/withFesapi/*.cpp)
	file (GLOB ALL_H_FILES ${CMAKE_CURRENT_SOURCE_DIR}/src/withFesapi/*.h)
	target_include_directories(${APPLICATION_NAME} PRIVATE ${FESAPI_INCLUDE_DIR})
else (WITH_FESAPI)
	file (GLOB ALL_CPP_FILES ${CMAKE_CURRENT_SOURCE_DIR}/src/withoutFesapi/*.cpp)
	file (GLOB ALL_H_FILES ${CMAKE_CURRENT_SOURCE_DIR}/src/withoutFesapi*.h)
endif (WITH_FESAPI)

target_sources(${APPLICATION_NAME} PRIVATE ${ALL_CPP_FILES} ${ALL_H_FILES})

target_include_directories(${APPLICATION_NAME} PRIVATE ${AVRO_INCLUDE_DIR} ${Boost_INCLUDE_DIR})

# organizing sources and headers in the Visual Studio Project
if (WIN32)
	source_group ("src" FILES ${ALL_CPP_FILES} ${ALL_H_FILES})
endif (WIN32)

# ============================================================================
# Install fetpapiClient
# ============================================================================

install (
	TARGETS ${APPLICATION_NAME}
	DESTINATION ${CMAKE_INSTALL_BINDIR}
)