package br.com.gabrielmarcos.githubmvvm.extensions

inline fun <R> Boolean.then(block: () -> R): R? {
    if (this) return block()
    return null
}