# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"; you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
# http://www.apache.org/licenses/LICENSE-2.0
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.

FROM centos:7.5.1804

LABEL maintainer="philippe.verney@f2i-consulting.com"

ENV CFLAGS="-fPIC -O2"
ENV CXXFLAGS="-fPIC -O2"

# RUN lance les commandes
RUN	yum install -y \
	# git is used to clone GitHub repositories \
	git \
	# C and C++ compilers
	gcc-c++ \
	# make is used to process Unix Makefiles
	make \
	# wget is used to download ressources. We use it instead of ADD in order to
	# minimize the size of the docker image by limitating the number of layers
	wget &&\
yum clean all && \
\
# epel repository is used to get both boost169 and cmake3 packages
wget http://dl.fedoraproject.org/pub/epel/epel-release-latest-7.noarch.rpm && \
rpm -ivh epel-release-latest-7.noarch.rpm && \
rm -f epel-release-latest-7.noarch.rpm && \
yum --enablerepo=epel install -y \
	# cmake3 is used to automatize building configuration
	cmake3 \
	# boost is a dependence of both avro and fesapi etp
	boost169-static && \
\

##############
# avro install
# avro is a dependence of fetpapiClient
mkdir fetpapiEnv && \
cd fetpapiEnv && \
mkdir dependencies && \
cd dependencies && \
wget https://apache.mirrors.benatherton.com/avro/stable/cpp/avro-cpp-1.10.1.tar.gz && \
tar xf avro-cpp-1.10.1.tar.gz && \
cd avro-cpp-1.10.1 && \
mkdir build && \
cd build && \
cmake3 -G "Unix Makefiles" -DBOOST_INCLUDEDIR=/usr/include/boost169/ -DBOOST_LIBRARYDIR=/usr/lib64/boost169 .. && \
make install && \
\
################
# fetpapi install
cd ../../.. && \
git clone https://github.com/F2I-Consulting/fetpapi.git && \
cd fetpapi && \
cd .. && \
mkdir fetpapi-build && \
cd fetpapi-build && \
cmake3 \
	-DBoost_INCLUDE_DIR=/usr/include/boost169 \
	-DBOOST_LIBRARYDIR=/usr/lib64/boost169 \
    -DAVRO_INCLUDE_DIR=/usr/local/include/avro \
    -DAVRO_LIBRARY_RELEASE=/usr/local/lib/libavrocpp_s.a \
	-DCMAKE_BUILD_TYPE=Release \
	-DBoost_USE_STATIC_LIBS=ON \
	-DBoost_USE_DEBUG_LIBS=OFF \
	-DBoost_USE_RELEASE_LIBS=ON \
	-DBoost_USE_MULTITHREADED=ON \
	-DBoost_USE_STATIC_RUNTIME=OFF \
	../fetpapi && \
make VERBOSE=OFF && \
make install && \
\
#######################
# fetpapiClient install
cd  .. && \
git clone https://github.com/F2I-Consulting/fetpapiClient.git && \
cd fetpapiClient && \
cd .. && \
mkdir fetpapiClient-build && \
cd fetpapiClient-build && \
cmake3 \
	-DFetpapi_DIR=../fetpapi-build/install/lib64/cmake/Fetpapi \
	-DBoost_INCLUDE_DIR=/usr/include/boost169 \
	-DBOOST_LIBRARYDIR=/usr/lib64/boost169 \
	-DAVRO_INCLUDE_DIR=/usr/local/include/avro \
	-DAVRO_LIBRARY_RELEASE=/usr/local/lib/libavrocpp_s.a \
	-DCMAKE_BUILD_TYPE=Release \
	-DBoost_USE_STATIC_LIBS=ON \
	-DBoost_USE_DEBUG_LIBS=OFF \
	-DBoost_USE_RELEASE_LIBS=ON \
	-DBoost_USE_MULTITHREADED=ON \
	-DBoost_USE_STATIC_RUNTIME=OFF \
	../fetpapiClient && \
make VERBOSE=OFF && \
make install && \
\
##########
# cleaning 
cd .. && \
rm -rf dependencies && \
yum install -y yum-plugin-remove-with-leaves && \
yum remove -y \
	cmake3 \
	boost169-static \
	git \
	gcc-c++ \
	make \
	wget \
	--remove-leaves && \
yum remove -y yum-plugin-remove-with-leaves && \
yum clean all

ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/fetpapiEnv/fetpapi-build/install/lib64/:/usr/local/lib/

WORKDIR /fetpapiEnv/fetpapiClient-build/install

# note from https://docs.docker.com/engine/reference/builder/ 
# The EXPOSE instruction informs Docker that the container listens on the specified 
# network ports at runtime. You can specify whether the port listens on TCP or UDP, 
# and the default is TCP if the protocol is not specified.
# The EXPOSE instruction does not actually publish the port. It functions as a type
# of documentation between the person who builds the image and the person who runs
# the container, about which ports are intended to be published. To actually publish
# the port when running the container, use the -p flag on docker run to publish and 
# map one or more ports, or the -P flag to publish all exposed ports and map them to
# high-order ports.
#
# in order to run the container on your docker desktop use, please use:
# docker run -p 80:80 [IMAGE]
EXPOSE 80 443

# setting command to launch at runtime
ENTRYPOINT ["./fetpapiClient"]
