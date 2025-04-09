pipeline {
    agent any

    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=.m2/repository"
    }

    stages {
        stage('Detect Changed Service') {
            steps {
                script {
                    sh 'git fetch origin main'
                    // Get changed files (compared to main) 
                    def diffFiles = sh(
                        script: "git diff --name-only FETCH_HEAD",
                        returnStdout: true
                    ).trim().split("\n")

                    // Define microservice folders
                    def services = ['spring-petclinic-customers-service', 'spring-petclinic-vets-service', 'spring-petclinic-visits-service', 'spring-petclinic-genai-service', 'spring-petclinic-config-service']
                
                    // Set the changedService if any match
                    env.CHANGED_SERVICE = ''
                    for (svc in services) {
                        if (diffFiles.any { it.startsWith(svc + "/") }) {
                            env.CHANGED_SERVICE = svc
                            break
                        }
                    }

                    if (env.CHANGED_SERVICE == '') {
                        error "No service changed. Skipping pipeline."
                    } else {
                        echo "Detected change in: ${env.CHANGED_SERVICE}"
                    }
                }
            }
        }

        stage('Test') {
            when {
                expression { return env.CHANGED_SERVICE }
            }
            steps {
                dir("${env.CHANGED_SERVICE}") {
                    sh './mvnw test'
                }
            }
            post {
                always {
                    junit "${env.CHANGED_SERVICE}/target/surefire-reports/*.xml"
                    jacoco execPattern: "${env.CHANGED_SERVICE}/target/jacoco.exec"
                }
            }
        }

        stage('Build') {
            when {
                expression { return env.CHANGED_SERVICE }
            }
            steps {
                dir("${env.CHANGED_SERVICE}") {
                    sh './mvnw clean package -DskipTests'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: "${env.CHANGED_SERVICE}/target/*.jar", allowEmptyArchive: true
        }
    }
}