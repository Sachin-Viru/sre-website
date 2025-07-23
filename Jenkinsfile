pipeline{
    agent any
    tools{
        maven = Maven3
    }
    environment{
        SONAR_HOME = tool "Sonar"
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
                sh "mvn clean pakage -DskipTests"
            }
        }
        stage("Create a docker images"){
            steps{
                sh "docker build -t sre-website:latest ."
            }
        }

    }   
}

