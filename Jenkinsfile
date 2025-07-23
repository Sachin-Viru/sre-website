pipeline{
    agent any
    environment{
        SONAR_HOME = tool "Sonar"
        DOCKER_IMAGE = "sachinviru/sre-website"   
        IMAGE_TAG = "latest"
    }

    stages{
        stage("Checkout code"){
            steps{
                echo "📥 Cloning source code from GitHub"
                checkout scm
            }
        }

    }   
}

