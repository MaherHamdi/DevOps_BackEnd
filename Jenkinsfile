pipeline {

    agent any

    tools { nodejs '19.9.0'}

    stages {
        stage('Checkout Backend Repo') {
            steps {
              git branch: 'master',
              url: 'https://github.com/MaherHamdi/DevOps_BackEnd.git'
            }
        }
                stage('build') {
                    steps {
                        sh 'mvn package'
                    }
                      post {
                                  success {
                                  emailext subject: 'Jenkins build Success',
                                  body: 'The Jenkins build  has succeeded. Build URL: ${BUILD_URL}',
                                   to: '$DEFAULT_RECIPIENTS'
                                            }

                                   failure {
                                   emailext subject: 'Jenkins build Failure',
                                   body: 'The Jenkins build has failed. Build URL: ${BUILD_URL}',
                                   to: '$DEFAULT_RECIPIENTS'
                                             }
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

        stage('Build docker image'){
            steps{
                script{
                    sh 'docker build -t maher198/spring-boot-docker .'
                }
            }
        }
        stage('Push beckend image to Hub'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockermdp')]) {
                    sh 'docker login -u maher198 -p ${dockermdp}'
                    }
                    sh 'docker push maher198/spring-boot-docker'
                }
            }
        }
        stage('Build Frontend') {
            agent any
            steps {
                // Checkout the Angular frontend repository
                git branch: 'master',
                url: 'https://github.com/MaherHamdi/DevOps_Front.git'
                sh 'rm -rf node_modules'
                sh 'npm install'
                sh 'npm install -g @angular/cli'
                sh 'ng build --configuration=production'
                sh 'docker build -t maher198/angular-app -f Dockerfile .'
                withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockermdp')]) {
                sh 'docker login -u maher198 -p ${dockermdp}'
                sh 'docker push maher198/angular-app'
                }
            }
        }
        stage('docker-compose full stack app'){
            steps{
                script{
                    sh 'docker compose up --build -d'
                }
            }
        }
    }
    post {
         success {
          emailext subject: 'Jenkins build Success',
          body: 'The Jenkins build  has succeeded. Build URL: ${BUILD_URL}',
          to: '$DEFAULT_RECIPIENTS'
           }

           failure {
           emailext subject: 'Jenkins build Failure',
           body: 'The Jenkins build has failed. Build URL: ${BUILD_URL}',
            to: '$DEFAULT_RECIPIENTS'
                                }
                            }


}