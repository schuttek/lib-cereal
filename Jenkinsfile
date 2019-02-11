#!groovy

final def libName = 'lib-cereal'
final def projectRepoUrl = 'https://github.com/Wezr/lib-cereal/'

/* Configuration */
properties([
		disableConcurrentBuilds(),
		[$class: 'GithubProjectProperty', projectUrlStr: projectRepoUrl],
		parameters([
			booleanParam(name: 'deploy', defaultValue: false, "description": "Check that if you want to proceed the deployment phase to artifact repository"),
		]),
		pipelineTriggers([githubPush()])
])

/* Run */
final def doDeployment = params.deploy == null ? false : params.deploy

node {
	final def JAVA_HOME = tool 'JDK8'
	withEnv(["JAVA_HOME=${JAVA_HOME}", "PATH+JAVA=${JAVA_HOME}/bin"]) {
		wrap([$class: 'AnsiColorBuildWrapper']) {
			// Checkout the last version
			stage('Checkout') {
				sh 'rm -rf ./*'
				sh 'rm -rf .git .gitignore .gradle'
				checkout scm
			}
			
			stage('Build') {
				// Launch compilation
				sh './gradlew --no-daemon clean assemble'

				// find out which version we've just built... (this is sometimes overridden by the project's build.gradle)
				def dashCount = 1
				def tmpStr = libName
				while (true) {
					def ind = tmpStr.indexOf('-')
					dashCount++
					tmpStr = tmpStr.substring(ind + 1)
					if (ind < 0) break
				}
				version = sh returnStdout: true, script: 'find build/libs -name "' + libName + '-*.jar" |awk -F"-" \'{ print $' + dashCount + ' }\' |awk -F".jar" \'{ print $1 }\''
				version = version.trim()
				archiveFile = 'build/libs/' + libName + '-' + version + '.jar'
				archive archiveFile
			}
			
			stage('Test') {
				// run tests
				sh './gradlew --no-daemon check jacocoTestReport'
				step([$class: 'JUnitResultArchiver', testResults: '**/build/test-results/**/TEST-*.xml'])
				jacoco classPattern: "build/classes/java/main", exclusionPattern: "", execPattern: "build/jacoco/*.exec", sourcePattern: "src/main"
			}
			
			if (doDeployment) {
				stage('Deployment') {
					sh './gradlew --no-daemon publish'
				}

			} else {
				println "*** DEPLOYMENT: SKIPPED ***"
			}
		}
	}
}
