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
- Full Kotlin and Java support

## Installation

### Add JitPack Repository

#### Groovy (settings.gradle)
```gradle
dependencyResolutionManagement {
    repositories {
        // ... other repositories
        maven { url 'https://jitpack.io' }
    }
}
```

#### Kotlin DSL (settings.gradle.kts)
```kotlin
dependencyResolutionManagement {
    repositories {
        // ... other repositories
        maven { url = uri("https://jitpack.io") }
    }
}
```

### Add Dependency

#### Groovy (build.gradle)
```gradle
dependencies {
    implementation 'com.github.TheByteArray:GoFileSDK:1.0.0'
}
```

#### Kotlin DSL (build.gradle.kts)
```kotlin
dependencies {
    implementation("com.github.TheByteArray:GoFileSDK:1.0.0")
}
```

### Add Required Dependencies

#### Groovy (build.gradle)
```gradle
dependencies {
    // Gofile SDK
    implementation 'com.github.TheByteArray:GoFileSDK:1.0.0'
}
```

#### Kotlin DSL (build.gradle.kts)
```kotlin
dependencies {
    // Gofile SDK
    implementation("com.github.TheByteArray:GoFileSDK:1.0.0")
}
```

## Quick Start

### Kotlin
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

### Java
```java
// Initialize the SDK with your API token
GofileSDK sdk = GofileSDK.getInstance("your_api_token");

// Create an executor service
ExecutorService executor = Executors.newSingleThreadExecutor();

// Upload a file
executor.execute(() -> {
    try {
        File file = new File("path/to/your/file.txt");
        UploadResponse response = sdk.uploadFile(file).getOrThrow();
        System.out.println("File uploaded successfully!");
        System.out.println("Download page: " + response.getData().getDownloadPage());
    } catch (Exception e) {
        System.out.println("Upload failed: " + e.getMessage());
    }
});

// Don't forget to shutdown the executor when done
executor.shutdown();
```

## API Documentation

### Authentication

#### Kotlin
```kotlin
// Initialize with API token
val sdk = GofileSDK.getInstance(apiToken = "your_api_token")

// Initialize with custom upload server
val sdk = GofileSDK.getInstance(
    apiToken = "your_api_token",
    uploadBaseUrl = "https://store1.gofile.io/"
)
```

#### Java
```java
// Initialize with API token
GofileSDK sdk = GofileSDK.getInstance("your_api_token");

// Initialize with custom upload server
GofileSDK sdk = GofileSDK.getInstance(
    "your_api_token",
    "https://store1.gofile.io/"
);

// Create an executor service
ExecutorService executor = Executors.newSingleThreadExecutor();
```

### File Operations

#### Upload File
```kotlin
suspend fun uploadFile(file: File, folderId: String? = null): Result<UploadResponse>
```

##### Kotlin Example
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

##### Java Example
```java
executor.execute(() -> {
    try {
        File file = new File("path/to/your/file.txt");
        UploadResponse response = sdk.uploadFile(file, "optional_folder_id").getOrThrow();
        System.out.println("File uploaded successfully!");
        System.out.println("Download page: " + response.getData().getDownloadPage());
        System.out.println("File ID: " + response.getData().getFileId());
    } catch (Exception e) {
        System.out.println("Upload failed: " + e.getMessage());
    }
});
```

#### Get Best Server
```kotlin
suspend fun getBestServer(): Result<String>
```

##### Kotlin Example
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

##### Java Example
```java
executor.execute(() -> {
    try {
        String server = sdk.getBestServer().getOrThrow();
        System.out.println("Best server for upload: " + server);
    } catch (Exception e) {
        System.out.println("Failed to get best server: " + e.getMessage());
    }
});
```

### Folder Operations

#### Create Folder
```kotlin
suspend fun createFolder(parentFolderId: String, folderName: String): Result<CreateFolderResponse>
```

##### Kotlin Example
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

##### Java Example
```java
executor.execute(() -> {
    try {
        CreateFolderResponse response = sdk.createFolder("parent_folder_id", "New Folder").getOrThrow();
        System.out.println("Folder created successfully!");
        System.out.println("Folder ID: " + response.getData().getId());
    } catch (Exception e) {
        System.out.println("Failed to create folder: " + e.getMessage());
    }
});
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

##### Kotlin Example
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

##### Java Example
```java
new CoroutineScope(Dispatchers.Main).launch(() -> {
    try {
        UpdateContentResponse response = sdk.updateContent(
            "your_content_id",
            "name",
            "New Name"
        ).getOrThrow();
        System.out.println("Content updated successfully!");
    } catch (Exception e) {
        System.out.println("Failed to update content: " + e.getMessage());
    }
    return Unit.INSTANCE;
});
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

##### Kotlin Example
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

##### Java Example
```java
new CoroutineScope(Dispatchers.Main).launch(() -> {
    try {
        List<String> sourceIps = Collections.singletonList("192.168.1.1");
        List<String> domains = Collections.singletonList("example.com");
        Map<String, String> auth = Collections.singletonMap("username", "password");
        
        DirectLinkResponse response = sdk.createDirectLink(
            "your_content_id",
            3600, // 1 hour
            sourceIps,
            domains,
            auth
        ).getOrThrow();
        
        System.out.println("Direct link created successfully!");
        System.out.println("Link: " + response.getData().getUrl());
    } catch (Exception e) {
        System.out.println("Failed to create direct link: " + e.getMessage());
    }
    return Unit.INSTANCE;
});
```

## Error Handling

### Kotlin
```kotlin
sdk.uploadFile(file)
    .onSuccess { response ->
        // Handle success
    }
    .onFailure { error ->
        // Handle error
    }
```

### Java
```java
try {
    UploadResponse response = sdk.uploadFile(file).getOrThrow();
    // Handle success
} catch (Exception e) {
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

### Kotlin
```kotlin
val sdk = GofileSDK.getInstance(
    uploadBaseUrl = "https://upload-eu-par.gofile.io/" // Europe (Paris)
)
```

### Java
```java
GofileSDK sdk = GofileSDK.getInstance(
    "your_api_token",
    "https://upload-eu-par.gofile.io/" // Europe (Paris)
);
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