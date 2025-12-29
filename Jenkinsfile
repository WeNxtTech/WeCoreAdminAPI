pipeline {
    agent none

    environment {
        // TO CHANGE
        REGISTRY             = '192.168.1.185:9002'
        REGISTRY_CREDENTIALS = 'nexus-docker-creds'
        IMAGE_NAME           = 'wecore-admin-api'
        SERVICE_NAME         = 'adminAPI'
    }

    stages {

        stage('Init') {
            agent any
            steps {
                script {

                    // TO CHANGE
                    def CONFIG = [
                        dev : ['dev-docker', 'dev-agent-windows', 'windows', 'C:\\Users\\prodadmin\\Desktop\\Devops\\wecore'],
                        uat : ['uat',        'uat-agent-linux-62', 'linux',   '/opt/devops/wecore'],
                        prod: ['prod',       'prod-agent-linux',   'linux',   '/opt/devops/wecore']
                    ]

                    def entry = CONFIG.find { it.value[0] == env.BRANCH_NAME }?.value
                    if (!entry) {
                        error "Unsupported branch: ${env.BRANCH_NAME}"
                    }

                    env.ENVIRONMENT  = CONFIG.find { it.value == entry }.key
                    env.DEPLOY_AGENT = entry[1]
                    env.OS_TYPE      = entry[2]
                    env.DEPLOY_PATH  = entry[3]

                    echo "Env=${ENVIRONMENT}, Agent=${DEPLOY_AGENT}, OS=${OS_TYPE}"
                }
            }
        }

        stage('Build & Push Image') {
            agent { label 'built-in' }
            steps {
                script {
                    def image = "${REGISTRY}/${ENVIRONMENT}/${IMAGE_NAME}:latest"

                    docker.withRegistry("http://${REGISTRY}", REGISTRY_CREDENTIALS) {
                        docker.build(image).push('latest')
                    }
                }
            }
        }

        stage('Deploy Service') {
            agent { label "${DEPLOY_AGENT}" }
            steps {
                script {
                    if (OS_TYPE == 'linux') {
                        sh """
                        export REGISTRY=${REGISTRY}
                        export ENVIRONMENT=${ENVIRONMENT}

                        cd ${DEPLOY_PATH}
                        docker-compose pull ${SERVICE_NAME}
                        docker-compose up -d --no-deps ${SERVICE_NAME}
                        """
                    } else {
                        bat """
                        set REGISTRY=${REGISTRY}
                        set ENVIRONMENT=${ENVIRONMENT}

                        cd /d ${DEPLOY_PATH}
                        docker-compose pull ${SERVICE_NAME}
                        docker-compose up -d --no-deps ${SERVICE_NAME}
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Successfully deployed ${SERVICE_NAME} to ${ENVIRONMENT}"
        }
        failure {
            echo "Deployment failed for ${SERVICE_NAME} in ${ENVIRONMENT}"
        }
    }
}
