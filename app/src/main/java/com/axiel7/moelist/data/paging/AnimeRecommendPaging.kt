package com.axiel7.moelist.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.axiel7.moelist.data.model.ApiParams
import com.axiel7.moelist.data.model.anime.AnimeList
import com.axiel7.moelist.data.network.Api

class AnimeRecommendPaging(
    private val api: Api,
    private val apiParams: ApiParams
) : PagingSource<String, AnimeList>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, AnimeList> {
        return try {
            val nextPage = params.key
            val response = if (nextPage == null) {
                api.getAnimeRecommendations(apiParams)
            } else {
                api.getAnimeRecommendations(nextPage)
            }
            LoadResult.Page(
                data = response.data!!,
                prevKey = null,
                nextKey = response.paging?.next
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, AnimeList>): String? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)

            anchorPage?.prevKey ?: anchorPage?.nextKey
        }
    }
}