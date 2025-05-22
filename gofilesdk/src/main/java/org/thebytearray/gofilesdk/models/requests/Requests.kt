package org.thebytearray.gofilesdk.models.requests

/**
 * Request model for creating a new folder.
 * @property parentFolderId The ID of the parent folder
 * @property folderName The name of the new folder
 */
data class CreateFolderRequest(
    val parentFolderId: String,
    val folderName: String
)

/**
 * Request model for updating content attributes.
 * @property attribute The attribute to update
 * @property attributeValue The new value for the attribute
 */
data class UpdateContentRequest(
    val attribute: String,
    val attributeValue: Any
)

/**
 * Request model for deleting content.
 * @property contentsId Comma-separated list of content IDs to delete
 */
data class DeleteContentRequest(
    val contentsId: String
)

/**
 * Request model for creating a direct link.
 * @property expireTime Optional expiration time in milliseconds
 * @property sourceIpsAllowed Optional list of allowed source IPs
 * @property domainsAllowed Optional list of allowed domains
 * @property auth Optional list of authentication credentials
 */
data class CreateDirectLinkRequest(
    val expireTime: Long? = null,
    val sourceIpsAllowed: List<String>? = null,
    val domainsAllowed: List<String>? = null,
    val auth: List<String>? = null
)

/**
 * Request model for updating a direct link.
 * @property expireTime Optional expiration time in milliseconds
 * @property sourceIpsAllowed Optional list of allowed source IPs
 * @property domainsAllowed Optional list of allowed domains
 * @property auth Optional list of authentication credentials
 */
data class UpdateDirectLinkRequest(
    val expireTime: Long? = null,
    val sourceIpsAllowed: List<String>? = null,
    val domainsAllowed: List<String>? = null,
    val auth: List<String>? = null
)

/**
 * Request model for copying content.
 * @property contentsId Comma-separated list of content IDs to copy
 * @property folderId The ID of the destination folder
 */
data class CopyContentRequest(
    val contentsId: String,
    val folderId: String
)

/**
 * Request model for moving content.
 * @property contentsId Comma-separated list of content IDs to move
 * @property folderId The ID of the destination folder
 */
data class MoveContentRequest(
    val contentsId: String,
    val folderId: String
)

/**
 * Request model for importing public content.
 * @property contentsId Comma-separated list of public content IDs to import
 */
data class ImportContentRequest(
    val contentsId: String
) 