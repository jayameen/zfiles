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
9. Read/Get Object In Base64 String From URL

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

### How to use API? (Examples)
<details>
<summary> 
Create  or Update Object From Base64 String
</summary>

Methods: POST, PUT

Path:  /zfiles/api/object/base64

Request Body:
```json
{
    "file_name" : "vs1.jpeg",
    "file_path" : "/madankn",
    "is_private" : false,
    "base64_data" : "iVBORw=="
}
```
Success Response: 200 OK
```json
{
    "status": "ok",
    "data": [
        "https://aadsf.blob.core.windows.net/mycontainer/madankn%2Fvs1.jpeg?sv=2023-11-03&se=2034-05-07T06%3A45%&sr=b&sp=r&sig=iPCrHk%%3D"
    ]
}
```
</details>


<details>
<summary>  Create Multiple Objects From Base64 String </summary>

Methods: POST, PUT

Path:  /zfiles/api/object/base64/multiple

Request Body:
```json
[
  {
    "file_name" : "vs1.jpeg",
    "file_path" : "/madankn",
    "is_private" : false,
    "base64_data" : "iVBORw=="
}, {
    "file_name" : "vs2.jpeg",
    "file_path" : "/madankn",
    "is_private" : false,
    "base64_data" : "iVBORw=="
  }
]
```
Success Response: 200 OK
```json
{
    "status": "ok",
    "data": [
        "https://aadsf.blob.core.windows.net/mycontainer/madankn%2Fvs1.jpeg?sv=2023-11-03&se=2034-05-07T06%3A45%&sr=b&sp=r&sig=iPCrHk%%3D", 
        "https://aadsf.blob.core.windows.net/mycontainer/madankn%2Fvs2.jpeg?sv=2023-11-03&se=2034-05-07T06%3A45%&sr=b&sp=r&sig=iPCrHk%%3D"
    ]
}
```
</details>

<details>
<summary> 
Create Object From Multipart File
</summary>

Methods: POST, PUT

Path: /zfiles/api/object/file

Form Data:
```
curl --location 'http://localhost:8081/zfiles/api/object/file' \
--form 'file=@"/Users/madan/Downloads/VS.jpeg"' \
--form 'file_path="/madankn/test"' \
--form 'is_private="false"'
```

Success Response: 200 OK
```json
{
    "status": "ok",
    "data": [
        "https://aadsf.blob.core.windows.net/mycontainer/madankn%2Fvs1.jpeg?sv=2023-11-03&se=2034-05-07T06%3A45%&sr=b&sp=r&sig=iPCrHk%%3D"
    ]
}
```
</details>

<details>
<summary> 
Delete Object From URL
</summary>

Methods: DELETE

Path:  /zfiles/api/object

Request Body:
```json
{
    "file_url" : "http:/localhost:8081/madankn/vs1.jpeg"
}
```
Success Response: 200 OK
```json
{
    "status": "ok",
    "data": [
        true
    ]
}
```
</details>

<details>
<summary> 
Delete Multiple Objects From URL
</summary>

Methods: DELETE

Path:  /zfiles/api/object/multiple

Request Body:
```json
[
    {
        "file_url" : "http:/localhost:8081/madankn/vs1.jpeg"
    }, {
        "file_url" : "http:/localhost:8081/madankn/vs2.jpeg"
    }
]
```
Success Response: 200 OK
```json
{
    "status": "ok",
    "data": [
        true,
        true
    ]
}
```
</details>

<details>
<summary> 
Read/Get Object In Base64 String From URL
</summary>

Methods: POST, PUT

Path:  /zfiles/api/object/base64

Request Body:
```json
{
    "file_url" : "https://storageaccount.blob.core.windows.net/mycontainer/madankn%2Fvs1.jpeg?sv=2023-11-03&se=2034-05-07T08%asdf%3A19Z&sr=b&sp=r&sig=49%asd%3D"
}
```
Success Response: 200 OK
```json
{
    "status": "ok",
    "data": [
        "iVBORw=="
    ]
}
```
</details>

#### FileRequest Input Object Model
```json 
{
    "file_name" : "vs1.jpeg",
    "file_path" : "/madankn",
    "is_private" : false,
    "base64_data" : "iVBORw=="
    "file_url" : "http://localhost:8081/zfiles/madankn/vs1.jpeg"
}
```

#### Errors
> Error Response: 400 Bad Request
```json
{
  "status": "error",
  "description": "File size exceeds the limit of 10 MB"
}
```

> Error Response: 500 Internal Server Error
```json
{
    "status": "error",
    "description": "Input byte array has wrong 4-byte ending unit"
}
```

### How to configure application.yml
```yaml
spring:
  application:
    name: zfiles # application name - this is also referred in the context path
  servlet:
    multipart:
      max-file-size: 25000000 # max file upload size 25MB
      max-request-size: 25000000 # max request size 25MB

server:
  port: 8081 # server port
  servlet:
    context-path: /${spring.application.name} # context path

---
spring:
  config:
    activate:
      on-profile: default # active profile

zfiles:
  cdn-type: local # gcp, s3, azure, local (default)

# Local File System Configurations
local:
  prefix-http-url: http://localhost # local file system http url prefix - You can use this to serve the files from the http server using apache/nginx
  prefix-upload: /Users/madan/Documents/JayaMeen/www/files # local file system upload prefix path

# Google Cloud Storage Configurations
gcp:
  prefix-http-url: https://storage.googleapis.com # google cloud storage http url prefix
  prefix-upload: # google cloud storage upload prefix path if you want to upload the files to a specific folder
  project-id: ${GCP_PROJECT_ID:test} # google cloud project id
  bucket-id: ${GCP_BUCKET_ID:test} # google cloud bucket id
  upload-url: https://storage.googleapis.com # google cloud storage upload url
  config-data: ${GCP_CONFIG_DATA_KEY:test} # google cloud storage config data key

# Amazon S3 Configurations
s3:
  prefix-http-url: https://uxin.sgp1.digitaloceanspaces.com # amazon s3 http url prefix
  prefix-upload: # amazon s3 upload prefix path if you want to upload the files to a specific folder
  region: sgp1 # amazon s3 region
  bucket-id: uxin # amazon s3 bucket id
  upload-url: https://sgp1.digitaloceanspaces.com # amazon s3 upload url
  key: ${DO_KEY:test} # amazon s3 key
  secret: ${DO_SECRET:test} # amazon s3 secret

azure:
  connection-string: ${AZURE_CONNECTION_STRING:DefaultEndpointsProtocol=https;AccountName=teststorage;AccountKey=yourkey;EndpointSuffix=core.windows.net} # azure connection string
  endpoint: ${AZURE_ENDPOINT:https://teststorage.blob.core.windows.net/} # azure endpoint
  container-name: ${AZURE_CONTAINER_NAME:test} #  azure container name
  prefix-http-url: https://xindusazureblobstorage.blob.core.windows.net # azure blob storage http url prefix
  prefix-upload: # azure blob storage upload prefix path if you want to upload the files to a specific folder

```



