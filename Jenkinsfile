pipeline{
    agent any
    tools{
        maven 'Maven3'
    }
    environment{
        SONAR_HOME = tool "Sonar"
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub')
        DOCKER_IMAGE = "sachinviru/sre-website"   
        IMAGE_TAG = "latest"
    }

    stages{
        stage("Checkout code"){
            steps{
                echo "Cloning source code from GitHub"
                checkout scm
            }
        }
        stage("Build with Tests"){
            steps{
                sh "mvn clean package"
            }
        }
        stage("SonarQube Quality Analysis"){
            steps{
                withSonarQubeEnv("Sonar"){
                    sh """
                    mvn sonar:sonar -Dsonar.projectName=sre-website -Dsonar.projectKey=sre-website -Dsonar.java.binaries=target/classes -Dsonar.sources=src/main/java
                    $SONAR_HOME/bin/sonar-scanner -Dsonar.projectName=sre-website -Dsonar.projectKey=sre-website -Dsonar.java.binaries=target/classes -Dsonar.sources=src/main/java
                    """
                }
            }
        }
        stage("OWASP Dependency check"){
            steps{
                dependencyCheck additionalArguments: '--scan ./ --format XML --out ./', odcInstallation: 'dc'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage("Create Docker Image"){
            steps{
                script{
                    dockerImage = docker.build("${DOCKER_IMAGE}:${IMAGE_TAG}") 
                }
            }
        }
        stage("Trivy Security Scan"){
            steps{
                sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy --server http://172.20.0.1:4954 image ${DOCKER_IMAGE}:${IMAGE_TAG} --format table -o trivy-image-report.txt'
            }
        }
        stage("Docker login and Push"){
            steps{
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]){
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin && docker push ${DOCKER_IMAGE}:${IMAGE_TAG}'
                }
            }
        }
        stage("Run docker image locally"){
            steps{
                sh 'docker rm -f sre_website || true && docker run -d --name sre_website -p 2020:2020 --network monitoring ${DOCKER_IMAGE}:${IMAGE_TAG}'
            }
        }
    }
    post{
        always{
            cleanWs()
            archiveArtifacts artifacts: '**/target/surefire-reports/*.xml,**/dependency-check-report.xml,trivy-scan.txt', allowEmptyArchive: true
        }
    }   
}