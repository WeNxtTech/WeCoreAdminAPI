pipeline {
    agent none
    parameters {
        string(
            name: 'ROLLBACK_VERSION',
            defaultValue: '',
            description: 'Rollback image version (e.g. 42-a3f9c2d). Leave empty for normal deployment.'
        )
    }
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

                    def CONFIG = [
                        dev : ['dev-docker', 'dev-agent-windows', 'windows', 'C:\\Users\\prodadmin\\Desktop\\Devops\\wecore', 1],
                        uat : ['uat',        'uat-agent-linux-143', 'linux',   '/opt/devops/wecore', 1],
                        prod: ['prod',       'prod-agent-linux',  'linux',   '/opt/devops/wecore', 3],
                        'zambia-live': ['zambia-live', 'uat-agent-linux-143', 'linux', '/opt/devops/wecore-zambia-live', 1]
                    ]

                    def entry = CONFIG.find { it.value[0] == env.BRANCH_NAME }?.value
                    if (!entry) {
                        error "Unsupported branch: ${env.BRANCH_NAME}"
                    }

                    env.ENVIRONMENT         = CONFIG.find { it.value == entry }.key
                    env.DEPLOY_AGENT        = entry[1]
                    env.OS_TYPE             = entry[2]
                    env.DEPLOY_PATH         = entry[3]
                    env.COMMON_API_REPLICAS = entry[4].toString()

                    if (params.ROLLBACK_VERSION?.trim()) {
                        env.VERSION = params.ROLLBACK_VERSION
                        env.IS_ROLLBACK = "true"
                        echo "ROLLBACK MODE ENABLED"
                    } else {
                        env.IS_ROLLBACK = "false"

                        env.GIT_SHORT = sh(
                            script: "git rev-parse --short HEAD",
                            returnStdout: true
                        ).trim()

                        env.VERSION = "${BUILD_NUMBER}-${GIT_SHORT}"
                    }

                    echo """
                    Environment : ${ENVIRONMENT}
                    Agent       : ${DEPLOY_AGENT}
                    OS          : ${OS_TYPE}
                    Path        : ${DEPLOY_PATH}
                    Replicas    : ${COMMON_API_REPLICAS}
                    Version     : ${VERSION}
                    Rollback    : ${IS_ROLLBACK}
                    """
                }
            }
        }
        stage('Build & Push Image') {
            when {
                expression { env.IS_ROLLBACK != "true" }
            }
            agent { label 'phoenix-jenkin-server-122' }
            steps {
                script {
                    def imageVersioned = "${REGISTRY}/${ENVIRONMENT}/${IMAGE_NAME}:${VERSION}"
                    def imageLatest    = "${REGISTRY}/${ENVIRONMENT}/${IMAGE_NAME}:latest"

                    docker.withRegistry("https://${REGISTRY}", REGISTRY_CREDENTIALS) {
                        def img = docker.build(imageVersioned)
                        img.push()
                        img.push('latest')
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
                        export VERSION=${VERSION}

                        cd ${DEPLOY_PATH}

                        docker compose pull ${SERVICE_NAME}
                        docker compose up -d --no-deps \
                          --scale ${SERVICE_NAME}=${COMMON_API_REPLICAS} \
                          ${SERVICE_NAME}
                        """
                    } else {
                        bat """
                        set REGISTRY=${REGISTRY}
                        set ENVIRONMENT=${ENVIRONMENT}
                        set VERSION=${VERSION}

                        cd /d ${DEPLOY_PATH}

                        docker compose pull %SERVICE_NAME%
                        docker compose up -d --no-deps ^
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
            echo "Successfully deployed ${SERVICE_NAME}:${VERSION} (${COMMON_API_REPLICAS} replicas) to ${ENVIRONMENT}"
        }
        failure {
            echo "Deployment failed for ${SERVICE_NAME} in ${ENVIRONMENT}"
        }
    }
}
