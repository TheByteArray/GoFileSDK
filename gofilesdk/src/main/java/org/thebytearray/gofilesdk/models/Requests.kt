package org.thebytearray.gofilesdk.models

data class CreateFolderRequest(
    val parentFolderId: String,
    val folderName: String
)

data class UpdateContentRequest(
    val attribute: String,
    val attributeValue: Any
)

data class DeleteContentRequest(
    val contentsId: String
)

data class CreateDirectLinkRequest(
    val expireTime: Long? = null,
    val sourceIpsAllowed: List<String>? = null,
    val domainsAllowed: List<String>? = null,
    val auth: List<String>? = null
)

data class UpdateDirectLinkRequest(
    val expireTime: Long? = null,
    val sourceIpsAllowed: List<String>? = null,
    val domainsAllowed: List<String>? = null,
    val auth: List<String>? = null
)

data class CopyContentRequest(
    val contentsId: String,
    val folderId: String
)

data class MoveContentRequest(
    val contentsId: String,
    val folderId: String
)

data class ImportContentRequest(
    val contentsId: String
) 