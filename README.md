
# Jenkins CI/CD Pipeline for SRE Website

This project demonstrates a complete CI/CD pipeline setup for an SRE-themed website using Jenkins, Docker, Trivy, SonarQube, and monitoring tools like Prometheus + Grafana.

---

## 🔧 Prerequisites

- Jenkins installed on your system (e.g., Ubuntu)
- Docker installed and configured (`jenkins` user added to `docker` group)
- Java, Maven, Git installed
- Public GitHub repo for pipeline triggering via webhook

---

## 🧩 Step-by-Step Jenkins Configuration

### 1. 🔌 Install Required Jenkins Plugins

Install the following plugins from `Manage Jenkins → Plugins`:
- **Git**
- **Pipeline**
- **Docker Pipeline**
- **OWASP Dependency-Check Plugin**
- **SonarQube Scanner for Jenkins**
- **Credentials Binding Plugin**
- **Trivy Scanner Plugin** (if available)

### 2. ⚙️ Maven Setup in Jenkins
Go to `Manage Jenkins → Global Tool Configuration → Maven`
- Add `Maven3` with correct installation path

### 3. 📦 SonarQube Configuration
- Install SonarQube locally or use public instance
- In Jenkins → `Manage Jenkins → Configure System`
  - Scroll to **SonarQube servers**
  - Add instance name (e.g., `Sonar`) and token
- Add webhook in your SonarQube project:  
  `http://<jenkins-url>/sonarqube-webhook/`

### 4. 🛡️ OWASP Dependency-Check Configuration
- Install `dependency-check` on Jenkins server
- In Jenkins → `Global Tool Configuration → Dependency-Check installations`, name it `dc`

### 5. 🐳 Docker Setup
- Add Jenkins user to docker group:
  ```bash
  sudo usermod -aG docker jenkins
  sudo systemctl restart docker
  ```
- Create Docker Hub credentials in Jenkins:
  - `Manage Jenkins → Credentials → Global`
  - Add Username/Password with ID: `docker-hub`

### 6. 🛠️ Trivy
You don’t need to install Trivy on Jenkins host. It is pulled inside the pipeline from DockerHub.

---

## 🧪 Jenkins Pipeline (Groovy)

```groovy
pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
    environment {
        SONAR_HOME = tool "Sonar"
        DOCKER_IMAGE = "sre-website"
        IMAGE_TAG = "latest"
    }
    stages {
        stage("Checkout code") {
            steps {
                checkout scm
            }
        }
        stage("SonarQube Quality Analysis") {
            steps {
                withSonarQubeEnv("Sonar") {
                    sh "${SONAR_HOME}/bin/sonar-scanner -Dsonar.projectName=sre-website -Dsonar.projectKey=sre-website"
                }
            }
        }
        stage("OWASP Dependency Check") {
            steps {
                dependencyCheck additionalArguments: '--scan ./', odcInstallation: 'dc'
                dependencyCheckPublisher pattern: '**/dependency-check-report.xml'
            }
        }
        stage("Build JAR") {
            steps {
                sh "mvn clean package -DskipTests"
            }
        }
        stage("Build Docker Image") {
            steps {
                sh "docker build -t ${DOCKER_IMAGE}:${IMAGE_TAG} ."
            }
        }
        stage("Trivy FS Scan") {
            steps {
                sh '''
                docker run --rm -v $(pwd):/src aquasec/trivy fs --format table -o /src/trivy-fs-report.txt /src
                '''
            }
        }
        stage("Docker Login and Push") {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh '''
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}:${IMAGE_TAG}
                    '''
                }
            }
        }
        stage("Run Docker Image Locally") {
            steps {
                sh "docker run -d --name sre_website --network monitoring -p 2020:2020 ${DOCKER_IMAGE}:${IMAGE_TAG}"
            }
        }
    }
}
```

---

## 📊 Monitoring Setup

1. **Create a Docker network**
```bash
docker network create monitoring
```

2. **Run Node Exporter**
```bash
docker run -d --name node-exporter --network monitoring prom/node-exporter
```

3. **Run Prometheus**
```bash
docker run -d --name prometheus --network monitoring -p 9090:9090 -v $PWD/prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus
```

4. **Run Grafana**
```bash
docker run -d --name grafana --network monitoring -p 3000:3000 grafana/grafana
```

5. **Run your SRE app container in same network**
```bash
docker run -d --name sre_website --network monitoring -p 2020:2020 sre-website:latest
```

---

## 🛠️ Tools & Technologies Used

### CI/CD
- Jenkins
- GitHub Actions

### Monitoring
- Prometheus
- Grafana

### Security
- Trivy
- Snyk
- OWASP Dependency Check

### Logging
- ELK Stack
- Loki

### Infrastructure as Code
- Terraform
- Ansible

---

## 📘 Notes

- Set up GitHub webhook to Jenkins for auto-triggering on push events
- Use Jenkins shared libraries and parameterized builds for more flexibility
- Export Trivy, OWASP, and Sonar reports as part of build artifacts

---

Made with ❤️ by SRE Team.
