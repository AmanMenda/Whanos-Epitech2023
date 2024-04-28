FROM jenkins/jenkins:lts

USER root

# install docker cli
RUN apt-get -y update; apt-get install -y sudo; apt-get install -y git wget
RUN echo "Jenkins ALL=NOPASSWD: ALL" >> /etc/sudoers
RUN wget http://get.docker.com/builds/Linux/x86_64/docker-latest.tgz
RUN tar -xvzf docker-latest.tgz
RUN cp -r docker/* /usr/bin/

#install kubectl
RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
RUN chmod +x ./kubectl
RUN mv ./kubectl /usr/local/bin

#install python and yaml module
RUN apt-get update
RUN apt-get install -y python3 python3-pip
RUN apt-get install -y python3-yaml

#install Minikube binary
# RUN curl -LO https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
# RUN install minikube-linux-amd64 /usr/local/bin/minikube

#install minikube ssh dependencies
# RUN apt-get -y update && apt-get -y install conntrack iptables containernetworking-plugins systemd sysvinit-utils
# RUN wget https://github.com/kubernetes-sigs/cri-tools/releases/download/v1.28.0/crictl-v1.28.0-linux-amd64.tar.gz
# RUN tar zxvf crictl-v1.28.0-linux-amd64.tar.gz -C /usr/local/bin
# RUN wget https://github.com/Mirantis/cri-dockerd/releases/download/v0.2.0/cri-dockerd-v0.2.0-linux-amd64.tar.gz
# RUN tar xvf cri-dockerd-v0.2.0-linux-amd64.tar.gz
# RUN mv ./cri-dockerd /usr/local/bin/
# RUN curl -LO "https://github.com/containernetworking/plugins/releases/download/v1.4.0/cni-plugins-linux-amd64-v1.4.0.tgz"
# RUN mkdir -p "/opt/cni/bin"
# RUN tar xzvf cni-plugins-linux-amd64-v1.4.0.tgz -C /opt/cni/bin
# RUN rm cni-plugins-linux-amd64-v1.4.0.tgz
# RUN mkdir -p /etc/docker/ && touch /etc/docker/daemon.json


#install kind
RUN [ $(uname -m) = x86_64 ] && curl -Lo ./kind https://kind.sigs.k8s.io/dl/v0.20.0/kind-linux-amd64
RUN chmod +x ./kind
RUN mv ./kind /usr/local/bin/kind
# RUN kind create cluster --name kind-clus
# RUN kind get clusters

USER jenkins
# Copy the VM ssh key into container
# COPY vm_key /var/jenkins_home/.ssh/id_rsa
# RUN cat /var/jenkins_home/.ssh/id_rsa
# Create minikube cluster
# RUN minikube start --driver=ssh MinikubeSwitch --ssh-ip-address=51.103.217.10 --ssh-user=azureuser

ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false"
ENV CASC_JENKINS_CONFIG /var/jenkins_home/whanos.yaml
COPY images/ /images
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
COPY whanos.txt /images
COPY create_deployment.py /var/jenkins_home/create_deployment.py
COPY job_dsl.groovy /var/jenkins_home/job_dsl.groovy
COPY whanos.yaml /var/jenkins_home/whanos.yaml