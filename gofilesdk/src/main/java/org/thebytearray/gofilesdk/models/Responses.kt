package org.thebytearray.gofilesdk.models

/**
 * Response model for file uploads.
 * @property status The status of the response
 * @property data The upload data
 */
data class UploadResponse(
    val status: String,
    val data: UploadData
)

/**
 * Data model for upload responses.
 * @property downloadPage The URL of the download page
 * @property code The file code
 * @property parentFolder The ID of the parent folder
 * @property fileId The ID of the uploaded file
 * @property fileName The name of the uploaded file
 * @property md5 The MD5 hash of the file
 */
data class UploadData(
    val downloadPage: String,
    val code: String,
    val parentFolder: String,
    val fileId: String,
    val fileName: String,
    val md5: String
)

/**
 * Response model for folder creation.
 * @property status The status of the response
 * @property data The folder data
 */
data class CreateFolderResponse(
    val status: String,
    val data: CreateFolderData
)

/**
 * Data model for folder creation responses.
 * @property id The ID of the created folder
 * @property name The name of the folder
 * @property parentFolder The ID of the parent folder
 */
data class CreateFolderData(
    val id: String,
    val name: String,
    val parentFolder: String
)

/**
 * Response model for content updates.
 * @property status The status of the response
 * @property data The updated content data
 */
data class UpdateContentResponse(
    val status: String,
    val data: UpdateContentData
)

/**
 * Data model for content update responses.
 * @property id The ID of the content
 * @property name The name of the content
 * @property type The type of the content
 * @property parentFolder The ID of the parent folder
 * @property createTime The creation timestamp
 * @property description Optional description
 * @property tags Optional list of tags
 * @property isPublic Whether the content is public
 * @property expiry Optional expiration timestamp
 * @property password Optional password
 */
data class UpdateContentData(
    val id: String,
    val name: String,
    val type: String,
    val parentFolder: String,
    val createTime: Long,
    val description: String?,
    val tags: List<String>?,
    val isPublic: Boolean,
    val expiry: Long?,
    val password: String?
)

/**
 * Response model for content deletion.
 * @property status The status of the response
 */
data class DeleteContentResponse(
    val status: String
)

/**
 * Response model for folder details.
 * @property status The status of the response
 * @property data The folder details
 */
data class FolderDetailsResponse(
    val status: String,
    val data: FolderDetailsData
)

/**
 * Data model for folder details responses.
 * @property id The ID of the folder
 * @property name The name of the folder
 * @property type The type of the folder
 * @property parentFolder The ID of the parent folder
 * @property createTime The creation timestamp
 * @property description Optional description
 * @property tags Optional list of tags
 * @property isPublic Whether the folder is public
 * @property expiry Optional expiration timestamp
 * @property password Optional password
 * @property contents List of content items in the folder
 */
data class FolderDetailsData(
    val id: String,
    val name: String,
    val type: String,
    val parentFolder: String,
    val createTime: Long,
    val description: String?,
    val tags: List<String>?,
    val isPublic: Boolean,
    val expiry: Long?,
    val password: String?,
    val contents: List<ContentItem>
)

/**
 * Data model for content items.
 * @property id The ID of the content
 * @property name The name of the content
 * @property type The type of the content
 * @property parentFolder The ID of the parent folder
 * @property createTime The creation timestamp
 * @property description Optional description
 * @property tags Optional list of tags
 * @property isPublic Whether the content is public
 * @property expiry Optional expiration timestamp
 * @property password Optional password
 * @property size Optional file size
 * @property downloadCount Optional download count
 * @property md5 Optional MD5 hash
 */
data class ContentItem(
    val id: String,
    val name: String,
    val type: String,
    val parentFolder: String,
    val createTime: Long,
    val description: String?,
    val tags: List<String>?,
    val isPublic: Boolean,
    val expiry: Long?,
    val password: String?,
    val size: Long?,
    val downloadCount: Int?,
    val md5: String?
)

/**
 * Response model for search results.
 * @property status The status of the response
 * @property data List of content items matching the search
 */
data class SearchResponse(
    val status: String,
    val data: List<ContentItem>
)

/**
 * Response model for direct link operations.
 * @property status The status of the response
 * @property data The direct link data
 */
data class DirectLinkResponse(
    val status: String,
    val data: DirectLinkData
)

/**
 * Data model for direct link responses.
 * @property id The ID of the direct link
 * @property url The direct link URL
 * @property expireTime Optional expiration timestamp
 * @property sourceIpsAllowed Optional list of allowed source IPs
 * @property domainsAllowed Optional list of allowed domains
 * @property auth Optional list of authentication credentials
 */
data class DirectLinkData(
    val id: String,
    val url: String,
    val expireTime: Long?,
    val sourceIpsAllowed: List<String>?,
    val domainsAllowed: List<String>?,
    val auth: List<String>?
)

/**
 * Response model for direct link deletion.
 * @property status The status of the response
 */
data class DeleteDirectLinkResponse(
    val status: String
)

/**
 * Response model for content copying.
 * @property status The status of the response
 * @property data List of copied content IDs
 */
data class CopyContentResponse(
    val status: String,
    val data: List<String>
)

/**
 * Response model for content moving.
 * @property status The status of the response
 */
data class MoveContentResponse(
    val status: String
)

/**
 * Response model for content importing.
 * @property status The status of the response
 * @property data List of imported content IDs
 */
data class ImportContentResponse(
    val status: String,
    val data: List<String>
)

/**
 * Response model for account ID retrieval.
 * @property status The status of the response
 * @property data The account ID
 */
data class AccountIdResponse(
    val status: String,
    val data: String
)

/**
 * Response model for account details.
 * @property status The status of the response
 * @property data The account details
 */
data class AccountDetailsResponse(
    val status: String,
    val data: AccountDetailsData
)

/**
 * Data model for account details responses.
 * @property id The account ID
 * @property email The account email
 * @property tier The account tier
 * @property rootFolder The ID of the root folder
 * @property totalSize Total storage size used
 * @property totalFiles Total number of files
 * @property totalFolders Total number of folders
 * @property createTime Account creation timestamp
 */
data class AccountDetailsData(
    val id: String,
    val email: String,
    val tier: String,
    val rootFolder: String,
    val totalSize: Long,
    val totalFiles: Int,
    val totalFolders: Int,
    val createTime: Long
)

/**
 * Response model for token reset.
 * @property status The status of the response
 * @property data The new token
 */
data class ResetTokenResponse(
    val status: String,
    val data: String
)

/**
 * Response model for server selection.
 * @property status The status of the response
 * @property data The server data
 */
data class ServerResponse(
    val status: String,
    val data: ServerData
)

/**
 * Data model for server responses.
 * @property server The server URL
 */
data class ServerData(
    val server: String
) 