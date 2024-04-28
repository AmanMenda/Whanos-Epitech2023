Since Ansible is a powerful tool for VM configuration, we utilized its capabilities to effectively deploy our local infrastructure on an Azure VM. Our deployment process involved several steps to ensure smooth and efficient setup.

To begin, we focused on installing the necessary system packages on the Azure VM. These packages included **ca-certificates**, **curl**, **software-properties-common**, **python3-pip**, **python3-setuptools**, **docker**, and its Python module, along with **docker-compose**. By installing these packages, we ensured that our VM had all the required dependencies to support our infrastructure.

Once the system packages were successfully installed, we proceeded to clone our repository at ***/home/azureuser/whanos***. This step allowed us to access the necessary code and configuration files for our infrastructure setup. With the repository in place, we moved on to the next crucial step.

To launch our services, specifically Jenkins and the registry, we utilized the power of docker-compose. By running the command `docker-compose up --build`, we were able to initiate the process of building and starting the required containers. This step ensured that our services were up and running, ready to serve our infrastructure needs.

With Jenkins and the registry successfully launched, we reached the final step of our deployment process. It involved making port 8080 available using the public IP address. This step was crucial as it allowed external access to our infrastructure, enabling users to interact with Jenkins and utilize its capabilities seamlessly.

By following this comprehensive deployment process, we were able to leverage Ansible's strengths and successfully deploy our local infrastructure on an Azure VM. This deployment empowered us to efficiently manage and scale our infrastructure while ensuring its accessibility to external users.

In order to use our infra, please visit [51.103.217.10:8080](https://www.notion.so/Whanos-Doc-5053bbea7ab443d8942614acbad27495?pvs=21). In the link project job, you can specify both public and private repository url using the ssh method