package com.example.task4

import rx.Observable
import rx.schedulers.Schedulers

import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

/**
 * Created by ashraf on 2/1/2016.
 */
class main4 {
    public static void main(String[] args) {
        Long start = System.currentTimeMillis()
        Observable<List<String>> allContent = getPaths().flatMap({
            Observable.from(it).flatMap({
                fileContent(it.toString())
            })
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

    static Observable<List<Paths>> getPaths() {
        return Observable.<List<Paths>> create({
            it.onNext(Files.walk(Paths.get("src")).filter({
                Files.isRegularFile(it)
            }).collect(Collectors.toList()))
            it.onCompleted()
        }).subscribeOn(Schedulers.newThread());
    }
}
