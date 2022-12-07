pipeline{
    agent{
        any
    }
    stages{
        stage('build'){
            steps{
                sh """
                    mvn -B compile
                """
            }
        }
        post {
            always {
                archiveArtifacts artifacts: 'src/main/resources/orders/*.txt'
            }
        }
    }
}