#!/usr/bin/env groovy



def call(String name = 'human') {
  //x = "http://192.168.0.143:8081/repository/skillup/teedy/${name}/teedy-${name}.war"
  //println x
  String a = "${name}"
  String[] str;       
  str = a.split('-');              
  for( String values : str )       
  //println(values);
  project_name = str[0]
  project_number = str[1]
  x = "http://192.168.0.143:8081/repository/skillup/${project_name}/${project_number}/${name}.war"
  println x
}

//fuction for deploy job with .war file
def deploy_war() {
    sh ("curl -u $MY_CREDS_USR:$MY_CREDS_PSW ${x} --output docs-web-1.10.war")
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


def list_images() {
properties([
    parameters([
        [$class: 'ChoiceParameter',
            choiceType: 'PT_SINGLE_SELECT',
            description: 'Select a choice',
            filterLength: 1,
            filterable: false,
            name: 'Images',
            script: [$class: 'GroovyScript',
                     fallbackScript: [classpath: [], sandbox: false, script: 'return ["Could not get images"]'],
                script: [classpath: [], sandbox: false, 
                    script: """
                        import groovy.json.JsonSlurperClassic
                        String a = "${params.url}"
                        String[] str
                        str = a.split('teddy/web:')
                        for( String values : str )
                        registry_url = str[0]
                        build_number  = str[1]
                        int number = build_number as Integer
                        project_name = "teddy"
                        def list = []
                        for(int i = number;i<number+9;i++) {
                            list << registry_url+project_name+"/web:"+(i-9)
                        }
                        list << a
                        return list
                    """
                ]]]])
])
}

def dwn_nexus() {
    withCredentials([usernamePassword(credentialsId: 'jenkins-nexus', passwordVariable: 'password', usernameVariable: 'user')]) {
                sh 'docker login -u biba -p $password ${registry}'
                }
                sh "docker pull ${params.url}"
                sh 'docker logout ${registry}'
                
                if (params.ENVIRONMENT == 'Dev') {
                                                  sh 'docker stop web'
                                                  sh ("docker run -d --rm --name web -p 8080:8080 ${params.url}")
                }
                else if (params.ENVIRONMENT == 'Prod') {
                                                  sh 'docker stop web'
                                                  sh ("docker run -d --rm --name web -p 8080:8080 ${params.url}")           
                }
}
