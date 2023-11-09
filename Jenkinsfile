pipeline {

    agent any
tools { nodejs '19.9.0'}
    stages {

        stage('Checkout Backend Repo') {
            steps {
               checkout scm
            }
        }
  stage('Unit Tests') {
            steps {
                script {

                    sh 'mvn test'
                }
            }
            post{
            always{
            junit '**/target/surefire-reports/TEST-*.xml'
            }
            }
        }
        stage('build') {
            steps {
                sh 'mvn package'
            }
        }

stage('JUNit Reports') {
            steps {
                    junit '**/target/surefire-reports/*.xml'
		                echo "Publishing JUnit reports"
            }
        }
         stage('Jacoco Reports') {
                    steps {

                          echo "Publishing Jacoco Code Coverage Reports";
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
                withSonarQubeEnv(installationName:'sql') {
                sh 'chmod +x ./mvnw'
                    sh 'mvn package sonar:sonar'
                }
            }
        }
        stage('Build docker image'){
                                steps{
                                    script{
                                        sh 'docker build -t maher198/devops-project .'
                                    }
                                }
                            }
         stage('Push image to Hub'){
              steps{
                   script{
                           withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                              sh 'docker login -u maher198 -p ${dockerhubpwd}'
                              sh 'docker push maher198/devops-project'
                           }
                           }
                   }
                   }
        stage('Build Frontend') {
            steps {
                // Checkout the Angular frontend repository
                git branch: 'main',
                url: 'https://github.com/MaherHamdi/DevOps_Front'
                sh 'npm install -g @angular/cli'
                sh 'npm install'
                sh 'ng build --configuration=production'
            }
        }

    }
 }