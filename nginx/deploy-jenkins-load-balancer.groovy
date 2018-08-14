pipeline {
    agent any

    parameters {
        string(defaultValue: "master", description: 'Branch to deploy from', name: 'BRANCH')
    }

    stages {
        stage('Checkout repo') {
            steps {
                script {
                    
                    def commitShaTemp = (BRANCH.length() == 40) ? BRANCH : '*/' + BRANCH
                    def scmVars = checkout([$class: 'GitSCM', branches: [[name: commitShaTemp]], doGenerateSubmoduleConfigurations: false,
                              extensions: [], submoduleCfg: [],
                              userRemoteConfigs: [[credentialsId: 'github',
                                                   url: 'https://github.com/Bconteh/kubernetes-deployments.git']]])
                    env.GIT_COMMIT = scmVars.GIT_COMMIT
                    env.GIT_BRANCH = scmVars.GIT_BRANCH
                    env.GIT_URL = scmVars.GIT_URL
                    echo "'${env.GIT_COMMIT}'"
                    echo "'${env.GIT_BRANCH}'"
                    echo "'${env.GIT_URL}'"
                }
            }
        }
        stage('Build Images') {
            steps {
                script {
                    try {
                        sh     '''
                                cd nginx
                                docker-compose build
                               '''
                    } catch (e) {
                            echo "[Build Images] Docker Image Build failed for ${env.GIT_BRANCH} by commit ${env.GIT_COMMIT} - (<${env.BUILD_URL}|Click here for more info>). Repository address: '${env.GIT_URL}'"
                            error "[Build Images] Build failed for branch ${env.GIT_BRANCH}"
                        }
                    }
                }
        }
        stage('Start Services') {
            steps {
                script {
                    try {
                        sh     '''
                                cd nginx
                                docker-compose up -d
                               '''
                    } catch (e) {
                            echo "[Start Services] Start Services failed for ${env.GIT_BRANCH} by commit ${env.GIT_COMMIT} - (<${env.BUILD_URL}|Click here for more info>). Repository address: '${env.GIT_URL}'"
                            error "[Start Services] Start Services failed for branch ${env.GIT_BRANCH}"

                        }
                    }
                }
        }
        stage('Health Check Service') {
            steps {
                script {
                    try {
                        sh     '''
                                curl localhost:9092 | grep "Hello"
                               '''
                    } catch (e) {
                            echo "[Deploy Mysql] Deployment failed for ${env.GIT_BRANCH} by commit ${env.GIT_COMMIT} - (<${env.BUILD_URL}|Click here for more info>). Repository address: '${env.GIT_URL}'"
                            error "[Deploy MySql] Deployment failed for branch ${env.GIT_BRANCH}"

                        }
                    }
                }
        }
        stage('Post Build Actions') {
            steps {
                script {
                    try {
                        sh     '''
                                echo "Pipeline completed successfully!!!".
                               '''
                    } catch (e) {
                            echo "[Post Build Actions] Post Build failed for ${env.GIT_BRANCH} by commit ${env.GIT_COMMIT} - (<${env.BUILD_URL}|Click here for more info>). Repository address: '${env.GIT_URL}'"
                            error "[Post Build Actions] Post failed for branch ${env.GIT_BRANCH}"
                        }
                    }
                }
        }


    }
}
