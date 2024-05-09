# ZFiles
### A Simple CRUD REST API For Multiple Cloud Storage Services (Google Cloud Services, Amazon S3, Azure, Digital Ocean, and many more..)

## Why?
### Why do I need this?
1. **Simple API**: ZFiles provides a single API to perform CRUD operations on files stored in multiple cloud storage services.
2. **Multiple Cloud Storage Services**: ZFiles supports multiple cloud storage services at runtime and you can switch between them without changing your code.
3. **Easy Configuration**: ZFiles is easy to configure and use. You can configure the cloud storage services in a single configuration file.
4. **Easy Switch Over**: ZFiles provides an option to switch between services at runtime. You can switch between services without changing your code.

## What?
### Supported Cloud Storage Services
1. Google Cloud Storage
2. Amazon S3
3. Azure Blob Storage
4. Digital Ocean Spaces
5. Local File System

### Object Operations Supported
1. Create Object From Base64 String
2. Create Multiple Objects From Base64 String
3. Create Object From Multipart File
4. Update Object From Base64 String
5. Update Multiple Objects From Base64 String
6. Update Object From Multipart File
7. Delete Object From URL
8. Delete Multiple Objects From URL
9. Read Object From URL

### Technologies Used
1. Java 8+
2. Spring Boot

## How?
### How to Use?
1. Download the source code from the repository & compile/build/run from your favorite IDE or servers.

### How to configure?
1. Open the `application.properties` file.
2. Add the required configuration for the cloud storage services.
3. Choose the implementation of the cloud storage service from the `cdn-type` property. Supported implementations are `gcp`, `s3`, `azure`, and `local`.


