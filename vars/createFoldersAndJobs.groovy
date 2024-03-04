def call(String dslFilePath) {
    def dslScript = readFileFromWorkspace(dslFilePath)
    def dslLines = dslScript.readLines()

    dslLines.each { line ->
        if (line.startsWith("folder(")) {
            def folderName = line.substring(8, line.length() - 1)
            createFolder(folderName)
        } else if (line.startsWith("multiPipeline(")) {
            def args = getArguments(line)
            createMultiPipelineJob(args[0], args[1])
        } else if (line.startsWith("pipeline(")) {
            def args = getArguments(line)
            createPipelineJob(args[0], args[1])
        } else {
            println "Unknown command: ${line}"
        }
    }
}

def createFolder(folderName) {
    // Create folder using Jenkins API
    def folder = Jenkins.instance.createProject(jenkins.model.Folder, folderName)
    println "Folder created: ${folder.fullName}"

    // Attach shared library to folder
    folder.addProperty(new org.jenkinsci.plugins.workflow.libs.FolderLibraries([
        new org.jenkinsci.plugins.workflow.libs.LibraryConfiguration(
            "MySharedLibrary", // Shared library name
            new org.jenkinsci.plugins.workflow.libs.LibraryConfiguration.DefaultVersionSpec("master"), // Branch or tag
            new org.jenkinsci.plugins.workflow.libs.LibraryConfiguration.ModernSCMSourceDefinition(
                "https://github.com/your-shared-library-repo.git" // Shared library repository URL
            )
        )
    ]))
}

def createFolder(folderName) {
    // Create folder implementation
    def folderPath = "${JENKINS_HOME}/jobs/${folderName}"
    if (!fileExists(folderPath)) {
        sh "mkdir -p ${folderPath}"
    } else {
        println "Folder '${folderName}' already exists."
    }
}

def createMultiPipelineJob(jobName, folderName) {
    // Create multi-pipeline job implementation
    def jobScript = """
pipeline {
    agent any
    stages {
        stage('Example') {
            steps {
                echo 'Hello World'
            }
        }
    }
}
"""
    def jobPath = "${JENKINS_HOME}/jobs/${folderName}/jobs/${jobName}"
    if (!fileExists(jobPath)) {
        sh "mkdir -p ${jobPath}"
        writeFile file: "${jobPath}/config.xml", text: jobScript
    } else {
        println "Multi-pipeline job '${jobName}' in folder '${folderName}' already exists."
    }
}

def createPipelineJob(jobName, folderName) {
    // Create pipeline job implementation
    def jobScript = """
pipeline {
    agent any
    stages {
        stage('Example') {
            steps {
                echo 'Hello World'
            }
        }
    }
}
"""
    def jobPath = "${JENKINS_HOME}/jobs/${folderName}/jobs/${jobName}"
    if (!fileExists(jobPath)) {
        sh "mkdir -p ${jobPath}"
        writeFile file: "${jobPath}/config.xml", text: jobScript
    } else {
        println "Pipeline job '${jobName}' in folder '${folderName}' already exists."
    }
}

def fileExists(filePath) {
    return sh(script: "[ -e ${filePath} ]", returnStatus: true) == 0
}

def getArguments(line) {
    def args = line.substring(line.indexOf("(") + 1, line.indexOf(")")).split(',').collect { it.trim() }
    return args
}
