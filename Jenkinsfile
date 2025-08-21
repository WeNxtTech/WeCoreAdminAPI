pipeline {
    agent any
    environment {
        REMOTE_DIR = 'jenkins/WeCoreAdminAPI'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building application...'
                sh './mvnw clean package'
            }
        }

        stage('Deploy to Dev') {
            when { branch 'dev' }
            steps {
                script {
                    def jarFile = sh(script: "ls -1 target/*.jar | head -n 1 | xargs -n 1 basename", returnStdout: true).trim()
                    echo "Detected JAR file: ${jarFile}"

                    sshPublisher(
                        publishers: [
                            sshPublisherDesc(
                                configName: 'dev-server',
                                transfers: [
                                    sshTransfer(
                                        sourceFiles: "target/${jarFile}",
                                        remoteDirectory: "${REMOTE_DIR}",
                                        removePrefix: 'target',
                                        execCommand: """
                                            bash -c '
                                                set -x
                                                echo "Stopping old application if running..."
                                                PID=\$(ps -ef | grep java | grep "${REMOTE_DIR}/${jarFile}" | grep -v grep | awk "{print \\\$2}")
                                                if [ -n "\$PID" ]; then
                                                    kill \$PID
                                                    echo "Old application stopped (PID \$PID)."
                                                else
                                                    echo "No old application running."
                                                fi

                                                echo "Starting new application..."
                                                nohup java -jar ${REMOTE_DIR}/${jarFile} > ${REMOTE_DIR}/app.log 2>&1 &

                                                echo "Application started. Logs: ${REMOTE_DIR}/app.log"
                                                exit 0
                                            '
                                        """

                                    )
                                ],
                                usePromotionTimestamp: false,
                                verbose: true
                            )
                        ]
                    )
                }
            }
        }


        stage('Deploy to UAT') {
            when { branch 'uat' }
            steps {
                echo 'Deploying to UAT Server...'
                // Similar sshPublisher for UAT
            }
        }

        stage('Deploy to Prod') {
            when { branch 'prod' }
            steps {
                input message: "Deploy to Production?"
                echo 'Deploying to Prod Server...'
                // Similar sshPublisher for Prod
            }
        }
    }
}

