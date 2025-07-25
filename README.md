
# SRE Website Jenkins CI/CD Pipeline

This README provides a comprehensive guide for setting up a **Jenkins-based CI/CD pipeline** for an SRE-themed Java website project. It covers tool installation, configuration, Jenkins pipeline scripting, and monitoring integration.

---

## 🧰 Prerequisites

- Ubuntu-based laptop or VM
- User with `sudo` access

---

## 🖥️ Install Jenkins on Local Laptop

```bash
sudo apt update
sudo apt install -y openjdk-11-jdk
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key | sudo tee /usr/share/keyrings/jenkins-keyring.asc
sudo sh -c 'echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] https://pkg.jenkins.io/debian-stable binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt update
sudo apt install -y jenkins
sudo systemctl start jenkins
sudo systemctl enable jenkins
```

> First login: Open [http://localhost:8080](http://localhost:8080) and enter the initial password from:

```bash
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```

Follow the on-screen steps to install suggested plugins and create the admin user.

---

## 🐳 Install Docker and Configure Jenkins Access

```bash
sudo apt install -y docker.io
sudo usermod -aG docker jenkins
sudo systemctl restart jenkins
```

> ⚠️ Reboot system if necessary to apply group changes.

---

## 📦 Required Jenkins Plugins

Install the following plugins:

* Pipeline (workflow-aggregator)
* Docker Pipeline
* SonarQube Scanner
* OWASP Dependency-Check
* Trivy Plugin (optional)
* Maven Integration
* Credentials Binding Plugin

---

## 📦 Install Maven and OWASP in Jenkins

1. **Install Maven:**

   * Manage Jenkins → Global Tool Configuration → Maven → Add Maven (`Name: Maven3`)
   * Install automatically or configure manually

2. **Install OWASP Dependency Check Plugin:**

   * Manage Jenkins → Manage Plugins → Available → Search `OWASP Dependency-Check Plugin`
   * Install and restart Jenkins

---

## 🔐 Add Credentials in Jenkins

Navigate to: **Manage Jenkins → Credentials → Global → Add Credentials**

| ID            | Type              | Usage                    |
| ------------- | ----------------- | ------------------------ |
| `docker-hub`  | Username/Password | DockerHub login for push |
| `sonar-token` | Secret text       | SonarQube authentication |

---

## 📈 Setup SonarQube in Docker

```bash
docker network create monitoring

docker run -d   --name sonarqube   --network monitoring   -p 9000:9000   sonarqube:lts
```

- Access: [http://localhost:9000](http://localhost:9000)
- Default login: `admin` / `admin`
- Set new password and generate token:
  - Profile → Security → Generate Token
- Save token in Jenkins credentials (`sonar-token`)
- Add server in Jenkins:
  - Manage Jenkins → Configure System → SonarQube servers → Add → Name: `Sonar`, Token Credentials: `sonar-token`

---

## 🔍 Install Trivy via Docker (No local install needed)

Used inside the pipeline:

```bash
docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image <image-name>
```

---

## 📦 Monitoring Stack in Docker (Prometheus + Grafana + Node Exporter)

1. Create a Docker network:

```bash
docker network create monitoring
```

2. Run Prometheus:

```bash
docker run -d   --name prometheus   --network monitoring   -v $PWD/prometheus.yml:/etc/prometheus/prometheus.yml   -p 9090:9090   prom/prometheus
```

3. Run Grafana:

```bash
docker run -d   --name grafana   --network monitoring   -p 3000:3000   grafana/grafana
```

4. Run Node Exporter:

```bash
docker run -d   --name node_exporter   --network monitoring   quay.io/prometheus/node-exporter
```

---

## 🔁 prometheus.yml

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'prometheus'
    static_configs:
      - targets: ['localhost:9090']

  - job_name: 'node'
    static_configs:
      - targets: ['node_exporter:9100']

  - job_name: 'sre-website'
    static_configs:
      - targets: ['sre_website:2020']
```

---

## 🧪 CI/CD Pipeline Steps Overview
The pipeline performs the following stages:
```
✅ Checkout code from GitHub

✅ SonarQube Code Scan to analyze code quality

✅ OWASP Dependency Check for known vulnerabilities

✅ Wait for Sonar Quality Gate (optional addition if configured)

✅ Build JAR using Maven

✅ Build Docker Image

✅ Trivy Image Scan (only container image, not file system)

✅ Push to Docker Hub (only if configured)

✅ Run Container Locally (for testing on Docker network)

✅ Monitor using Prometheus + Grafana (optional setup)

```

## 🚀 Jenkins CI/CD Pipeline (Groovy Script)

```groovy
pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
    environment {
        SONAR_HOME = tool "Sonar"
        DOCKER_IMAGE = "sachinviru/sre-website"
        IMAGE_TAG = "latest"
    }

    stages {
        stage("Checkout code") {
            steps {
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
                    mvn sonar:sonar -Dsonar.projectKey=sre-website -Dsonar.projectName=sre-website Dsonar.java.binaries=target/classes -Dsonar.sources=src/main/java
                    $SONAR_HOME/bin/sonar-scanner -Dsonar.projectName=sre-website -Dsonar.projectKey=sre-website -Dsonar.java.binaries=target/classes -Dsonar.sources=src/main/java                                
                    '''
                }
            }
        }
        stage("OWASP Dependency Check") {
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
                sh 'docker run --rm -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --severity HIGH,CRITICAL ${DOCKER_IMAGE}:${IMAGE_TAG} > trivy-scan.txt'
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

✅ Now your `sre_website` container will expose metrics at port `2020`, picked up by Prometheus, and visible in Grafana ([http://localhost:3000](http://localhost:3000)).

---

Happy SRE Engineering 🚀
