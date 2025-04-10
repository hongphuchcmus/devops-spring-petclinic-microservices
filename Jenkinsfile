pipeline {
    agent any

    environment {
        MAVEN_OPTS = "-Dmaven.repo.local=.m2/repository"
    }

    stages {
        stage('Detect Changed Service') {
            steps {
                script {
                    // sh 'git fetch origin main'
                    // // Get changed files (compared to main) 
                    // def diffFiles = sh(
                    //     script: "git diff --name-only FETCH_HEAD",
                    //     returnStdout: true
                    // ).trim().split("\n")

                    // // Define microservice folders
                    // def services = ['spring-petclinic-vets-service','spring-petclinic-customers-service','spring-petclinic-visits-service','spring-petclinic-admin-server','spring-petclinic-api-gateway','spring-petclinic-config-server','spring-petclinic-genai-service','spring-petclinic-discovery-server']
                
                    // // Set the changedService if any match
                    // env.CHANGED_SERVICE = ''
                    // for (svc in services) {
                    //     if (diffFiles.any { it.startsWith(svc + "/") }) {
                    //         env.CHANGED_SERVICE = svc
                    //         brea
                    //     }
                    // }

                    // if (env.CHANGED_SERVICE == '') {
                    //     error "No service changed. Skipping pipeline."
                    // } else {
                    //     echo "Detected change in: ${env.CHANGED_SERVICE}"
                    // }

                    env.CHANGED_SERVICE = 'spring-petclinic-customers-service'
                }
            }
        }

        stage('Test') {
            when {
                expression { return env.CHANGED_SERVICE }
            }
            steps {
                // Test on changed service 
                sh "./mvnw -pl ${env.CHANGED_SERVICE} -am clean org.jacoco:jacoco-maven-plugin:0.8.8:prepare-agent test org.jacoco:jacoco-maven-plugin:0.8.8:report -Djacoco.skip=false"
            }
            post {
                always {
                    junit "${env.CHANGED_SERVICE}/target/surefire-reports/*.xml"
                    jacoco execPattern:"${env.CHANGED_SERVICE}/target/jacoco.exec",
                    classPattern: "${env.CHANGED_SERVICE}/target/classes",
                    sourcePattern: "${env.CHANGED_SERVICE}/src/main/java",
                    minimumInstructionCoverage: '0'
                }
            }
        }

        stage('Build') {
            when {
                expression { return env.CHANGED_SERVICE }
            }
            steps {
                sh "./mvnw -pl ${env.CHANGED_SERVICE} clean package -DskipTests"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: "${env.CHANGED_SERVICE}/target/*.jar", allowEmptyArchive: true
            archiveArtifacts artifacts: "${env.CHANGED_SERVICE}/target/site/jacoco/**/*", allowEmptyArchive: true
        }
    }
}