package org.thebytearray.gofilesdk

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import org.thebytearray.gofilesdk.api.GofileApi
import org.thebytearray.gofilesdk.models.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Main entry point for the Gofile SDK.
 * This class provides methods to interact with the Gofile API.
 */
class GofileSDK private constructor(
    private val apiToken: String?,
    private val baseUrl: String,
    uploadBaseUrl: String
) {
    private val api: GofileApi
    private val uploadApi: GofileApi

    init {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().apply {
                    apiToken?.let { addHeader("Authorization", "Bearer $it") }
                }.build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val uploadRetrofit = Retrofit.Builder()
            .baseUrl(uploadBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(GofileApi::class.java)
        uploadApi = uploadRetrofit.create(GofileApi::class.java)
    }

    /**
     * Gets the best server for file uploads.
     * @return A [Result] containing the server URL if successful, or an exception if failed.
     */
    suspend fun getBestServer(): Result<String> = runCatching {
        val response = api.getBestServer()
        if (response.isSuccessful) {
            response.body()?.data?.server ?: throw Exception("Get best server failed: No data received")
        } else {
            throw Exception("Get best server failed: ${response.code()}")
        }
    }

    /**
     * Uploads a file to Gofile.
     * @param file The file to upload
     * @param folderId Optional folder ID to upload to
     * @return A [Result] containing the upload data if successful, or an exception if failed
     */
    suspend fun uploadFile(file: File, folderId: String? = null): Result<UploadData> = runCatching {
        val requestFile = file.asRequestBody("application/octet-stream".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)
        val folderIdPart = folderId?.let {
            MultipartBody.Part.createFormData("folderId", it)
        }

        val response = uploadApi.uploadFile(filePart, folderId)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Upload failed: No data received")
        } else {
            throw Exception("Upload failed: ${response.code()}")
        }
    }

    /**
     * Creates a new folder in Gofile.
     * @param parentFolderId The ID of the parent folder
     * @param folderName The name of the new folder
     * @return A [Result] containing the folder data if successful, or an exception if failed
     */
    suspend fun createFolder(parentFolderId: String, folderName: String): Result<CreateFolderData> = runCatching {
        val request = CreateFolderRequest(parentFolderId, folderName)
        val response = api.createFolder(request)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Create folder failed: No data received")
        } else {
            throw Exception("Create folder failed: ${response.code()}")
        }
    }

    /**
     * Updates content attributes in Gofile.
     * @param contentId The ID of the content to update
     * @param attribute The attribute to update
     * @param attributeValue The new value for the attribute
     * @return A [Result] containing the updated content data if successful, or an exception if failed
     */
    suspend fun updateContent(
        contentId: String,
        attribute: String,
        attributeValue: Any
    ): Result<UpdateContentData> = runCatching {
        val request = UpdateContentRequest(attribute, attributeValue)
        val response = api.updateContent(contentId, request)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Update content failed: No data received")
        } else {
            throw Exception("Update content failed: ${response.code()}")
        }
    }

    /**
     * Deletes content from Gofile.
     * @param contentIds List of content IDs to delete
     * @return A [Result] containing Unit if successful, or an exception if failed
     */
    suspend fun deleteContent(contentIds: List<String>): Result<Unit> = runCatching {
        val request = DeleteContentRequest(contentIds.joinToString(","))
        val response = api.deleteContent(request)
        if (!response.isSuccessful) {
            throw Exception("Delete content failed: ${response.code()}")
        }
    }

    /**
     * Gets details of a folder.
     * @param folderId The ID of the folder
     * @param password Optional password for protected folders
     * @return A [Result] containing the folder details if successful, or an exception if failed
     */
    suspend fun getFolderDetails(folderId: String, password: String? = null): Result<FolderDetailsData> = runCatching {
        val response = api.getFolderDetails(folderId, password)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Get folder details failed: No data received")
        } else {
            throw Exception("Get folder details failed: ${response.code()}")
        }
    }

    /**
     * Searches for content within a folder.
     * @param contentId The ID of the folder to search in
     * @param searchString The search term
     * @return A [Result] containing the search results if successful, or an exception if failed
     */
    suspend fun searchWithinFolder(contentId: String, searchString: String): Result<List<ContentItem>> = runCatching {
        val response = api.searchWithinFolder(contentId, searchString)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Search failed: No data received")
        } else {
            throw Exception("Search failed: ${response.code()}")
        }
    }

    /**
     * Creates a direct link for content.
     * @param contentId The ID of the content
     * @param expireTime Optional expiration time in milliseconds
     * @param sourceIpsAllowed Optional list of allowed source IPs
     * @param domainsAllowed Optional list of allowed domains
     * @param auth Optional list of authentication credentials
     * @return A [Result] containing the direct link data if successful, or an exception if failed
     */
    suspend fun createDirectLink(
        contentId: String,
        expireTime: Long? = null,
        sourceIpsAllowed: List<String>? = null,
        domainsAllowed: List<String>? = null,
        auth: List<String>? = null
    ): Result<DirectLinkData> = runCatching {
        val request = CreateDirectLinkRequest(expireTime, sourceIpsAllowed, domainsAllowed, auth)
        val response = api.createDirectLink(contentId, request)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Create direct link failed: No data received")
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
     * @return A [Result] containing the updated direct link data if successful, or an exception if failed
     */
    suspend fun updateDirectLink(
        contentId: String,
        directLinkId: String,
        expireTime: Long? = null,
        sourceIpsAllowed: List<String>? = null,
        domainsAllowed: List<String>? = null,
        auth: List<String>? = null
    ): Result<DirectLinkData> = runCatching {
        val request = UpdateDirectLinkRequest(expireTime, sourceIpsAllowed, domainsAllowed, auth)
        val response = api.updateDirectLink(contentId, directLinkId, request)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Update direct link failed: No data received")
        } else {
            throw Exception("Update direct link failed: ${response.code()}")
        }
    }

    /**
     * Deletes a direct link.
     * @param contentId The ID of the content
     * @param directLinkId The ID of the direct link
     * @return A [Result] containing Unit if successful, or an exception if failed
     */
    suspend fun deleteDirectLink(contentId: String, directLinkId: String): Result<Unit> = runCatching {
        val response = api.deleteDirectLink(contentId, directLinkId)
        if (!response.isSuccessful) {
            throw Exception("Delete direct link failed: ${response.code()}")
        }
    }

    /**
     * Copies content to a destination folder.
     * @param contentIds List of content IDs to copy
     * @param destinationFolderId The ID of the destination folder
     * @return A [Result] containing the list of copied content IDs if successful, or an exception if failed
     */
    suspend fun copyContent(contentIds: List<String>, destinationFolderId: String): Result<List<String>> = runCatching {
        val request = CopyContentRequest(contentIds.joinToString(","), destinationFolderId)
        val response = api.copyContent(request)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Copy content failed: No data received")
        } else {
            throw Exception("Copy content failed: ${response.code()}")
        }
    }

    /**
     * Moves content to a destination folder.
     * @param contentIds List of content IDs to move
     * @param destinationFolderId The ID of the destination folder
     * @return A [Result] containing Unit if successful, or an exception if failed
     */
    suspend fun moveContent(contentIds: List<String>, destinationFolderId: String): Result<Unit> = runCatching {
        val request = MoveContentRequest(contentIds.joinToString(","), destinationFolderId)
        val response = api.moveContent(request)
        if (!response.isSuccessful) {
            throw Exception("Move content failed: ${response.code()}")
        }
    }

    /**
     * Imports public content.
     * @param contentIds List of public content IDs to import
     * @return A [Result] containing the list of imported content IDs if successful, or an exception if failed
     */
    suspend fun importPublicContent(contentIds: List<String>): Result<List<String>> = runCatching {
        val request = ImportContentRequest(contentIds.joinToString(","))
        val response = api.importPublicContent(request)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Import content failed: No data received")
        } else {
            throw Exception("Import content failed: ${response.code()}")
        }
    }

    /**
     * Gets the account ID associated with the current API token.
     * @return A [Result] containing the account ID if successful, or an exception if failed
     */
    suspend fun getAccountId(): Result<String> = runCatching {
        val response = api.getAccountId()
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Get account ID failed: No data received")
        } else {
            throw Exception("Get account ID failed: ${response.code()}")
        }
    }

    /**
     * Gets account details.
     * @param accountId The ID of the account
     * @return A [Result] containing the account details if successful, or an exception if failed
     */
    suspend fun getAccountDetails(accountId: String): Result<AccountDetailsData> = runCatching {
        val response = api.getAccountDetails(accountId)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Get account details failed: No data received")
        } else {
            throw Exception("Get account details failed: ${response.code()}")
        }
    }

    /**
     * Resets the authentication token for an account.
     * @param accountId The ID of the account
     * @return A [Result] containing the new token if successful, or an exception if failed
     */
    suspend fun resetAuthToken(accountId: String): Result<String> = runCatching {
        val response = api.resetAuthToken(accountId)
        if (response.isSuccessful) {
            response.body()?.data ?: throw Exception("Reset auth token failed: No data received")
        } else {
            throw Exception("Reset auth token failed: ${response.code()}")
        }
    }

    companion object {
        private const val DEFAULT_API_URL = "https://api.gofile.io/"
        private const val DEFAULT_UPLOAD_URL = "https://upload.gofile.io/"

        @Volatile
        private var instance: GofileSDK? = null

        /**
         * Gets the singleton instance of GofileSDK.
         * @param apiToken Optional API token for authenticated operations
         * @param baseUrl Optional custom API base URL
         * @param uploadBaseUrl Optional custom upload base URL
         * @return The GofileSDK instance
         */
        fun getInstance(
            apiToken: String? = null,
            baseUrl: String = DEFAULT_API_URL,
            uploadBaseUrl: String = DEFAULT_UPLOAD_URL
        ): GofileSDK {
            return instance ?: synchronized(this) {
                instance ?: GofileSDK(apiToken, baseUrl, uploadBaseUrl).also { instance = it }
            }
        }
    }
} 