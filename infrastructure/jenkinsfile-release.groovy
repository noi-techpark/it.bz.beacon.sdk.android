pipeline {
    agent {
        node {
            label 'mac'
        }
    }

    options {
        ansiColor('xterm')
    }    

    parameters {
        string(name: 'TAG', defaultValue: '1.0.0', description: 'Tag')
        gitParameter name: 'BRANCH', branchFilter: 'origin/(.*)', defaultValue: 'master', type: 'PT_BRANCH'
    }

    environment {
        VARIANT = 'release'
        S3_REPO_URL = "s3://it.bz.opendatahub/${VARIANT}"
        S3_REPO_USERNAME = credentials('s3_repo_username')
        S3_REPO_PASSWORD = credentials('s3_repo_password')
    }

    stages {
        stage('Dependencies') {
            steps {
                sh 'bundle install --path=vendor/bundle'
                sh 'bundle update'
            }
        }
        stage('Test') {
            steps {
                sh 'bundle exec fastlane test'
            }
        }
        stage('Release') {
            steps {
                sh "TAG='${params.TAG}' bundle exec fastlane release"
            }
        }
        stage('Config jitpack.yml') {
            steps {
                sh '''
                    echo "Send environmental variables to jitpack.io"
                    sed -ie 's/__VARIANT__/${VARIANT}/g' jitpack.yml
                    sed -ie 's/__TAG__/${params.TAG}/g' jitpack.yml
                '''
            }
        }
        stage('Tag') {
            steps {
                sshagent (credentials: ['jenkins_github_ssh_key']) {
                    sh "git config --global user.email 'info@opendatahub.bz.it'"
                    sh "git config --global user.name 'Jenkins'"
                    sh "git commit -a -m 'Version ${params.TAG}' --allow-empty"
                    sh "git tag -d ${params.TAG} || true"
                    sh "git tag -a ${params.TAG} -m ${params.TAG}"
                    sh "mkdir -p ~/.ssh"
                    sh "ssh-keyscan -H github.com >> ~/.ssh/known_hosts"
                    sh "git push origin HEAD:${params.BRANCH} --follow-tags"
                }
            }
        }
    }
}
