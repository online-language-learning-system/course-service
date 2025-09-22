-- CREATE DATABASE and USE dbo SCHEMA
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'course_db')
BEGIN
    CREATE DATABASE course_db;
END;
GO

-- COURSE CATEGORY TABLE
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.course_category') AND type = 'U')
BEGIN
    CREATE TABLE dbo.course_category (
        id                  BIGINT NOT NULL PRIMARY KEY,
        category_level      NVARCHAR(5) NOT NULL,
        description         NVARCHAR(255) NOT NULL
    );
END;
GO

-- COURSES TABLE
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.course') AND type = 'U')
BEGIN
    CREATE TABLE dbo.course_category (
        id                      BIGINT NOT NULL PRIMARY KEY,
        category_id             BIGINT NOT NULL FOREIGN KEY,
        title                   NVARCHAR(100) NOT NULL,
        teaching_language       NVARCHAR(20) NOT NULL,
        price                   DECIMAL(9, 0) NOT NULL,
        description             NVARCHAR(255) NOT NULL,
        startDate               DATETIMEOFFSET NOT NULL,
        endDate                 DATETIMEOFFSET NOT NULL,
        approvalStatus          NVARCHAR(20) NOT NULL,
        created_by              NVARCHAR(100) NOT NULL,
        created_on              DATETIMEOFFSET NOT NULL,
        last_modified_by        NVARCHAR(100) NOT NULL,
        last_modified_on        DATETIMEOFFSET NOT NULL
    );
END;
GO

-- IMAGE TABLE
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.course_image') AND type = 'U')
BEGIN
    CREATE TABLE dbo.course_image (
        id              BIGINT NOT NULL PRIMARY KEY,
        course_id       BIGINT NOT NULL FOREIGN KEY,
        image_url       NVARCHAR(255) NOT NULL
    )
END;
GO

-- MODULE TABLE
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.course_module') AND type = 'U')
BEGIN
    CREATE TABLE dbo.course_module (
        id                    BIGINT NOT NULL PRIMARY KEY,
        course_id             BIGINT NOT NULL FOREIGN KEY,
        title                 NVARCHAR(100) NOT NULL,
        description           NVARCHAR(255) NOT NULL,
        order_index           INT NOT NULL,
        can_free_trial        BIT NOT NULL,
        created_by            NVARCHAR(100) NOT NULL,
        created_on            DATETIMEOFFSET NOT NULL,
        last_modified_by      NVARCHAR(100) NOT NULL,
        last_modified_on      DATETIMEOFFSET NOT NULL
    )
END;
GO

-- LESSON TABLE
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.lesson') AND type = 'U')
BEGIN
    CREATE TABLE dbo.lesson (
        id                    BIGINT NOT NULL PRIMARY KEY,
        module_id             BIGINT NOT NULL FOREIGN KEY,
        title                 NVARCHAR(100) NOT NULL,
        description           NVARCHAR(255) NOT NULL,
        duration              INTEGER NULL,
        created_by            NVARCHAR(100) NOT NULL,
        created_on            DATETIMEOFFSET NOT NULL,
        last_modified_by      NVARCHAR(100) NOT NULL,
        last_modified_on      DATETIMEOFFSET NOT NULL
    )
END;
GO

-- LESSON RESOURCE TABLE
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'dbo.lesson_resource') AND type = 'U')
BEGIN
    CREATE TABLE dbo.lesson_resource (
        id                    BIGINT NOT NULL PRIMARY KEY,
        lesson_id             BIGINT NOT NULL FOREIGN KEY,
        resource_type         NVARCHAR(10) NOT NULL,
        resource_url          NVARCHAR(255) NULL
    )
END;
GO