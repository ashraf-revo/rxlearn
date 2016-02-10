package com.example.task1

import groovy.transform.Canonical
import rx.Observable
import rx.schedulers.Schedulers

class Main {

    static void main(String[] args) {
        f1()
        sleep(1000)
    }

    static void f1() {
        user().flatMap {
            follower(it).flatMap { x ->
                it.followers = x
                Observable.just(it)
            }
        }
        .subscribe {
            println(it)
        }

    }

    static void f2() {
        user().flatMap {
            follower(it)
        }.collect({ new ArrayList() }, { acc, it ->
            acc.addAll(it)
        })
                .subscribe {
            it.each { x ->
                println x

            }
        }
    }

    static Observable<User> user() {
        return Observable.<User> create {
            it.onNext(new User(id: UUID.randomUUID().toString(), name: "ashraf"))
            it.onNext(new User(id: UUID.randomUUID().toString(), name: "aaaaa"))
            println Thread.currentThread().id
            it.onCompleted()
        }.subscribeOn(Schedulers.newThread())
    }

    static Observable<List<User>> follower(User user) {
        Observable.<List<User>> create {
            def user2 = [new User(id: user.id, name: UUID.randomUUID().toString())]
            user2 << new User(id: user.id, name: UUID.randomUUID().toString())
            it.onNext(user2)
            println Thread.currentThread().id
            it.onCompleted()
        }.subscribeOn(Schedulers.newThread())
    }
}

@Canonical
class User {
    String id
    String name
    List<User> followers

    User plus(User user) {
        this.name += " " + user.name;
        this
    }
}


