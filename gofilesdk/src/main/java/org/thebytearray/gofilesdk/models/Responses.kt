package org.thebytearray.gofilesdk.models

data class UploadResponse(
    val status: String,
    val data: UploadData
)

data class UploadData(
    val downloadPage: String,
    val code: String,
    val parentFolder: String,
    val fileId: String,
    val fileName: String,
    val md5: String
)

data class CreateFolderResponse(
    val status: String,
    val data: CreateFolderData
)

data class CreateFolderData(
    val id: String,
    val name: String,
    val parentFolder: String
)

data class UpdateContentResponse(
    val status: String,
    val data: UpdateContentData
)

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

data class DeleteContentResponse(
    val status: String
)

data class FolderDetailsResponse(
    val status: String,
    val data: FolderDetailsData
)

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

data class SearchResponse(
    val status: String,
    val data: List<ContentItem>
)

data class DirectLinkResponse(
    val status: String,
    val data: DirectLinkData
)

data class DirectLinkData(
    val id: String,
    val url: String,
    val expireTime: Long?,
    val sourceIpsAllowed: List<String>?,
    val domainsAllowed: List<String>?,
    val auth: List<String>?
)

data class DeleteDirectLinkResponse(
    val status: String
)

data class CopyContentResponse(
    val status: String,
    val data: List<String>
)

data class MoveContentResponse(
    val status: String
)

data class ImportContentResponse(
    val status: String,
    val data: List<String>
)

data class AccountIdResponse(
    val status: String,
    val data: String
)

data class AccountDetailsResponse(
    val status: String,
    val data: AccountDetailsData
)

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

data class ResetTokenResponse(
    val status: String,
    val data: String
)

data class ServerResponse(
    val status: String,
    val data: ServerData
)

data class ServerData(
    val server: String
) 