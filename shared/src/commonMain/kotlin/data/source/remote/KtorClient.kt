package com.attendace.leopard.data.source.remote

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.model.dto.ErrorDto
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.LoginDto
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class MissingPageException(response: HttpResponse, cachedResponseText: String) :
    ResponseException(response, cachedResponseText) {
    override val message: String = "Missing page: ${response.call.request.url}. " +
            "Status: ${response.status}."
}


class ApiClient(
    engine: HttpClientEngine,
    val authSettings: AuthSettings,
) {
    val client = HttpClient(engine) {
        expectSuccess = true
        install(ContentNegotiation) {
            json(Json {
                encodeDefaults = true
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 10000
            requestTimeoutMillis = 10000
            connectTimeoutMillis = 10000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.ALL

        }
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, request ->
                val clientException = exception as? ClientRequestException
                    ?: return@handleResponseExceptionWithRequest
                throw clientException
            }
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
    }
}

suspend inline fun <reified T : Any> ApiClient.makeRequest(
    urlBuilder: URLBuilder,
    methodType: HttpMethod = HttpMethod.Get,
    toAppendHeaders: List<Pair<String, String>> = emptyList(),
    noinline body: (HttpRequestBuilder.() -> Unit)? = null
): Either<Failure.NetworkFailure, T> {
    return try {
        Either.Right(
            this.client.request(
                url = urlBuilder.build()
            ) {
                method = methodType
                headers.append("Authorization", "Bearer ${authSettings.getAccessToken().first()}")

                toAppendHeaders.forEach { (key, value) ->
                    headers.append(key, value)
                }
                body?.let { it() }
            }.body()
        )
    } catch (e: RedirectResponseException) { // for 3xx
        if (e.response.status.value == 401) {
            refreshToken().fold(ifRight = {
                authSettings.setAccessToken(it.access_token)
                authSettings.setRefreshToken(it.refresh_token)
            }, ifLeft = {
                authSettings.setAccessToken("")
                authSettings.setRefreshToken("")
                authSettings.setLoggedIn(false)
                authSettings.setWorkplacesLastModifiedTime("")
            })
        }
        val localizedMessage = kotlin.runCatching {
            e.response.body<ErrorDto>().error
        }.getOrNull()

        Either.Left(Failure.NetworkFailure.RedirectException(e,localizedMessage))
    } catch (e: ClientRequestException) { // for 4 xx
        if (e.response.status.value == 401) {
            refreshToken().fold(ifRight = {
                authSettings.setAccessToken(it.access_token)
                authSettings.setRefreshToken(it.refresh_token)
            }, ifLeft = {
                authSettings.setAccessToken("")
                authSettings.setRefreshToken("")
                authSettings.setLoggedIn(false)
                authSettings.setWorkplacesLastModifiedTime("")
            })
        }
        val localizedMessage = kotlin.runCatching {
            e.response.body<ErrorDto>().error?: e.response.body<ErrorDto>().message
        }.getOrNull()

        Either.Left(Failure.NetworkFailure.ClientException(e, localizedMessage))
    } catch (e: ServerResponseException) { // for 5xx
        if (e.response.status.value == 401) {
            refreshToken().fold(ifRight = {
                authSettings.setAccessToken(it.access_token)
                authSettings.setRefreshToken(it.refresh_token)
            }, ifLeft = {
                authSettings.setAccessToken("")
                authSettings.setRefreshToken("")
                authSettings.setLoggedIn(false)
                authSettings.setWorkplacesLastModifiedTime("")
            })
        }
        Either.Left(Failure.NetworkFailure.ServerException(e))
    } catch (e: Exception) {
        Either.Left(Failure.NetworkFailure.UnknownException(e))
    }
}


suspend fun ApiClient.refreshToken(
    methodType: HttpMethod = HttpMethod.Post,
    body: (HttpRequestBuilder.() -> Unit)? = null
): Either<Failure.NetworkFailure, LoginDto> {
    return try {
        Either.Right(
            this.client.request(
                url = URLBuilder(
                    authSettings.getBaseUrl().first()
                ).apply { path("NFS.Web/oauth/token") }.build()
            ) {
                method = methodType
                body?.let { it() }
                setBody(FormDataContent(
                    Parameters.build {
                        append("grant_type", "refresh_token")
                        append("refresh_token", authSettings.getRefreshToken().first())
                    }
                ))
            }.body()
        )
    } catch (e: RedirectResponseException) { // for 3xx
        val localizedMessage = kotlin.runCatching {
            e.response.body<ErrorDto>().error
        }.getOrNull()

        Either.Left(Failure.NetworkFailure.RedirectException(e, localizedMessage))
    } catch (e: ClientRequestException) { // for 4 xx
        val localizedMessage = kotlin.runCatching {
            e.response.body<ErrorDto>().error ?: e.response.body<ErrorDto>().message
        }.getOrNull()

        Either.Left(Failure.NetworkFailure.ClientException(e, localizedMessage))
    } catch (e: ServerResponseException) { // for 5xx
        Either.Left(Failure.NetworkFailure.ServerException(e))
    } catch (e: Exception) {
        Either.Left(Failure.NetworkFailure.UnknownException(e))
    }
}