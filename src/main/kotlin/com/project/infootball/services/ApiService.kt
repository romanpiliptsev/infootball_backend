package com.project.infootball.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class ApiService {
    @Value("\${football-api.token}")
    private val token: String? = null

    fun fetchApi(endpoint: String): String? {
        val request: HttpRequest = HttpRequest.newBuilder()
            .uri(URI.create("https://api.football-data.org/v4/${endpoint}"))
            .header("X-Auth-Token", token)
            .build()

        val response: HttpResponse<String> =
            HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())

        return response.body()
    }

    fun getStandingsByCode(code: String): String? {
        return fetchApi("competitions/${code}/standings")
    }

    fun getScorersByCode(code: String): String? {
        return fetchApi("competitions/${code}/scorers")
    }

    fun getTodayMatches(): String? {
        return fetchApi("matches")
    }

    fun getTeamMatches(code: String): String? {
        return fetchApi("teams/${code}/matches")
    }
}