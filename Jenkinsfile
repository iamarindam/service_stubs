@Library('zapp-utilities') _

continuousBuildBoot {
    maven_v = 'Maven 3.6.1'
    java_v = 'openjdk11.28'
    build_goal = 'clean install -Pjenkins -DskipTests'
    test_goal = 'test -Djacoco.reportPath.dir=$WORKSPACE/target'
    it_test_goal = 'clean install test -Pjenkins -e -rf :ap-p2p-sm-payment-execution-itests'
    sonar_goal = 'sonar:sonar -Psonar,dev-env-v3,test-ci -Dsonar.projectKey=ap-p2p-sm-payment-execution -Dsonar.login=${sonar_credentials} -Dsonar.branch.name=$BRANCH_NAME'
    branch = 'develop'
    target = 'develop'
    env = 'cb'
    component = 'ap-p2p-sm-payment-execution'
    settings_id = 'pwba-settings'
    agent = 'zapp-dev-env2'
}