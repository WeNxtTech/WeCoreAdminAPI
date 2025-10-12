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
                                  PID=\$(ps -ef | grep java | grep "${remoteDir}/${jarFile}" | grep -v grep | awk "{print \\\$2}")
                                  if [ -n "\$PID" ]; then
                                      kill \$PID
                                      echo "Old application stopped (PID \$PID)."
                                  else
                                      echo "No old application running."
                                  fi

                                  echo "Starting new application..."
                                  nohup java -jar ${remoteDir}/${jarFile} --spring.profiles.active=${profile}> ${remoteDir}/app.log 2>&1 &

                                  echo "Application started. Logs: ${remoteDir}/app.log"
                                  exit 0
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
