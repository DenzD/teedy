#!/usr/bin/env groovy

def call(String name = 'human') {
  x = "http://192.168.0.143:8081/repository/skillup/teedy/${name}/teedy-${name}.war"
  println x
}

def hi() {
    println("Hello World!")
}
