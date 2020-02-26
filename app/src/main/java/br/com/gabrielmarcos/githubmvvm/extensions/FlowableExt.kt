package br.com.gabrielmarcos.githubmvvm.extensions

import io.reactivex.Flowable
import java.util.concurrent.TimeUnit

private const val RETRY_DELAY = 2000
private const val MAX_RETRIES = 3

fun <T> Flowable<T>.retryWithDelay(
    maxRetries: Int = MAX_RETRIES,
    retryDelayMillis: Int = RETRY_DELAY
): Flowable<T> {
    var retryCount = 0
    return retryWhen { thObservable ->
        thObservable.flatMap { throwable ->
            if (++retryCount < maxRetries) {
                Flowable.timer(retryDelayMillis.toLong(), TimeUnit.MILLISECONDS)
            } else {
                Flowable.error(throwable)
            }
        }
    }
}