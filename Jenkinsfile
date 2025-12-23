pipeline {
    agent none

    environment {
        NEXUS_REGISTRY = '192.168.1.185:9002'
        IMAGE_NAME = 'wecore-admin-api'
        REGISTRY_CREDENTIALS = 'nexus-docker-creds'
        DEPLOY_PATH = 'C:\\Users\\prodadmin\\Desktop\\Devops\\wecore'
        SERVICE_NAME = 'adminAPI'
    }

    stages {

        stage('Set Environment') {
            steps {
                script {
                    if (env.BRANCH_NAME == 'dev') {
                        env.ENVIRONMENT = 'dev'
                    } else if (env.BRANCH_NAME == 'uat') {
                        env.ENVIRONMENT = 'uat'
                    } else if (env.BRANCH_NAME == 'prod') {
                        env.ENVIRONMENT = 'prod'
                    } else {
                        error "Unsupported branch: ${env.BRANCH_NAME}"
                    }

                    echo "Deploying for environment: ${env.ENVIRONMENT}"
                }
            }
        }

        stage('Build & Push Image') {
            agent { label 'built-in' }

            steps {
                script {
                    def imagePath = "${NEXUS_REGISTRY}/${ENVIRONMENT}/${IMAGE_NAME}:latest"

                    echo "Building image ${imagePath}"
                    def dockerImage = docker.build(imagePath)

                    docker.withRegistry("http://${NEXUS_REGISTRY}", REGISTRY_CREDENTIALS) {
                        echo "Pushing image ${imagePath}"
                        dockerImage.push('latest')
                    }
                }
            }
        }

        stage('Deploy Service') {
            agent { label "${ENVIRONMENT}-agent-windows" }

            steps {
                echo "Deploying ${SERVICE_NAME} to ${ENVIRONMENT}"

                bat """
                cd /d ${DEPLOY_PATH}
                set REGISTRY=${NEXUS_REGISTRY}
                set ENVIRONMENT=${ENVIRONMENT}
                set SERVICE_NAME=${SERVICE_NAME}

                docker-compose pull %SERVICE_NAME%
                docker-compose up -d --no-deps %SERVICE_NAME%
                """
            }
        }
    }

    post {
        success {
            echo "Successfully deployed ${SERVICE_NAME} to ${ENVIRONMENT}"
        }
        failure {
            echo "Deployment failed for ${ENVIRONMENT}"
        }
    }
}
