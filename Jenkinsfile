pipeline {
    agent any
    tools {
        nodejs '19.9.0'
    }

    stages {
        stage('GIT') {
            steps {
                git branch: 'master',
                url: 'https://github.com/MaherHamdi/DevOps_BackEnd'
            }
        }


        stage('Unit Tests') {
            steps {
                script {
                    sh 'mvn test'
                }
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('build') {
            steps {
                sh 'mvn package'
            }
            post {
                success {
                    emailext subject: 'Jenkins build Success',
                    body: 'The Jenkins build has succeeded. Build URL: ${BUILD_URL}',
                    to: '$DEFAULT_RECIPIENTS'
                }
                failure {
                    emailext subject: 'Jenkins build Failure',
                    body: 'The Jenkins build has failed. Build URL: ${BUILD_URL}',
                    to: '$DEFAULT_RECIPIENTS'
                }
            }
        }

        stage('JUNit') {
            steps {
                junit '**/target/surefire-reports/*.xml'
                echo "Publishing JUnit reports"
            }
        }

        stage('Jacoco Reports') {
            steps {
                script {
                    echo "Publishing Jacoco Code Coverage Reports"
                }
            }
            post {
                success {
                    jacoco(
                        execPattern: '**/target/*.exec',
                    )
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv(installationName:'Sonar') {
                    sh 'chmod +x ./mvnw'
                    sh 'mvn package sonar:sonar'
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                script {
                    // Maven deploy to Nexus
                    sh 'mvn deploy'
                }
            }
        }

        stage('Build docker image') {
            steps {
                script {
                    sh 'docker build -t maher198/devops-project .'
                }
            }
        }

        stage('Push image to Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                        sh 'docker login -u maher198 -p ${dockerhubpwd}'
                        sh 'docker push maher198/devops-project'
                    }
                }
            }
        }

        stage('Build Frontend') {
            steps {
                git branch: 'master',
                url: 'https://github.com/MaherHamdi/DevOps_Front'
                sh 'npm install -g @angular/cli'
                sh 'npm install'
                sh 'ng build --configuration=production'
                sh 'docker build -t maher198/angular-app -f Dockerfile .'
                withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    sh 'docker login -u maher198 -p ${dockerhubpwd}'
                    sh 'docker push maher198/angular-app'
                }
            }
        }
          stage('docker-compose backend') {
                                 steps {
                                     script {
                                         def workspacePath = env.WORKSPACE
                                         sh "cd $workspacePath && docker compose -f docker-compose.yml up -d"
                                     }
                                 }
                             }


}

        post {
            success {
                emailext subject: 'Jenkins build Success',
                body: 'The Jenkins build has succeeded. Build URL: ${BUILD_URL}',
                to: '$DEFAULT_RECIPIENTS'
            }

            failure {
                emailext subject: 'Jenkins build Failure',
                body: 'The Jenkins build has failed. Build URL: ${BUILD_URL}',
                to: '$DEFAULT_RECIPIENTS'
            }
        }
    }

