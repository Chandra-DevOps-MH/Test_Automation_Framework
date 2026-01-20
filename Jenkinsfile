pipeline {
    agent any

    tools {
                  // Jenkins → Global Tool Configuration
        maven 'Maven_3.9.6'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/Chandra-DevOps-MH/Test_Automation_Framework.git'
            }
        }

        stage('Clean & Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'mvn test'
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
            archiveArtifacts artifacts: '**/src/test/resources/ExtentReport/*.html', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }

        success {
        emailext(
            subject: "✅ BUILD SUCCESS : ${env.JOB_NAME} #${env.BUILD_NUMBER}",
            body: """
                <p>Hello Team,</p>

                <p>The Jenkins job <b>${env.JOB_NAME}</b> has been executed successfully.</p>

                <p><b>Build Number:</b> ${env.BUILD_NUMBER}</p>
                <p><b>Status:</b> SUCCESS</p>

                <p>
                    <a href="${env.BUILD_URL}">Click here to view build details</a>
                </p>
                
                 <p><b>Extent Report:</b>
                    <a href="http://localhost:8080/job/OrangeHRM_API_TestCase/HTML_20Extent_20Report/">Click here to view Extent Report</a>
                </p>

                <p>Regards,<br/>
                Jenkins Automation</p>
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
                <p><b>Status:</b> FAILURE</p>

                <p><b>Failed Stage:</b> ${env.STAGE_NAME}</p>

                <p>
                    <a href="${env.BUILD_URL}">Click here to check build logs</a>
                </p>

                <p>Please investigate the issue.</p>

                <p>Regards,<br/>
                Jenkins Automation</p>
            """,
            mimeType: 'text/html',
            to: 'chandrakantghasti99@gmail.com'
        )
    }
    }
}
