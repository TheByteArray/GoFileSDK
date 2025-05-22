# GoFile SDK for Android

[![JitPack](https://jitpack.io/v/TheByteArray/GoFileSDK.svg)](https://jitpack.io/#TheByteArray/GoFileSDK)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A professional Android SDK for interacting with the Gofile API, developed by The Byte Array. This SDK provides a clean, type-safe interface for all Gofile API operations.

## Features

- Complete API coverage
- Type-safe request and response models
- Efficient file uploads with best server selection
- Proper error handling with Kotlin Result type
- Coroutine-based async operations
- Singleton pattern for easy access
- Comprehensive documentation
- Regional proxy support

## Installation

Add the JitPack repository to your project's `settings.gradle`:

```gradle
dependencyResolutionManagement {
    repositories {
        // ... other repositories
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your app's `build.gradle`:

```gradle
dependencies {
    implementation 'com.github.TheByteArray:GoFileSDK:1.0.0'
}
```

## Quick Start

```kotlin
// Initialize the SDK with your API token
val sdk = GofileSDK.getInstance(apiToken = "your_api_token")

// Upload a file
lifecycleScope.launch {
    val file = File("path/to/your/file.txt")
    sdk.uploadFile(file)
        .onSuccess { response ->
            println("File uploaded successfully!")
            println("Download page: ${response.data.downloadPage}")
        }
        .onFailure { error ->
            println("Upload failed: ${error.message}")
        }
}
```

## API Documentation

### Authentication

```kotlin
// Initialize with API token
val sdk = GofileSDK.getInstance(apiToken = "your_api_token")

// Initialize with custom upload server
val sdk = GofileSDK.getInstance(
    apiToken = "your_api_token",
    uploadBaseUrl = "https://store1.gofile.io/"
)
```

### File Operations

#### Upload File
```kotlin
suspend fun uploadFile(file: File, folderId: String? = null): Result<UploadResponse>
```

Example:
```kotlin
lifecycleScope.launch {
    val file = File("path/to/your/file.txt")
    sdk.uploadFile(file, folderId = "optional_folder_id")
        .onSuccess { response ->
            println("File uploaded successfully!")
            println("Download page: ${response.data.downloadPage}")
            println("File ID: ${response.data.fileId}")
        }
        .onFailure { error ->
            println("Upload failed: ${error.message}")
        }
}
```

#### Get Best Server
```kotlin
suspend fun getBestServer(): Result<String>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.getBestServer()
        .onSuccess { server ->
            println("Best server for upload: $server")
        }
        .onFailure { error ->
            println("Failed to get best server: ${error.message}")
        }
}
```

### Folder Operations

#### Create Folder
```kotlin
suspend fun createFolder(parentFolderId: String, folderName: String): Result<CreateFolderResponse>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.createFolder(
        parentFolderId = "parent_folder_id",
        folderName = "New Folder"
    ).onSuccess { response ->
        println("Folder created successfully!")
        println("Folder ID: ${response.data.id}")
    }.onFailure { error ->
        println("Failed to create folder: ${error.message}")
    }
}
```

#### Get Folder Details
```kotlin
suspend fun getFolderDetails(folderId: String, password: String? = null): Result<FolderDetailsResponse>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.getFolderDetails(folderId = "your_folder_id")
        .onSuccess { response ->
            println("Folder name: ${response.data.name}")
            println("Contents: ${response.data.contents}")
        }
        .onFailure { error ->
            println("Failed to get folder details: ${error.message}")
        }
}
```

### Content Management

#### Update Content
```kotlin
suspend fun updateContent(
    contentId: String,
    attribute: String,
    attributeValue: String
): Result<UpdateContentResponse>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.updateContent(
        contentId = "your_content_id",
        attribute = "name",
        attributeValue = "New Name"
    ).onSuccess { response ->
        println("Content updated successfully!")
    }.onFailure { error ->
        println("Failed to update content: ${error.message}")
    }
}
```

#### Delete Content
```kotlin
suspend fun deleteContent(contentsId: String): Result<DeleteContentResponse>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.deleteContent(contentsId = "your_content_id")
        .onSuccess { response ->
            println("Content deleted successfully!")
        }
        .onFailure { error ->
            println("Failed to delete content: ${error.message}")
        }
}
```

### Direct Links

#### Create Direct Link
```kotlin
suspend fun createDirectLink(
    contentId: String,
    expireTime: Int? = null,
    sourceIpsAllowed: List<String>? = null,
    domainsAllowed: List<String>? = null,
    auth: Map<String, String>? = null
): Result<DirectLinkResponse>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.createDirectLink(
        contentId = "your_content_id",
        expireTime = 3600, // 1 hour
        sourceIpsAllowed = listOf("192.168.1.1"),
        domainsAllowed = listOf("example.com"),
        auth = mapOf("username" to "password")
    ).onSuccess { response ->
        println("Direct link created successfully!")
        println("Link: ${response.data.url}")
    }.onFailure { error ->
        println("Failed to create direct link: ${error.message}")
    }
}
```

### Account Management

#### Get Account ID
```kotlin
suspend fun getAccountId(): Result<String>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.getAccountId()
        .onSuccess { accountId ->
            println("Account ID: $accountId")
        }
        .onFailure { error ->
            println("Failed to get account ID: ${error.message}")
        }
}
```

#### Get Account Details
```kotlin
suspend fun getAccountDetails(accountId: String): Result<AccountDetailsResponse>
```

Example:
```kotlin
lifecycleScope.launch {
    sdk.getAccountDetails(accountId = "your_account_id")
        .onSuccess { response ->
            println("Account email: ${response.data.email}")
            println("Account tier: ${response.data.tier}")
        }
        .onFailure { error ->
            println("Failed to get account details: ${error.message}")
        }
}
```

## Error Handling

All SDK methods return a Kotlin `Result` type, which provides a clean way to handle both success and error cases:

```kotlin
sdk.uploadFile(file)
    .onSuccess { response ->
        // Handle success
    }
    .onFailure { error ->
        // Handle error
    }
```

## Requirements

- Android API level 21+
- Kotlin 1.6+
- Coroutines support
- Retrofit 2.x
- OkHttp 4.x

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Support

For SDK-related queries, please:
- Open an issue on our [GitHub repository](https://github.com/TheByteArray/GoFileSDK/issues)
- Contact us at contact@thebytearray.org

## About The Byte Array

The Byte Array is a software development company specializing in creating high-quality, professional SDKs and libraries for the Android platform. Visit our [website](https://thebytearray.com) to learn more about our services.

## Regional Upload Proxies

The SDK supports all Gofile regional upload proxies. You can specify a different upload URL when initializing the SDK:

```kotlin
val sdk = GofileSDK.getInstance(
    uploadBaseUrl = "https://upload-eu-par.gofile.io/" // Europe (Paris)
)
```

Available regional proxies:
- `https://upload.gofile.io/` (Automatic - nearest)
- `https://upload-eu-par.gofile.io/` (Europe - Paris)
- `https://upload-na-phx.gofile.io/` (North America - Phoenix)
- `https://upload-ap-sgp.gofile.io/` (Asia-Pacific - Singapore)
- `https://upload-ap-hkg.gofile.io/` (Asia-Pacific - Hong Kong)
- `https://upload-ap-tyo.gofile.io/` (Asia-Pacific - Tokyo)
- `https://upload-sa-sao.gofile.io/` (South America - São Paulo)

## License

This SDK is provided under the MIT License. See the LICENSE file for details.

## Support

For support, please contact:
- Email: support@gofile.io
- API Documentation: https://gofile.io/api 