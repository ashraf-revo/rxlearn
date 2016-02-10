package com.example.task3

import rx.Observable
import rx.schedulers.Schedulers

import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by ashraf on 2/1/2016.
 */
class main3 {
    public static void main(String[] args) {
        Long start = System.currentTimeMillis()

        Observable<List<String>> allContent = getPaths().flatMap({
            fileContent(it.toString())
        }).toList()
        allContent.subscribe({
            println(System.currentTimeMillis() - start)

        })
        sleep(1000)
    }

    static Observable<String> fileContent(p) {
        return Observable.create({
            it.onNext(new File(p).text)
            it.onCompleted()
        }).subscribeOn(Schedulers.io());
    }

    static Observable<Paths> getPaths() {
        return Observable.<Paths> create({
            Files.walk(Paths.get("src")).filter({
                Files.isRegularFile(it)
            }).forEach({ data ->
                it.onNext(data)
            })
            it.onCompleted()
        }).subscribeOn(Schedulers.newThread());
    }
}
