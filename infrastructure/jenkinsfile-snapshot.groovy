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
        gitParameter name: 'BRANCH', branchFilter: 'origin/(.*)', defaultValue: 'development', type: 'PT_BRANCH'
    }

    environment {
        S3_REPO_ID = 'maven-repo.opendatahub.bz.it'
        S3_REPO_USERNAME = credentials('s3_repo_username')
        S3_REPO_PASSWORD = credentials('s3_repo_password')
        VARIANT = 'snapshot'
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
                sh "TAG='${params.TAG}-SNAPSHOT' bundle exec fastlane appSnapshot"
            }
        }
    }
}
