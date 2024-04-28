Since we wanted to be able to support every whanos-compatible repository using C, Java, Javascript, Python and even Befunge. For each of those languages, we’ve written a Dockerfile.base which would serve the purpose of building the base dependencies needed by the language such as GCC for C before building the provided Dockerfile to add layers to the image and a Dockerfile.standalone to build the entire project whenever the whanos-compatible repository does not have a Dockerfile at root.