#!/usr/bin/env python3

import yaml
import sys

specs = dict
filepath = sys.argv[1]
image_name = sys.argv[2]

def create_header():
    with open("whanos.deployment.yaml", 'w') as deployment:
        deployment.write("apiVersion: apps/v1\nkind: Deployment\nmetadata:\n  name: whanos-deployment\n  labels:\n    app: whanos\n")
    pass

def parse_deployment_config(config: str):
    global specs

    with open(config, 'r') as cf:
        specs = yaml.safe_load(cf)

def deployment_specs():
    global specs

    if specs.get("deployment") is not None:
        with open("whanos.deployment.yaml", 'a') as deployment:
            deployment.write(f"spec:\n  replicas: {specs['deployment']['replicas'] if specs.get('deployment').get('replicas') else 0}\n")
            deployment.write(f"  selector:\n    matchLabels:\n      app: whanos\n  template:\n")
            deployment.write(f"    metadata:\n      labels:\n        app: whanos\n    spec:\n      containers:\n      - name: whanos-compatible-app\n")
            deployment.write(f"        image: localhost:5000/{image_name}:latest\n")
            if specs.get("deployment").get("resources"):
                deployment.write(f"        resources:\n")
                deployment.write(f"          limits:\n            memory: {specs['deployment']['resources']['limits']['memory']}\n")
                deployment.write(f"          requests:\n            memory: {specs['deployment']['resources']['requests']['memory']}\n")
            if specs.get("deployment").get("ports"):
                deployment.write(f"        ports:\n        - containerPort: {specs['deployment']['ports'][0]}\n")

    else:
        sys.stdout.write("Not a deployment ! Aborting...")

def footer():
    pass

def main():
    create_header()
    parse_deployment_config(filepath)
    deployment_specs()
    print(specs)

if __name__ == "__main__":
    main()