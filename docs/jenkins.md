**What is the role of Jenkins in our infrastructure?**

Jenkins plays a crucial role in automating our CI/CD process and interacting with the Kubernetes cluster. It serves as the backbone of our infrastructure, allowing us to efficiently build, test, and deploy our applications. Our Jenkins instance runs in a Docker container via Docker Compose, providing scalability and portability. In our infrastructure, it is crucial to understand that we have chosen to run our Jenkins instance in a Docker container. This decision provides us with several benefits, including scalability and portability. To achieve this, we utilize docker-compose, a tool that allows us to define and manage multi-container Docker applications.

To bring up our Jenkins instance within the Docker container, we use the trivial command `docker-compose up --build`. This command triggers the build process, ensuring that the latest changes and dependencies are incorporated into the container. By running Jenkins in a Docker container, we are able to easily manage and scale our CI/CD process, as well as interact with the Kubernetes cluster.

**How did we configure our Jenkins instance ?**

We utilized Jenkins' Configuration-as-Code plugin to create an Admin user. Additionally, we configured the SSH key for GitHub to work with a private repository. The sensitive information, such as the private key and passwords, were stored in a .env file.

To preserve the formatting of the SSH key while copying its content into the .env file, we used the following command: echo "SSH_PRIVATE_KEY=\"`sed -E 's/$/\\\\\\\\\\\\n/g' ~/.ssh/id_rsa`\"" >> .env.

Regarding the verification of the Git host key, we opted for the "acceptFirstConnectionStrategy" as it aligned best with our objectives.

**The Jobs:**

In the section dedicated to job configuration, we would like to showcase our plugin file. This file serves as a comprehensive reference, outlining all the essential plugins and dependencies that our Groovy script relies on. By clearly defining these plugins and dependencies, we ensure that our Jenkins job runs smoothly and efficiently, without any missing or incompatible components.

We have created two jobs: one to build an image and another to build all base images. The main job is responsible for checking if there are any changes in the repository. We use a cron scheduler to periodically check the workspace for any changes. Since our source code management (SCM) is git, we need to make sure to reference the GitHub SSH key using the assigned ID in the jCasC file (namely “github-ci-key”).

Once changes are detected, we determine the language used in the repository, build the corresponding image, and push it to a registry. If there is a "whanos.yml" file in the repository, it indicates that we need to deploy the infrastructure. To accomplish this, we use a conditional step that triggers when the "whanos.yml" file exists. This step runs a Python script to generate the complete deployment configuration from the provided "whanos.yml" file and outputs it as "whanos.deployment.yml".

Finally, the generated deployment file is used with a service to deploy the app into a Kubernetes cluster.

Generated whanos.deployment.yaml

That’s it for the jenkins part.