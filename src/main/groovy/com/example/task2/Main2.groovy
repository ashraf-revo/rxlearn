package com.example.task2

import groovy.transform.Canonical
import rx.Observable
import rx.schedulers.Schedulers

/**
 * Created by ashraf on 1/31/2016.
 */
class Main {
    public static void main(String[] args) {
        findUser(1).subscribe({
            println "follower*****************************************"
            it.follower.each {
                println(it.name)
            }
            println "post*********************************************"
            it.post.each {
                println(it.content)
            }
            println "notification**************************************"
            it.notification.each {
                println(it.content)
            }

        })
        sleep(1000)
    }

    static Observable<User> findUser(id) {
        Observable.<User> create({
            it.onNext(new User(id: id, name: UUID.randomUUID().toString()))
            it.onCompleted()
        }).subscribeOn(Schedulers.newThread()).flatMap({
            Observable.zip(findFollower(it.id), findPost(it.id), findNotification(it.id), { f, p, n ->
                it.follower = f
                it.post = p
                it.notification = n
                it
            })
        })
    }

    static Observable<List<User>> findFollower(id) {
        return Observable.<List<User>> create({
            List<User> follower = []
            for (i in 0..10) {
                follower << new User(id: i, name: UUID.randomUUID().toString())
            }
            it.onNext(follower)
            debug()
            it.onCompleted()
        }).subscribeOn(Schedulers.newThread())
    }

    static Observable<List<Post>> findPost(id) {
        return Observable.<List<User>> create({
            List<Post> post = []
            for (i in 0..10) {
                post << new Post(id: i, content: UUID.randomUUID().toString())
            }
            it.onNext(post)
            debug()
            it.onCompleted()
        }).subscribeOn(Schedulers.newThread())
    }

    static Observable<List<Notification>> findNotification(id) {
        return Observable.<List<Notification>> create({
            List<Notification> notification = []
            for (i in 0..10) {
                notification << new Notification(id: i, content: UUID.randomUUID().toString())
            }
            it.onNext(notification)
            debug()
            it.onCompleted()
        }).subscribeOn(Schedulers.newThread())
    }

    static void debug() {
        println Thread.currentThread().id
    }
}
@Canonical
class User {
    Long id
    String name
    List<User> follower
    List<Post> post
    List<Notification> notification
}

class Post {
    Long id
    String content
    Date CreatedDate = new Date()
}

class Notification {
    Long id
    String content
}