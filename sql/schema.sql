CREATE TABLE `User`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `email`           VARCHAR(45),
    `password`        VARCHAR(200),
    `name`            VARCHAR(100),
    `description`     VARCHAR(100),
    `profileImageUrl` VARCHAR(200),
    `createTime`      DATETIME,
    `updateTime`      DATETIME
);

CREATE TABLE `UserRole`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `userId`     INT,
    `roleId`     INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE `Role`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(45),
    `description` VARCHAR(100),
    `createTime`  DATETIME,
    `updateTime`  DATETIME
);

CREATE TABLE `Article`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `title`       VARCHAR(100),
    `author`      VARCHAR(100),
    `description` VARCHAR(100),
    `link`        VARCHAR(200),
    `views`       INT,
    `createTime`  DATETIME,
    `updateTime`  DATETIME,
    `categoryId`  INT,
    `userId`      INT
);

CREATE TABLE `PostLike`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `userId`     INT,
    `articleId`  INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE `BookMark`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `userId`     INT,
    `articleId`  INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE `Category`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(45),
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE `ArticleTag`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `articleId`  INT,
    `tagId`      INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE `Tag`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(45),
    `createTime` DATETIME,
    `updateTime` DATETIME
);

ALTER TABLE `UserRole`
    ADD FOREIGN KEY (`userId`) REFERENCES `User` (`id`);

ALTER TABLE `UserRole`
    ADD FOREIGN KEY (`roleId`) REFERENCES `Role` (`id`);

ALTER TABLE `Article`
    ADD FOREIGN KEY (`categoryId`) REFERENCES `Category` (`id`);

ALTER TABLE `PostLike`
    ADD FOREIGN KEY (`userId`) REFERENCES `User` (`id`);

ALTER TABLE `PostLike`
    ADD FOREIGN KEY (`articleId`) REFERENCES `Article` (`id`);

ALTER TABLE `BookMark`
    ADD FOREIGN KEY (`userId`) REFERENCES `User` (`id`);

ALTER TABLE `BookMark`
    ADD FOREIGN KEY (`articleId`) REFERENCES `Article` (`id`);

ALTER TABLE `ArticleTag`
    ADD FOREIGN KEY (`articleId`) REFERENCES `Article` (`id`);

ALTER TABLE `ArticleTag`
    ADD FOREIGN KEY (`tagId`) REFERENCES `Tag` (`id`);
