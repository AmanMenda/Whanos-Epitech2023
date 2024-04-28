In the Kubernetes section of our infrastructure, we opted to use the "Kind" tool to create a cluster on our Virtual Machine hosted on Azure. This decision was driven by the desire to have a reliable and efficient Kubernetes environment for our applications.

By using "Kind," we were able to easily set up and configure a Kubernetes cluster on our Azure VM. 

To ensure seamless integration with the Kubernetes cluster, we defined the context of kubectl on “kind” (the name of our deployed cluster). This step was essential as it enabled us to interact with the cluster using the familiar kubectl command-line tool. With the appropriate context configured, we were able to perform various operations on the cluster, such as managing deployments, services, and pods.

However, setting up kubectl and defining the context was not the final step in our Kubernetes journey. We still needed to address the important task of pushing our application image into the deployed cluster. This step was crucial for making our application available within the Kubernetes environment.