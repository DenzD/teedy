#!/usr/bin/env groovy

def call(String name = 'human') {
  x = "http://192.168.0.143:8081/repository/skillup/teedy/${name}/teedy-${name}.war"
  println x
}

def hi() {
    println("Hello World!")
    sh ("curl -u $MY_CREDS_USR:$MY_CREDS_PSW ${x} --output docs-web-1.10.war")
}
