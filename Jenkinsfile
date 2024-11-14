pipeline {
    agent any

    tools {
        maven "Maven"  // Ensure Maven is installed
    }

    environment {
        EC2_USER = 'ec2-user'
        EC2_HOST = 'ec2-3-101-117-47.us-west-1.compute.amazonaws.com'
        PEM_FILE_PATH = 'C:/Program Files/Jenkins/aws.pem'  // Path to your AWS PEM file
        JAR_NAME = 'devops-0.0.1-SNAPSHOT.jar' 
    }

    stages {
        stage('Checkout') {
            steps {
                // Fetch Github Repository
                git branch: 'master', url: 'https://github.com/Smitkhobragade/Honors-VII-sem.git'
            }
        }
        
        stage('Build') {
            steps {
                bat 'mvn clean install'  // Builds the project and generates JAR
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Copy JAR file to the EC2 instance
                    bat "scp -i \"${PEM_FILE_PATH}\" target/${JAR_NAME} ${EC2_USER}@${EC2_HOST}:/home/ec2-user/"

                    // Start the application on the EC2 server
                    bat "ssh -i \"${PEM_FILE_PATH}\" ${EC2_USER}@${EC2_HOST} java -jar /home/ec2-user/${JAR_NAME}"
                }
            }
        }
    }

    post {
        success {
            archiveArtifacts 'target/*.jar'
            echo 'Deployment successful!'
        }
        failure {
            echo 'Build or deployment failed!'
        }
    }
}
