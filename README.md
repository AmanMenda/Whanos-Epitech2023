# whanos

**Epitech DevOps Module Final Project**

*whanos* is a versatile and comprehensive CI/CD infrastructure designed to support multiple programming languages, including C, Java, JavaScript, Python, and Befunge. Built using a combination of Ansible, Docker, Kubernetes, and Jenkins, *whanos* provides a scalable, automated solution for deploying applications on an Azure-based environment.

## Table of Contents
- [Project Overview](#project-overview)
- [Infrastructure Setup](#infrastructure-setup)
- [Docker Configuration](#docker-configuration)
- [Jenkins CI/CD Integration](#jenkins-ci-cd-integration)
- [Kubernetes Cluster Management](#kubernetes-cluster-management)
- [Usage](#usage)

---

## Project Overview

*whanos* was designed to facilitate automated deployment, configuration management, and container orchestration for polyglot applications. Leveraging Ansible, Jenkins, Docker, and Kubernetes, this infrastructure enables streamlined VM configuration, service containerization, and CI/CD pipelines for seamless code delivery and deployment.

## Infrastructure Setup

To deploy the local infrastructure on an Azure VM, we utilized Ansible for streamlined configuration. The deployment involves these key steps:

1. **System Package Installation**:
   - Essential system packages were installed, including `ca-certificates`, `curl`, `software-properties-common`, `python3-pip`, `python3-setuptools`, `docker`, and `docker-compose`.
   - These packages provide the necessary dependencies for our infrastructure.

2. **Repository Cloning**:
   - The *whanos* repository was cloned into the `/home/azureuser/whanos` directory on the Azure VM, bringing in all necessary code and configuration files.

3. **Service Initialization**:
   - Jenkins and the Docker registry were set up using Docker Compose with the command `docker-compose up --build`.
   - This command starts the services, allowing the CI/CD process to begin.

4. **Port Configuration**:
   - Port 8080 was opened on the public IP to enable external access to Jenkins.

## Docker Configuration

Given the diversity of languages supported, *whanos* includes customized Docker files to manage dependencies and ensure consistent builds:

- **Dockerfile.base**: Builds the core dependencies for each supported language (e.g., GCC for C).
- **Dockerfile.standalone**: Builds the entire project when a *whanos-compatible* repository does not contain a Dockerfile at the root.

These Dockerfiles standardize build environments for supported languages, ensuring consistent deployment across projects.

## Jenkins CI CD Integration

Jenkins is at the core of *whanos*'s CI/CD pipeline, automating builds, testing, and deployment:

1. **Jenkins Containerization**:
   - Jenkins runs within a Docker container, deployed via Docker Compose. This setup provides portability and scalability for the CI/CD pipeline.
   - The instance is started using the command `docker-compose up --build`, which builds and runs the latest Jenkins container.

2. **Configuration-as-Code**:
   - We utilized the Configuration-as-Code (jCasC) plugin to create an admin user and configure SSH keys for GitHub integration.
   - Sensitive data, such as SSH keys and passwords, are stored securely in a `.env` file, and the SSH key is formatted using:
     ```bash
     echo "SSH_PRIVATE_KEY="$(sed -E 's/$/\\\\n/g' ~/.ssh/id_rsa)"" >> .env
     ```
   - For Git host key verification, we implemented the `acceptFirstConnectionStrategy`.

3. **Job Management**:
   - Two jobs were set up: one to build images and another to build all base images.
   - A cron scheduler checks for updates in the repository, and if changes are detected, the appropriate image is built and pushed to the Docker registry.
   - If a `whanos.yml` file is present in the repository, a Python script generates a complete deployment configuration (`whanos.deployment.yml`) to deploy the application within the Kubernetes cluster.

## Kubernetes Cluster Management

For Kubernetes management, we used the **Kind** tool to set up a Kubernetes cluster on the Azure VM:

1. **Cluster Creation**:
   - *Kind* allowed us to establish a local Kubernetes environment efficiently.

2. **kubectl Context Setup**:
   - We configured kubectl to interact with the cluster, enabling management of deployments, services, and pods.

3. **Application Deployment**:
   - After pushing the application image to the Kubernetes cluster, we could manage deployments directly from Jenkins, using the generated deployment file (`whanos.deployment.yml`).

## Usage

To interact with our infrastructure:

1. **Jenkins Interface**: Visit `http://51.103.217.10:8080` (currently inaccessible. There are no credits left on our VMs) and access the Jenkins dashboard to view jobs and monitor deployments.
2. **Linking Projects**:
   - In the Jenkins project job, specify the repository URLs (public or private) using SSH for secure access.

For more details, please consult the individual module files located in "docs".
