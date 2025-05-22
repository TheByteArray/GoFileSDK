package org.thebytearray.gofilesdk.core

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.thebytearray.gofilesdk.api.GofileApi
import org.thebytearray.gofilesdk.models.AccountDetailsResponse
import org.thebytearray.gofilesdk.models.CopyContentRequest
import org.thebytearray.gofilesdk.models.CopyContentResponse
import org.thebytearray.gofilesdk.models.CreateDirectLinkRequest
import org.thebytearray.gofilesdk.models.CreateFolderRequest
import org.thebytearray.gofilesdk.models.CreateFolderResponse
import org.thebytearray.gofilesdk.models.DeleteContentRequest
import org.thebytearray.gofilesdk.models.DeleteContentResponse
import org.thebytearray.gofilesdk.models.DeleteDirectLinkResponse
import org.thebytearray.gofilesdk.models.DirectLinkResponse
import org.thebytearray.gofilesdk.models.FolderDetailsResponse
import org.thebytearray.gofilesdk.models.ImportContentRequest
import org.thebytearray.gofilesdk.models.ImportContentResponse
import org.thebytearray.gofilesdk.models.MoveContentRequest
import org.thebytearray.gofilesdk.models.MoveContentResponse
import org.thebytearray.gofilesdk.models.SearchResponse
import org.thebytearray.gofilesdk.models.UpdateContentRequest
import org.thebytearray.gofilesdk.models.UpdateContentResponse
import org.thebytearray.gofilesdk.models.UpdateDirectLinkRequest
import org.thebytearray.gofilesdk.models.UploadResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Main entry point for the Gofile SDK.
 * This class provides a high-level interface for interacting with the Gofile API.
 * It handles authentication, request building, and response parsing.
 *
 * @property apiToken The API token used for authentication
 * @property uploadBaseUrl The base URL for file uploads
 */
class GofileSDK private constructor(
    private val apiToken: String,
    uploadBaseUrl: String
) {
    private val apiRetrofit: Retrofit
    private val uploadRetrofit: Retrofit
    private val api: GofileApi
    private val uploadApi: GofileApi

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiToken")
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        apiRetrofit = Retrofit.Builder()
            .baseUrl("https://api.gofile.io/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        uploadRetrofit = Retrofit.Builder()
            .baseUrl(uploadBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = apiRetrofit.create(GofileApi::class.java)
        uploadApi = uploadRetrofit.create(GofileApi::class.java)
    }

    companion object {
        private var instance: GofileSDK? = null

        /**
         * Gets the singleton instance of the GofileSDK.
         * @param apiToken The API token for authentication
         * @param uploadBaseUrl The base URL for file uploads
         * @return The GofileSDK instance
         */
        fun getInstance(apiToken: String, uploadBaseUrl: String = "https://store1.gofile.io/"): GofileSDK {
            return instance ?: synchronized(this) {
                instance ?: GofileSDK(apiToken, uploadBaseUrl).also { instance = it }
            }
        }
    }

    /**
     * Gets the best server for file uploads.
     * @return A [Result] containing the server URL or an error
     */
    suspend fun getBestServer(): Result<String> = runCatching {
        val response = api.getBestServer()
        if (response.isSuccessful) {
            response.body()?.data?.server ?: throw Exception("Server data is null")
        } else {
            throw Exception("Failed to get best server: ${response.code()}")
        }
    }

    /**
     * Uploads a file to Gofile.
     * @param file The file to upload
     * @param folderId Optional folder ID to upload to
     * @return A [Result] containing the upload response or an error
     */
    suspend fun uploadFile(file: File, folderId: String? = null): Result<UploadResponse> = runCatching {
        val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        
        val folderIdPart = folderId?.let {
            it.toRequestBody("text/plain".toMediaTypeOrNull())
        }

        val response = uploadApi.uploadFile(filePart, folderId)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Upload response is null")
        } else {
            throw Exception("Upload failed: ${response.code()}")
        }
    }

    /**
     * Creates a new folder.
     * @param parentFolderId The ID of the parent folder
     * @param folderName The name of the new folder
     * @return A [Result] containing the folder creation response or an error
     */
    suspend fun createFolder(parentFolderId: String, folderName: String): Result<CreateFolderResponse> = runCatching {
        val request = CreateFolderRequest(parentFolderId, folderName)
        val response = api.createFolder(request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Create folder response is null")
        } else {
            throw Exception("Create folder failed: ${response.code()}")
        }
    }

    /**
     * Updates content attributes.
     * @param contentId The ID of the content to update
     * @param attribute The attribute to update
     * @param attributeValue The new value for the attribute
     * @return A [Result] containing the update response or an error
     */
    suspend fun updateContent(
        contentId: String,
        attribute: String,
        attributeValue: String
    ): Result<UpdateContentResponse> = runCatching {
        val request = UpdateContentRequest(attribute, attributeValue)
        val response = api.updateContent(contentId, request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Update content response is null")
        } else {
            throw Exception("Update content failed: ${response.code()}")
        }
    }

    /**
     * Deletes content.
     * @param contentsId The ID of the content to delete
     * @return A [Result] containing the deletion response or an error
     */
    suspend fun deleteContent(contentsId: String): Result<DeleteContentResponse> = runCatching {
        val request = DeleteContentRequest(contentsId)
        val response = api.deleteContent(request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Delete content response is null")
        } else {
            throw Exception("Delete content failed: ${response.code()}")
        }
    }

    /**
     * Gets folder details.
     * @param folderId The ID of the folder
     * @param password Optional password for protected folders
     * @return A [Result] containing the folder details or an error
     */
    suspend fun getFolderDetails(
        folderId: String,
        password: String? = null
    ): Result<FolderDetailsResponse> = runCatching {
        val response = api.getFolderDetails(folderId, password)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Folder details response is null")
        } else {
            throw Exception("Get folder details failed: ${response.code()}")
        }
    }

    /**
     * Searches for content within a folder.
     * @param contentId The ID of the folder to search in
     * @param searchString The search term
     * @return A [Result] containing the search results or an error
     */
    suspend fun searchWithinFolder(
        contentId: String,
        searchString: String
    ): Result<SearchResponse> = runCatching {
        val response = api.searchWithinFolder(contentId, searchString)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Search response is null")
        } else {
            throw Exception("Search failed: ${response.code()}")
        }
    }

    /**
     * Creates a direct link.
     * @param contentId The ID of the content
     * @param expireTime Optional expiration time in milliseconds
     * @param sourceIpsAllowed Optional list of allowed source IPs
     * @param domainsAllowed Optional list of allowed domains
     * @param auth Optional list of authentication credentials
     * @return A [Result] containing the direct link response or an error
     */
    suspend fun createDirectLink(
        contentId: String,
        expireTime: Long? = null,
        sourceIpsAllowed: List<String>? = null,
        domainsAllowed: List<String>? = null,
        auth: List<String>? = null
    ): Result<DirectLinkResponse> = runCatching {
        val request = CreateDirectLinkRequest(expireTime, sourceIpsAllowed, domainsAllowed, auth)
        val response = api.createDirectLink(contentId, request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Create direct link response is null")
        } else {
            throw Exception("Create direct link failed: ${response.code()}")
        }
    }

    /**
     * Updates a direct link.
     * @param contentId The ID of the content
     * @param directLinkId The ID of the direct link
     * @param expireTime Optional expiration time in milliseconds
     * @param sourceIpsAllowed Optional list of allowed source IPs
     * @param domainsAllowed Optional list of allowed domains
     * @param auth Optional list of authentication credentials
     * @return A [Result] containing the updated direct link response or an error
     */
    suspend fun updateDirectLink(
        contentId: String,
        directLinkId: String,
        expireTime: Long? = null,
        sourceIpsAllowed: List<String>? = null,
        domainsAllowed: List<String>? = null,
        auth: List<String>? = null
    ): Result<DirectLinkResponse> = runCatching {
        val request = UpdateDirectLinkRequest(expireTime, sourceIpsAllowed, domainsAllowed, auth)
        val response = api.updateDirectLink(contentId, directLinkId, request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Update direct link response is null")
        } else {
            throw Exception("Update direct link failed: ${response.code()}")
        }
    }

    /**
     * Deletes a direct link.
     * @param contentId The ID of the content
     * @param directLinkId The ID of the direct link
     * @return A [Result] containing the deletion response or an error
     */
    suspend fun deleteDirectLink(
        contentId: String,
        directLinkId: String
    ): Result<DeleteDirectLinkResponse> = runCatching {
        val response = api.deleteDirectLink(contentId, directLinkId)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Delete direct link response is null")
        } else {
            throw Exception("Delete direct link failed: ${response.code()}")
        }
    }

    /**
     * Copies content.
     * @param contentsId The ID of the content to copy
     * @param folderId The ID of the destination folder
     * @return A [Result] containing the copy response or an error
     */
    suspend fun copyContent(
        contentsId: String,
        folderId: String
    ): Result<CopyContentResponse> = runCatching {
        val request = CopyContentRequest(contentsId, folderId)
        val response = api.copyContent(request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Copy content response is null")
        } else {
            throw Exception("Copy content failed: ${response.code()}")
        }
    }

    /**
     * Moves content.
     * @param contentsId The ID of the content to move
     * @param folderId The ID of the destination folder
     * @return A [Result] containing the move response or an error
     */
    suspend fun moveContent(
        contentsId: String,
        folderId: String
    ): Result<MoveContentResponse> = runCatching {
        val request = MoveContentRequest(contentsId, folderId)
        val response = api.moveContent(request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Move content response is null")
        } else {
            throw Exception("Move content failed: ${response.code()}")
        }
    }

    /**
     * Imports public content.
     * @param contentsId The ID of the content to import
     * @return A [Result] containing the import response or an error
     */
    suspend fun importPublicContent(contentsId: String): Result<ImportContentResponse> = runCatching {
        val request = ImportContentRequest(contentsId)
        val response = api.importPublicContent(request)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Import content response is null")
        } else {
            throw Exception("Import content failed: ${response.code()}")
        }
    }

    /**
     * Gets the account ID associated with the current API token.
     * @return A [Result] containing the account ID or an error
     */
    suspend fun getAccountId(): Result<String> = runCatching {
        val response = api.getAccountId()
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Account ID is null")
        } else {
            throw Exception("Get account ID failed: ${response.code()}")
        }
    }

    /**
     * Gets account details.
     * @param accountId The ID of the account
     * @return A [Result] containing the account details or an error
     */
    suspend fun getAccountDetails(accountId: String): Result<AccountDetailsResponse> = runCatching {
        val response = api.getAccountDetails(accountId)
        if (response.isSuccessful) {
            response.body() ?: throw Exception("Account details response is null")
        } else {
            throw Exception("Get account details failed: ${response.code()}")
        }
    }

    /**
     * Resets the authentication token for an account.
     * @param accountId The ID of the account
     * @return A [Result] containing the new token or an error
     */
    suspend fun resetAuthToken(accountId: String): Result<String> = runCatching {
        val response = api.resetAuthToken(accountId)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("New token is null")
        } else {
            throw Exception("Reset token failed: ${response.code()}")
        }
    }
} 