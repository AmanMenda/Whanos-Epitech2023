folder('Whanos base images') {
    description('Folder for our base images')
    
    languages = ["c", "java", "javascript", "python", "befunge"]

    for (language in languages) {
        job("Whanos base images/whanos-$language") {
            steps {
                shell("cat /images/whanos.txt; docker build -t whanos-$language -f /images/$language/Dockerfile.base /images/$language/")
            }
        }
    }

    job('Whanos base images/Build all base images') {
        steps {
            for (language in languages) {
                shell("docker build -t whanos-$language -f /images/$language/Dockerfile.base /images/$language/")
            }
        }
    }
}

folder('Projects') {
    description('Every jobs created by the link-project job')
    job('link-project') {
        parameters {
            stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
            stringParam('JOB_NAME', '', 'Name for the job which will be created')
        }
        steps {
            dsl {
                text('''
                job('Projects/' + JOB_NAME) {
                    triggers {
                        scm('* * * * *')
                    }
                    scm {
                        git {
                            remote {
                                name('origin')
                                url("$GIT_REPOSITORY_URL")
                                credentials('github-ci-key')
                            }
                            branch('main')
                        }
                    }
                    steps {
                        containerizationScript = """
                            if [ -f Dockerfile ]; then
                                docker build -t $JOB_NAME .
                                docker tag $JOB_NAME localhost:5000/$JOB_NAME
                                docker push localhost:5000/$JOB_NAME
                                docker image remove $JOB_NAME
                            elif [ -f Makefile ] && [ ! -f app/*.cpp ] && [ ! -f Dockerfile ]; then
                                cp /images/c/Dockerfile.standalone .
                                docker build -t $JOB_NAME -f ./Dockerfile.standalone .
                                docker tag $JOB_NAME localhost:5000/$JOB_NAME
                                docker push localhost:5000/$JOB_NAME
                                docker pull localhost:5000/$JOB_NAME
                                docker image remove $JOB_NAME
                            elif [ -f app/pom.xml ] && [ ! -f Dockerfile ]; then
                                cp /images/java/Dockerfile.standalone .
                                docker build -t $JOB_NAME -f ./Dockerfile.standalone .
                                docker tag $JOB_NAME localhost:5000/$JOB_NAME
                                docker push localhost:5000/$JOB_NAME
                                docker image remove $JOB_NAME
                            elif [ -f package.json ] && [ ! -f Dockerfile ]; then
                                cp /images/javascript/Dockerfile.standalone .
                                docker build -t $JOB_NAME -f ./Dockerfile.standalone .docker build -f /images/javascript/Dockerfile.standalone -t $JOB_NAME .
                                docker tag $JOB_NAME localhost:5000/$JOB_NAME
                                docker push localhost:5000/$JOB_NAME
                                docker image remove $JOB_NAME
                            elif [ -f requirements.txt ] && [ ! -f Dockerfile ]; then
                                cp /images/python/Dockerfile.standalone .
                                docker build -t $JOB_NAME -f ./Dockerfile.standalone .
                                docker tag $JOB_NAME localhost:5000/$JOB_NAME
                                docker push localhost:5000/$JOB_NAME
                                docker image remove $JOB_NAME
                            elif [ -f app/main.bf ] && [ ! -f Dockerfile ]; then
                                cp /images/befunge/Dockerfile.standalone .
                                docker build -t $JOB_NAME -f ./Dockerfile.standalone .
                                docker tag $JOB_NAME localhost:5000/$JOB_NAME
                                docker push localhost:5000/$JOB_NAME
                                docker image remove $JOB_NAME
                            fi
                        """
                        shell(containerizationScript)

                        conditionalSteps {
                            condition {
                                fileExists('whanos.yml', BaseDir.WORKSPACE)
                            }
                            runner('Run')
                            steps {
                                shell("chmod +x /var/jenkins_home/create_deployment.py && python3 /var/jenkins_home/create_deployment.py whanos.yml $JOB_NAME")
                            }
                        }
                    }
                }
                '''.stripIndent())
            }
        }
    }
}

