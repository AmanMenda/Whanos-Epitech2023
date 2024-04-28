#!usr/bin python3

import os

class RepoAnalyser:

    buildDependency = {
        "C":"docker build -t whanos-c .",
        "Java":"docker build -t whanos-java .",
        "JavaScript":"docker build -t whanos-javascript .",
        "Python":"docker build -t whanos-python .",
        "Befunge":"docker build -t whanos-befunge ." }

    dockerFilesCopy = {
        "C":"cp images/c/Dockerfile.standalone .",
        "Java":"cp images/java/Dockerfile.standalone .",
        "JavaScript":"cp images/javascript/Dockerfile.standalone .",
        "Python":"cp images/python/Dockerfile.standalone .",
        "Befunge":"cp images/befunge/Dockerfile.standalone ."}

    files = os.listdir(".")

    def __init__(self):
        pass

    def checkLanguageUsed(self):
        lang = []
        try:
            if 'Makefile' in self.files:
                lang.append('C')

            if 'package.json' in self.files:
                lang.append('JavaScript')

            if 'requirements.txt' in self.files:
                lang.append('Python')
            
            try:
                if 'app' in self.files:
                    appFiles = os.listdir("./app")

                    if 'pom.xml' in appFiles:
                        lang.append('Java')

                    if 'main.bf' in appFiles:
                        lang.append('Befunge')

            except:
                raise Exception("No language found")
        except:
            raise Exception("No language found")
        return lang

    def runDockerFiles(self, language):
        i = 0
        if "Dockerfile" in self.files:
            i = os.system(self.buildDependency[language])
        else:
            os.system(self.dockerFilesCopy[language])
            os.system(self.buildDependency[language])
            
        if i != 0:
            raise Exception('Dockerfile build failed')

def main():

    myrepoanalyser = RepoAnalyser()
    language = myrepoanalyser.checkLanguageUsed()
    
    if language.__len__() > 1:
        raise Exception("Too many languages")
    if language.__len__() == 0:
        raise Exception('No language found')

    myrepoanalyser.runDockerFiles(language[0])

if __name__ == "__main__":
    main()