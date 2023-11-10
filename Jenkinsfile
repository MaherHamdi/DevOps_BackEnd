pipeline {

    node {
        // Set the Node.js tool version
        tools {
            nodejs '19.9.0'
        }

        stage('Git Checkout') {
            checkout([$class: 'GitSCM', branches: [[name: 'master']], userRemoteConfigs: [[url: 'https://github.com/MaherHamdi/DevOps_BackEnd']]])
        }

        stage('Unit Tests') {
            script {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                }
            }
        }

        stage('Build') {
            sh 'mvn package'
        }

        stage('JUNit Reports') {
            junit '**/target/surefire-reports/*.xml'
            echo "Publishing JUnit reports"
        }

        stage('Jacoco Reports') {
            script {
                echo "Publishing Jacoco Code Coverage Reports"
            }
            post {
                success {
                    jacoco(execPattern: '**/target/*.exec')
                }
            }
        }

        stage('Build Docker Image') {
            script {
                sh 'docker build -t maher198/devops-project .'
            }
        }

        stage('Push Image to Hub') {
            script {
                withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    sh 'docker login -u maher198 -p ${dockerhubpwd}'
                    sh 'docker push maher198/devops-project'
                }
            }
        }

        stage('Deploy to Nexus') {
            script {
                // Maven deploy to Nexus
                withCredentials([usernamePassword(credentialsId: 'Nexus', usernameVariable: 'NEXUS_USERNAME', passwordVariable: 'NEXUS_PASSWORD')]) {
                    sh 'mvn deploy -Dmaven.test.skip=true -DaltDeploymentRepository=nexus::default::http://192.168.34.10:8081/repository/maven-releases/'
                }
            }
        }

        stage('Build Frontend') {
            script {
                // Checkout the Angular frontend repository
                checkout([$class: 'GitSCM', branches: [[name: 'master']], userRemoteConfigs: [[url: 'https://github.com/MaherHamdi/DevOps_Front']]])
                sh 'npm install -g @angular/cli'
                sh 'npm install'
                sh 'ng build --configuration=production'
                // Build and push Docker image for the frontend
                sh 'docker build -t maher198/angular-app -f Dockerfile .'
                withCredentials([string(credentialsId: 'dockerhub-pwd', variable: 'dockerhubpwd')]) {
                    sh 'docker login -u maher198 -p ${dockerhubpwd}'
                    sh 'docker push maher198/angular-app'
                }
            }
        }
    }

 }
