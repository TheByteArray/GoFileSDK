package org.thebytearray.gofilesdk.api

import okhttp3.MultipartBody
import org.thebytearray.gofilesdk.models.AccountDetailsResponse
import org.thebytearray.gofilesdk.models.AccountIdResponse
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
import org.thebytearray.gofilesdk.models.ResetTokenResponse
import org.thebytearray.gofilesdk.models.SearchResponse
import org.thebytearray.gofilesdk.models.ServerResponse
import org.thebytearray.gofilesdk.models.UpdateContentRequest
import org.thebytearray.gofilesdk.models.UpdateContentResponse
import org.thebytearray.gofilesdk.models.UpdateDirectLinkRequest
import org.thebytearray.gofilesdk.models.UploadResponse
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit interface for the Gofile API.
 * This interface defines all the API endpoints and their request/response types.
 */
interface GofileApi {
    /**
     * Gets the best server for file uploads.
     * @return A [Response] containing the server information
     */
    @GET("getServer")
    suspend fun getBestServer(): Response<ServerResponse>

    /**
     * Uploads a file to Gofile.
     * @param file The file to upload
     * @param folderId Optional folder ID to upload to
     * @return A [Response] containing the upload information
     */
    @Multipart
    @POST("uploadfile")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("folderId") folderId: String? = null
    ): Response<UploadResponse>

    /**
     * Creates a new folder.
     * @param request The folder creation request
     * @return A [Response] containing the folder information
     */
    @POST("contents/createFolder")
    suspend fun createFolder(
        @Body request: CreateFolderRequest
    ): Response<CreateFolderResponse>

    /**
     * Updates content attributes.
     * @param contentId The ID of the content to update
     * @param request The update request
     * @return A [Response] containing the updated content information
     */
    @PUT("contents/{contentId}/update")
    suspend fun updateContent(
        @Path("contentId") contentId: String,
        @Body request: UpdateContentRequest
    ): Response<UpdateContentResponse>

    /**
     * Deletes content.
     * @param request The deletion request
     * @return A [Response] containing the deletion status
     */
    @DELETE("contents")
    suspend fun deleteContent(
        @Body request: DeleteContentRequest
    ): Response<DeleteContentResponse>

    /**
     * Gets folder details.
     * @param folderId The ID of the folder
     * @param password Optional password for protected folders
     * @return A [Response] containing the folder details
     */
    @GET("contents/{folderId}")
    suspend fun getFolderDetails(
        @Path("folderId") folderId: String,
        @Query("password") password: String? = null
    ): Response<FolderDetailsResponse>

    /**
     * Searches for content within a folder.
     * @param contentId The ID of the folder to search in
     * @param searchString The search term
     * @return A [Response] containing the search results
     */
    @GET("contents/search")
    suspend fun searchWithinFolder(
        @Query("contentId") contentId: String,
        @Query("searchedString") searchString: String
    ): Response<SearchResponse>

    /**
     * Creates a direct link.
     * @param contentId The ID of the content
     * @param request The direct link creation request
     * @return A [Response] containing the direct link information
     */
    @POST("contents/{contentId}/directlinks")
    suspend fun createDirectLink(
        @Path("contentId") contentId: String,
        @Body request: CreateDirectLinkRequest
    ): Response<DirectLinkResponse>

    /**
     * Updates a direct link.
     * @param contentId The ID of the content
     * @param directLinkId The ID of the direct link
     * @param request The direct link update request
     * @return A [Response] containing the updated direct link information
     */
    @PUT("contents/{contentId}/directlinks/{directLinkId}")
    suspend fun updateDirectLink(
        @Path("contentId") contentId: String,
        @Path("directLinkId") directLinkId: String,
        @Body request: UpdateDirectLinkRequest
    ): Response<DirectLinkResponse>

    /**
     * Deletes a direct link.
     * @param contentId The ID of the content
     * @param directLinkId The ID of the direct link
     * @return A [Response] containing the deletion status
     */
    @DELETE("contents/{contentId}/directlinks/{directLinkId}")
    suspend fun deleteDirectLink(
        @Path("contentId") contentId: String,
        @Path("directLinkId") directLinkId: String
    ): Response<DeleteDirectLinkResponse>

    /**
     * Copies content.
     * @param request The copy request
     * @return A [Response] containing the copy operation results
     */
    @POST("contents/copy")
    suspend fun copyContent(
        @Body request: CopyContentRequest
    ): Response<CopyContentResponse>

    /**
     * Moves content.
     * @param request The move request
     * @return A [Response] containing the move operation status
     */
    @PUT("contents/move")
    suspend fun moveContent(
        @Body request: MoveContentRequest
    ): Response<MoveContentResponse>

    /**
     * Imports public content.
     * @param request The import request
     * @return A [Response] containing the import operation results
     */
    @POST("contents/import")
    suspend fun importPublicContent(
        @Body request: ImportContentRequest
    ): Response<ImportContentResponse>

    /**
     * Gets the account ID associated with the current API token.
     * @return A [Response] containing the account ID
     */
    @GET("accounts/getid")
    suspend fun getAccountId(): Response<AccountIdResponse>

    /**
     * Gets account details.
     * @param accountId The ID of the account
     * @return A [Response] containing the account details
     */
    @GET("accounts/{accountId}")
    suspend fun getAccountDetails(
        @Path("accountId") accountId: String
    ): Response<AccountDetailsResponse>

    /**
     * Resets the authentication token for an account.
     * @param accountId The ID of the account
     * @return A [Response] containing the new token
     */
    @POST("accounts/{accountId}/resetToken")
    suspend fun resetAuthToken(
        @Path("accountId") accountId: String
    ): Response<ResetTokenResponse>
} 