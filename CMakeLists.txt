cmake_minimum_required(VERSION 3.5)

project(Fetpapi_Client)

set (APPLICATION_NAME fetpapiClient)

set(CMAKE_CXX_STANDARD 11)
set(CMAKE_CXX_STANDARD_REQUIRED ON)
set(CMAKE_CXX_EXTENSIONS OFF)
set(CMAKE_POSITION_INDEPENDENT_CODE TRUE)

add_executable (${APPLICATION_NAME} "")

# ============================================================================
# checking for required dependencies
# ============================================================================

find_package(Fetpapi REQUIRED)

if (WIN32)
	target_link_libraries (${APPLICATION_NAME} PRIVATE bcrypt.lib)
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

target_link_libraries(${APPLICATION_NAME} PRIVATE Fetpapi::Fetpapi)

# ============================================================================
# Getting source files
# ============================================================================

file (GLOB ALL_CPP_FILES ${CMAKE_CURRENT_SOURCE_DIR}/src/*.cpp)
file (GLOB ALL_H_FILES ${CMAKE_CURRENT_SOURCE_DIR}/src/*.h)

target_sources(${APPLICATION_NAME} PRIVATE ${ALL_CPP_FILES} ${ALL_H_FILES})

target_include_directories(${APPLICATION_NAME} PRIVATE ${AVRO_INCLUDE_DIR} ${Boost_INCLUDE_DIR})

# organizing sources and headers in the Visual Studio Project
if (WIN32)
	source_group ("src" FILES ${ALL_CPP_FILES} ${ALL_H_FILES})
endif (WIN32)
