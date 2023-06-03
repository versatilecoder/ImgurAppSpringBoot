pipeline {
	agent any
	environment {
		mavenHome = tool 'jenkins_maven'
	}
	tools {
		jdk 'jdk1.8.0_271'
		maven 'jenkins_maven'
	}
	stages {
		stage('Build'){
			steps {
				bat "mvn clean install -DskipTests"
			}
		}
		stage('Test'){
			steps{
				bat "mvn test"
			}
		}
		stage('Deploy') {
			steps {
			    bat "mvn jar:jar deploy:deploy"
			}
		}
	}
}