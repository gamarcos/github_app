package br.com.gabrielmarcos.githubmvvm.gist

import br.com.gabrielmarcos.githubmvvm.model.Gist

object GistMapper {
    fun map(
        gist: List<Gist>, favsIds: List<String>
    ): List<Gist> {
        if (gist.isNullOrEmpty() or favsIds.isNullOrEmpty()) return gist
        val mapperGist = ArrayList<Gist>()
        gist.forEach {
            mapperGist.add(
                Gist(
                    gistId = it.gistId,
                    description = it.description,
                    createdAt = it.createdAt,
                    files = it.files,
                    owner = it.owner,
                    starred = favsIds.contains(it.gistId)
                )
            )
        }
        return mapperGist
    }
}