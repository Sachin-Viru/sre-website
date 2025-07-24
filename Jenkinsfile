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
        stage("SonarQube Quality Analysys"){
            steps{
                withSonarQubeEnv("Sonar"){
                    sh "$SONAR_HOME/bin/sonar-scanner -Dsonar.projectName=sre-website -Dsonar.projectKey=sre-website"
                }
            }
        }
        stage("OWASP Dependency check"){
            steps{
                dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'dc'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
//        stage("Sonar Quality Gate Scan"){
//            steps{
//                timeout(time: 10, unit: "MINUTES"){
//                    waitForQualityGate abortPipeline: false
//               }
//            }
//        }
        stage("Build A Jar file for SRE-website"){
            steps{
                sh "mvn clean package -DskipTests"
            }
        }
        stage("Create a docker images"){
            steps{
                script{
                    dockerImage = docker.build("${DOCKER_IMAGE}:${IMAGE_TAG}") 
                }
            }
        }
        stage("Trivy File System Scan"){
            steps{
                sh '''
                docker run --rm -v $(pwd):/src aquasec/trivy fs --format table -o /src/trivy-fs-report.txt /src
                '''
            }
        }
        stage("Docker login and Push"){
            steps{
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]){
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
                    '''
                }
            }
        }
        stage("Run docker image locally"){
            steps{
                sh '''
                docker rm -f sre_website || true
                docker run -itd --name sre_website -p 2020:2020 --network monitoring $DOCKER_IMAGE:$IMAGE_TAG
                '''
            }
        }

    }   
}

