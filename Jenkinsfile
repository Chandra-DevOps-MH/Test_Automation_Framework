pipeline {
    agent any

    tools {
        maven 'Maven_3.9.6'
    }

    environment {
        COMPOSE_PATH = "${WORKSPACE}/docker"   // docker-compose.yml location
        GRID_URL = "http://localhost:4444/wd/hub"
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Chandra-DevOps-MH/Test_Automation_Framework.git'
            }
        }

        stage('Start Selenium Grid') {
            steps {
                dir("${COMPOSE_PATH}") {
                    bat '''
                    docker-compose down
                    docker-compose up -d
                    '''
                }
            }
        }

        stage('Wait for Grid') {
            steps {
                bat 'timeout /t 15'
            }
        }

        stage('Clean & Build') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Run Tests') {
            steps {
                bat '''
                mvn test -Dgrid.url=%GRID_URL%
                '''
            }
        }

        stage('Generate Report') {
            steps {
                publishHTML(target: [
                    reportDir: 'src/test/resources/ExtentReport',
                    reportFiles: 'ExtentReport.html',
                    reportName: 'Extent Spark Report'
                ])
            }
        }
    }

    post {
        always {
            dir("${COMPOSE_PATH}") {
                bat 'docker-compose down'
            }

            archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }

        success {
            emailext(
                subject: "✅ BUILD SUCCESS : ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <p>Hello Team,</p>
                <p>The Jenkins job <b>${env.JOB_NAME}</b> executed successfully.</p>
                <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                <p><a href="${env.BUILD_URL}">View build details</a></p>
                <p>Regards,<br/>Jenkins Automation</p>
                """,
                mimeType: 'text/html',
                to: 'chandrakantghasti99@gmail.com'
            )
        }

        failure {
            emailext(
                subject: "❌ BUILD FAILED : ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: """
                <p>Hello Team,</p>
                <p>The Jenkins job <b>${env.JOB_NAME}</b> has <b>FAILED</b>.</p>
                <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                <p><a href="${env.BUILD_URL}">Check logs</a></p>
                <p>Regards,<br/>Jenkins Automation</p>
                """,
                mimeType: 'text/html',
                to: 'chandrakantghasti99@gmail.com'
            )
        }
    }
}
