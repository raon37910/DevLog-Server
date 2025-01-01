CREATE TABLE IF NOT EXISTS `User`
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

CREATE TABLE IF NOT EXISTS `UserRole`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `userId`     INT,
    `roleId`     INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE IF NOT EXISTS `Role`
(
    `id`          INT PRIMARY KEY AUTO_INCREMENT,
    `name`        VARCHAR(45),
    `description` VARCHAR(100),
    `createTime`  DATETIME,
    `updateTime`  DATETIME
);

CREATE TABLE IF NOT EXISTS `Article`
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

CREATE TABLE IF NOT EXISTS `ArticleLike`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `userId`     INT,
    `articleId`  INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE IF NOT EXISTS `BookMark`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `userId`     INT,
    `articleId`  INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE IF NOT EXISTS `Category`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(45),
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE IF NOT EXISTS `ArticleTag`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `articleId`  INT,
    `tagId`      INT,
    `createTime` DATETIME,
    `updateTime` DATETIME
);

CREATE TABLE IF NOT EXISTS `Tag`
(
    `id`         INT PRIMARY KEY AUTO_INCREMENT,
    `name`       VARCHAR(45),
    `createTime` DATETIME,
    `updateTime` DATETIME
);

ALTER TABLE `UserRole`
    ADD CONSTRAINT `fk_UserRole_user`
        FOREIGN KEY (`userId`)
            REFERENCES `User` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `UserRole`
    ADD CONSTRAINT `fk_UserRole_role`
        FOREIGN KEY (`roleId`)
            REFERENCES `Role` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `Article`
    ADD CONSTRAINT `fk_Article_category`
        FOREIGN KEY (`categoryId`)
            REFERENCES `Category` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `ArticleLike`
    ADD CONSTRAINT `fk_ArticleLike_user`
        FOREIGN KEY (`userId`)
            REFERENCES `User` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `ArticleLike`
    ADD CONSTRAINT `fk_ArticleLike_article`
        FOREIGN KEY (`articleId`)
            REFERENCES `Article` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `BookMark`
    ADD CONSTRAINT `fk_BookMark_user`
        FOREIGN KEY (`userId`)
            REFERENCES `User` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `BookMark`
    ADD CONSTRAINT `fk_BookMark_article`
        FOREIGN KEY (`articleId`)
            REFERENCES `Article` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `ArticleTag`
    ADD CONSTRAINT `fk_ArticleTag_article`
        FOREIGN KEY (`articleId`)
            REFERENCES `Article` (`id`)
            ON DELETE CASCADE;

ALTER TABLE `ArticleTag`
    ADD CONSTRAINT `fk_ArticleTag_tag`
        FOREIGN KEY (`tagId`)
            REFERENCES `Tag` (`id`)
            ON DELETE CASCADE;
