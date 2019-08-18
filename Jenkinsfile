node{
stage('Build') {
withMaven(
  mavenSettingsConfig: 'b91be174-fd2f-4cf4-9b77-071607082b7a', jdk: jdk.toString(), maven: 'Maven 3.3.9', mavenOpts: '-Xmx2048m') {
   // Run the maven build
   //  sh "mvn package"
    // stash name:"jar", includes:"target/vendor-service-1.0.jar"
    sh 'mvn clean package'
    stash name:"jar", includes:"target/vendor-service-1.0.jar"
      }
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
