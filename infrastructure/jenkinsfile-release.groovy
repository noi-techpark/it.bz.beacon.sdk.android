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
    }

    environment {
        BRANCH = "master"
        VARIANT = 'release'
        TAG = "${params.TAG}"
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
                sh "bundle exec fastlane release"
            }
        }
        stage('Tag') {
            steps {
                sshagent (credentials: ['jenkins_github_ssh_key']) {
                    sh """
                        git config --global user.email 'info@opendatahub.com'
                        git config --global user.name 'Jenkins'
                        git commit -a -m 'Version ${TAG}' --allow-empty
                        git tag -d ${TAG} || true
                        git tag -a ${TAG} -m ${TAG}
                        mkdir -p ~/.ssh
                        ssh-keyscan -H github.com >> ~/.ssh/known_hosts
                        git push origin HEAD:${BRANCH} --follow-tags
                    """
                }
            }
        }
    }
}
