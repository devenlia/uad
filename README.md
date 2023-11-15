# UAD Server

[Release notes](./RELEASE_NOTES.md)

# API Documentation

## PageController

URL Path: "/content/page"

- **GET /search**
    - **Parameter**: path (String)
    - **Purpose**: Allows searching of a page by its path.
    - **Response Type**: ResponseEntity

- **GET /home**
    - **Purpose**: Allows fetching of the homepage.
    - **Response Type**: ResponseEntity

- **GET /get**
    - **Parameter**: id (String)
    - **Purpose**: Allows fetching of a page by its id.
    - **Response Type**: ResponseEntity

- **GET /list**
    - **Purpose**: Allows fetching of a list of all pages.
    - **Response Type**: ResponseEntity

- **POST /add**
    - **Parameter**: page (RequestBody)
    - **Purpose**: Allows adding of a new page.
    - **Response Type**: ResponseEntity

- **PUT /update**
    - **Parameter**: page (RequestBody)
    - **Purpose**: Allows updating of a page.
    - **Response Type**: ResponseEntity

- **DELETE /delete**
    - **Parameter**: id (String)
    - **Purpose**: Allows deletion of a page by its id.
    - **Response Type**: ResponseEntity

## ContainerController

URL Path: "/content/container"

- **GET /get**
    - **Parameter**: id (String)
    - **Purpose**: Allows fetching of a container by its id.
    - **Response Type**: ResponseEntity

- **POST /add**
    - **Parameter**: container (RequestBody)
    - **Purpose**: Allows adding of a new container.
    - **Response Type**: ResponseEntity

- **DELETE /delete**
    - **Parameter**: id (String)
    - **Purpose**: Allows deletion of a container by its id.
    - **Response Type**: ResponseEntity

## CategoryController

URL Path: "/content/category"

- **GET /get**
    - **Parameter**: id (String)
    - **Purpose**: Allows fetching of a category by its id.
    - **Response Type**: ResponseEntity

- **POST /add**
    - **Parameter**: category (RequestBody)
    - **Purpose**: Allows adding of a new category.
    - **Response Type**: ResponseEntity

- **DELETE /delete**
    - **Parameter**: id (String)
    - **Purpose**: Allows deletion of a category by its id.
    - **Response Type**: ResponseEntity

## LinkController

URL Path: "/content/link"

- **GET /get**
    - **Parameter**: id (String)
    - **Purpose**: Allows fetching of a link by its id.
    - **Response Type**: ResponseEntity

- **POST /add**
    - **Parameter**: link (RequestBody)
    - **Purpose**: Allows adding of a new link.
    - **Response Type**: ResponseEntity

- **DELETE /delete**
    - **Parameter**: id (String)
    - **Purpose**: Allows deletion of a link by its id.
    - **Response Type**: ResponseEntity