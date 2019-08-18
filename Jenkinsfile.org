node('maven') {
  stage('Build') {
    git url: "https://github.com/tonyfieit/vendor-service.git"
    sh "mvn package"
    stash name:"jar", includes:"target/vendor-service-1.0.jar"
  }
  stage('Test') {
    parallel(
      "Vendor Tests": {
        sh "mvn verify -P vendor-tests"
      },
      "Discount Tests": {
        sh "mvn verify -P discount-tests"
      }
    )
  }
  stage('Build Image') {
    unstash name:"jar"
    sh "oc start-build vendor --from-file=target/vendor-service-1.0.jar --follow"
  }
  stage('Deploy') {
    openshiftDeploy depCfg: 'vendor'
    openshiftVerifyDeployment depCfg: 'vendor', replicaCount: 1, verifyReplicaCount: true
  }
  stage('System Test') {
    sh "curl -si http://localhost:8080/camel/vendor/1"
  }
}
