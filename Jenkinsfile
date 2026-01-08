pipeline {
    agent none

    environment {
        REGISTRY             = 'registry.wenxttech.com'
        REGISTRY_CREDENTIALS = 'nexus-docker-creds'
        IMAGE_NAME           = 'wecore-admin-api'
        SERVICE_NAME         = 'adminAPI'
    }

    stages {

        stage('Init') {
            agent any
            steps {
                script {

                    // Environment mapping
                    def CONFIG = [
                        dev : ['dev-docker', 'dev-agent-windows', 'windows', 'C:\\Users\\prodadmin\\Desktop\\Devops\\wecore', 1],
                        uat : ['uat',        'uat-agent-linux-143', 'linux',   '/opt/devops/wecore',                           1],
                        prod: ['prod',       'prod-agent-linux',  'linux',   '/opt/devops/wecore',                           3]
                    ]

                    def entry = CONFIG.find { it.value[0] == env.BRANCH_NAME }?.value
                    if (!entry) {
                        error "Unsupported branch: ${env.BRANCH_NAME}"
                    }

                    env.ENVIRONMENT          = CONFIG.find { it.value == entry }.key
                    env.DEPLOY_AGENT         = entry[1]
                    env.OS_TYPE              = entry[2]
                    env.DEPLOY_PATH          = entry[3]
                    env.COMMON_API_REPLICAS  = entry[4].toString()

                    echo """
                    Environment  : ${ENVIRONMENT}
                    Agent        : ${DEPLOY_AGENT}
                    OS           : ${OS_TYPE}
                    Path         : ${DEPLOY_PATH}
                    Replicas     : ${COMMON_API_REPLICAS}
                    """
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
                        set -e
                        export REGISTRY=${REGISTRY}
                        export ENVIRONMENT=${ENVIRONMENT}

                        cd ${DEPLOY_PATH}

                        docker-compose pull ${SERVICE_NAME}
                        docker-compose up -d --no-deps \
                          --scale ${SERVICE_NAME}=${COMMON_API_REPLICAS} \
                          ${SERVICE_NAME}
                        """
                    } else {
                        bat """
                        set REGISTRY=${REGISTRY}
                        set ENVIRONMENT=${ENVIRONMENT}

                        cd /d ${DEPLOY_PATH}

                        docker-compose pull %SERVICE_NAME%
                        docker-compose up -d --no-deps ^
                          --scale %SERVICE_NAME%=%COMMON_API_REPLICAS% ^
                          %SERVICE_NAME%
                        """
                    }
                }
            }
        }
    }

    post {
        success {
            echo "Successfully deployed ${SERVICE_NAME} (${COMMON_API_REPLICAS} replicas) to ${ENVIRONMENT}"
        }
        failure {
            echo "Deployment failed for ${SERVICE_NAME} in ${ENVIRONMENT}"
        }
    }
}
