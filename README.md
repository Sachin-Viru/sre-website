
# SRE Website Jenkins CI/CD Pipeline

This README provides detailed documentation of a complete Jenkins CI/CD pipeline setup for the **SRE Tools & Technologies Website**. The pipeline includes SonarQube analysis, OWASP dependency check, Docker image build and push, Trivy security scan, and monitoring integration with Prometheus and Grafana.

---

## 🔧 Prerequisites

- Jenkins installed (on laptop or VM)
- Docker installed and Jenkins user added to Docker group:
  ```bash
  sudo usermod -aG docker jenkins
  ```
  Restart Jenkins or reboot system for changes to apply.

- Maven installed and configured in Jenkins (`Maven3` label)
- Prometheus, Grafana, Node Exporter running as Docker containers on the same custom Docker network (`monitoring`)
- Docker network created for monitoring:
  ```bash
  docker network create monitoring
  ```

---

## 🐳 Run Required Docker Containers

### SonarQube (in Docker)
```bash
docker run -d --name sonarqube --network monitoring -p 9000:9000 sonarqube:lts
```
- Access: http://localhost:9000
- Generate a SonarQube token from your user account and save it in Jenkins credentials (secret text) with ID `sonar-token`
- Create SonarQube server config in Jenkins: Manage Jenkins → Configure System → SonarQube servers

### Trivy (used as container, no install needed)
Trivy is run inside a container during the pipeline:
```bash
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image <image-name>
```

---

## 🚫 Important Note

> If Jenkins is installed **locally** and not exposed publicly, **GitHub webhooks will NOT work**. You must trigger builds manually or use GitHub polling.

---

## ⚙️ Jenkins Plugin Requirements

Install the following Jenkins plugins:

- **Pipeline** (`workflow-aggregator`)
- **Docker Pipeline**
- **OWASP Dependency-Check Plugin**
- **SonarQube Scanner**
- **Trivy Scanner Plugin** (optional)
- **Credentials Binding Plugin**
- **Maven Integration Plugin**

---

## 🔐 Credentials Required in Jenkins

| ID             | Type           | Usage                        |
|----------------|----------------|------------------------------|
| `docker-hub`   | Username/Password | For DockerHub login/push     |
| `sonar-token`  | Secret text     | SonarQube authentication     |

---

## 🧪 Pipeline Overview

```groovy
pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
    environment {
        SONAR_HOME = tool "Sonar"
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub')
        DOCKER_IMAGE = "sachinviru/sre-website"
        IMAGE_TAG = "latest"
    }

    stages {
        stage("Checkout code") {
            steps {
                echo "Cloning source code from GitHub"
                checkout scm
            }
        }
        stage("Build with Tests") {
            steps {
                sh "mvn clean package"
            }
        }
        stage("SonarQube Quality Analysis") {
            steps {
                withSonarQubeEnv("Sonar") {
                    sh '''
                    mvn sonar:sonar -Dsonar.projectName=sre-website -Dsonar.projectKey=sre-website -Dsonar.java.binaries=target/classes -Dsonar.sources=src/main/java
                    $SONAR_HOME/bin/sonar-scanner -Dsonar.projectName=sre-website -Dsonar.projectKey=sre-website -Dsonar.java.binaries=target/classes -Dsonar.sources=src/main/java
                    '''
                }
            }
        }
        stage("OWASP Dependency check") {
            steps {
                dependencyCheck additionalArguments: '--scan ./ --format XML --out ./', odcInstallation: 'dc'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage("Create Docker Image") {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}:${IMAGE_TAG}")
                }
            }
        }
        stage("Trivy Security Scan") {
            steps {
                sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy:latest image --severity HIGH,CRITICAL ${DOCKER_IMAGE}:${IMAGE_TAG} > trivy-scan.txt'
            }
        }
        stage("Docker login and Push") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin && docker push ${DOCKER_IMAGE}:${IMAGE_TAG}'
                }
            }
        }
        stage("Run docker image locally") {
            steps {
                sh 'docker rm -f sre_website || true && docker run -d --name sre_website -p 2020:2020 --network monitoring ${DOCKER_IMAGE}:${IMAGE_TAG}'
            }
        }
    }

    post {
        always {
            cleanWs()
            archiveArtifacts artifacts: '**/target/surefire-reports/*.xml,**/dependency-check-report.xml,trivy-scan.txt', allowEmptyArchive: true
        }
    }
}
```

---

## 📊 Monitoring Setup (Prometheus + Grafana)

1. You already have Prometheus, Node Exporter, and Grafana running in containers.
2. Add your running SRE container (`sre_website`) to the same Docker network `monitoring`.
3. Add the following target in `prometheus.yml`:
```yaml
  - job_name: 'sre-website'
    static_configs:
      - targets: ['sre_website:2020']
```
4. Restart Prometheus container.

---

## ✅ SRE Tooling Covered

| Category        | Tools                            |
|----------------|----------------------------------|
| CI/CD          | Jenkins, GitHub Actions          |
| Monitoring     | Prometheus, Grafana, Node Exporter |
| Security       | Trivy, Snyk                      |
| Logging        | ELK Stack, Loki                  |
| IaC            | Terraform, Ansible               |

---

Happy SRE Engineering 🚀
