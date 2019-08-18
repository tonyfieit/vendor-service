node{
  stage ('Build') {
 
    git url: "https://github.com/tonyfieit/vendor-service.git"
 
    withMaven(maven: 'maven-3', mavenSettingsConfig: 'my-maven-settings') {
 
      // Run the maven build
     sh "mvn package"
     stash name:"jar", includes:"target/vendor-service-1.0.jar"
 
    } // withMaven will discover the generated Maven artifacts, JUnit Surefire & FailSafe & FindBugs & SpotBugs reports...
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
