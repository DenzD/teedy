#!/usr/bin/env groovy

def call(String name = 'human') {
  x = "http://192.168.0.143:8081/repository/skillup/teedy/${name}/teedy-${name}.war"
  println x
}

def download() {
    sh ("curl -u $MY_CREDS_USR:$MY_CREDS_PSW ${x} --output docs-web-1.10.war")
}

def upload() {
    sshPublisher(publishers: [sshPublisherDesc(configName: 'Tomcat_Server', 
                                                           transfers: [sshTransfer(cleanRemote: false, excludes: '', 
                                                           execCommand: 'sudo systemctl restart tomcat.service', 
                                                           execTimeout: 120000, flatten: false, makeEmptyDirs: false, 
                                                           noDefaultExcludes: false, patternSeparator: '[, ]+', 
                                                           remoteDirectory: '/var/lib/tomcat9/webapps/', remoteDirectorySDF: false, 
                                                           //removePrefix: 'docs-web/target', sourceFiles: '**/*.war')],
                                                           removePrefix: '', sourceFiles: '*.war')],
                            usePromotionTimestamp: false, 
                            useWorkspaceInPromotion: false, 
                            verbose: false)])
}
