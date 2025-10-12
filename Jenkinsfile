pipeline {
    agent any

    environment {
        REMOTE_DIR = 'jenkins/WeCoreAdminAPI'
        MVN_CMD    = './mvnw clean package -DskipTests'
    }

    stages {
        stage('Build') {
            steps {
                echo 'Building application...'
                sh "${MVN_CMD}"
            }
        }

        stage('Deploy') {
            parallel {
                stage('Dev') {
                    when { branch 'dev' }
                    steps {
                        deployApp('dev-server', 'dev')
                    }
                }

                stage('UAT') {
                    when { branch 'uat' }
                    steps {
                        deployApp('uat-server', 'uat')
                    }
                }

                stage('Prod') {
                    when { branch 'prod' }
                    steps {
                        input message: "Deploy to Production?"
                        deployApp('prod-server', 'prod')
                    }
                }
            }
        }
    }
}

def deployApp(String serverConfig, String profile) {
    script {
        def jarFile = sh(
            script: "ls -1 target/*.jar | head -n 1 | xargs -n 1 basename",
            returnStdout: true
        ).trim()

        echo "Deploying ${jarFile} to ${serverConfig} with profile=${profile}"

        sshPublisher(
            publishers: [
                sshPublisherDesc(
                    configName: serverConfig,
                    transfers: [
                        sshTransfer(
                            sourceFiles: "target/${jarFile}",
                            remoteDirectory: "${REMOTE_DIR}",
                            removePrefix: 'target',
                            execCommand: """
                                bash -c '
                                    set -x
                                    echo "Stopping old application if running..."
                                    PID=\$(pgrep -f "${REMOTE_DIR}/${jarFile}")
                                    if [ -n "\$PID" ]; then
                                        kill $PID 2>/dev/null || true
                                        echo "Stopped old app (PID \$PID)."
                                    else
                                        echo "No old app running."
                                    fi

                                    echo "Starting new application..."
                                    nohup java -jar ${REMOTE_DIR}/${jarFile} --spring.profiles.active=${profile} > ${REMOTE_DIR}/app.log 2>&1 &

                                    echo "Application started. Logs: ${REMOTE_DIR}/app.log"
                                '
                            """
                        )
                    ],
                    verbose: true
                )
            ]
        )
    }
}
