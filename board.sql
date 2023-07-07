create table board.categories
(
    category_id bigint auto_increment
        primary key,
    category    varchar(25) not null
);

create table board.gallery_files
(
    file_id        bigint auto_increment
        primary key,
    file_name      varchar(255) not null,
    file_realname  varchar(255) not null,
    size           int          null,
    extension      varchar(100) null,
    thumbnail_path varchar(255) null,
    post_id        bigint       null
);

create table board.users
(
    user_id   bigint auto_increment
        primary key,
    login_id  varchar(14)          null,
    username  varchar(40)          null,
    email     varchar(40)          null,
    passwd    varchar(255)         null,
    role      varchar(10)          null,
    activated tinyint(1) default 1 null
);

create table board.community_posts
(
    post_id       bigint auto_increment
        primary key,
    created_date  datetime             null,
    modified_date datetime             null,
    title         varchar(100)         null,
    author_id     bigint               null,
    content       text                 null,
    hits          bigint     default 0 not null,
    category_id   bigint               null,
    delete_flag   tinyint(1) default 0 null,
    constraint categorys_fk_1
        foreign key (category_id) references board.categories (category_id),
    constraint users_fk_1
        foreign key (author_id) references board.users (user_id)
);

create table board.community_comments
(
    comment_id    bigint auto_increment
        primary key,
    comment       varchar(255)         null,
    created_date  datetime             null,
    post_id       bigint               null,
    user_id       bigint               null,
    delete_flag   tinyint(1) default 0 null,
    modified_date datetime             null,
    constraint FK_posts_TO_comments_1
        foreign key (post_id) references board.community_posts (post_id),
    constraint FK_users_to_comments_1
        foreign key (user_id) references board.users (user_id)
);

create table board.community_files
(
    file_id       bigint auto_increment
        primary key,
    file_name     varchar(255) not null,
    file_realname varchar(255) not null,
    post_id       bigint       not null,
    extension     varchar(100) null,
    size          int          null,
    constraint FK_posts_TO_files_1
        foreign key (post_id) references board.community_posts (post_id)
);

create table board.gallery_posts
(
    post_id       bigint auto_increment
        primary key,
    created_date  datetime             null,
    modified_date datetime             null,
    title         varchar(100)         null,
    content       text                 null,
    hits          bigint     default 0 not null,
    delete_flag   tinyint(1) default 0 not null,
    author_id     bigint               not null,
    thumbnail_id  bigint               null,
    constraint FK_users_TO_image_posts_1
        foreign key (author_id) references board.users (user_id),
    constraint image_posts_image_files_file_id_fk
        foreign key (thumbnail_id) references board.gallery_files (file_id)
);

create table board.gallery_comments
(
    comment_id    bigint auto_increment
        primary key,
    comment       varchar(255)         not null,
    created_date  datetime             not null,
    modified_date datetime             null,
    delete_flag   tinyint(1) default 0 null,
    user_id       bigint               not null,
    post_id       bigint               not null,
    constraint FK_image_posts_TO_imgae_comments_1
        foreign key (post_id) references board.gallery_posts (post_id),
    constraint FK_users_TO_imgae_comments_1
        foreign key (user_id) references board.users (user_id)
);

alter table board.gallery_files
    add constraint image_files_image_posts_post_id_fk
        foreign key (post_id) references board.gallery_posts (post_id);

create table board.inquiry_posts
(
    post_id       bigint auto_increment
        primary key,
    created_date  datetime             null,
    modified_date datetime             null,
    title         varchar(100)         null,
    content       text                 null,
    hits          bigint     default 0 not null,
    delete_flag   tinyint(1) default 0 not null,
    author_id     bigint               not null,
    secret        tinyint(1) default 0 null,
    constraint FK_users_TO_inquiry_posts_1
        foreign key (author_id) references board.users (user_id)
);

create table board.inquiry_comments
(
    comment_id    bigint auto_increment
        primary key,
    comment       varchar(255)         not null,
    created_date  datetime             not null,
    modified_date datetime             null,
    delete_flag   tinyint(1) default 0 null,
    user_id       bigint               not null,
    post_id       bigint               not null,
    constraint FK_inquiry_posts_TO_inquiry_comments_1
        foreign key (post_id) references board.inquiry_posts (post_id),
    constraint FK_users_TO_inquiry_comments_1
        foreign key (user_id) references board.users (user_id)
);

create table board.inquiry_files
(
    file_id       bigint auto_increment
        primary key,
    file_name     varchar(255) not null,
    file_realname varchar(255) not null,
    size          int          null,
    extension     varchar(100) null,
    post_id       bigint       not null,
    constraint FK_inquiry_posts_TO_inquiry_files_1
        foreign key (post_id) references board.inquiry_posts (post_id)
);

create table board.notice_posts
(
    post_id       bigint auto_increment
        primary key,
    created_date  datetime             null,
    modified_date datetime             null,
    title         varchar(100)         null,
    content       text                 null,
    hits          bigint     default 0 not null,
    delete_flag   tinyint(1) default 0 not null,
    target        varchar(25)          null,
    author_id     bigint               not null,
    constraint FK_users_TO_notice_posts_1
        foreign key (author_id) references board.users (user_id)
);

create table board.notice_comments
(
    comment_id    bigint auto_increment
        primary key,
    comment       varchar(255)         not null,
    created_date  datetime             not null,
    modified_date datetime             null,
    delete_flag   tinyint(1) default 0 null,
    user_id       bigint               not null,
    post_id       bigint               not null,
    constraint FK_notice_posts_TO_notice_comments_1
        foreign key (post_id) references board.notice_posts (post_id),
    constraint FK_users_TO_notice_comments_1
        foreign key (user_id) references board.users (user_id)
);

create table board.notice_files
(
    file_id       bigint auto_increment
        primary key,
    file_name     varchar(255) not null,
    file_realname varchar(255) not null,
    size          int          null,
    extension     varchar(100) null,
    post_id       bigint       not null,
    constraint FK_notice_posts_TO_notice_files_1
        foreign key (post_id) references board.notice_posts (post_id)
);

create table board.qna_comments
(
    comment_id    bigint auto_increment
        primary key,
    comment       varchar(255)         not null,
    created_date  datetime             not null,
    modified_date datetime             null,
    delete_flag   tinyint(1) default 0 null,
    user_id       bigint               not null,
    post_id       bigint               not null,
    constraint FK_users_TO_qna_comments_1
        foreign key (user_id) references board.users (user_id)
);

create table board.qna_posts
(
    post_id            bigint auto_increment
        primary key,
    created_date       datetime             null,
    modified_date      datetime             null,
    title              varchar(100)         null,
    content            text                 null,
    hits               bigint     default 0 not null,
    adopted_comment_id bigint               null,
    delete_flag        tinyint(1) default 0 not null,
    category_id        bigint               not null,
    author_id          bigint               not null,
    constraint FK_categories_TO_qna_posts_1
        foreign key (category_id) references board.categories (category_id),
    constraint FK_qna_comments_TO_qna_posts_1
        foreign key (adopted_comment_id) references board.qna_comments (comment_id),
    constraint FK_users_TO_qna_posts_1
        foreign key (author_id) references board.users (user_id)
);

alter table board.qna_comments
    add constraint FK_qna_posts_TO_qna_comments_1
        foreign key (post_id) references board.qna_posts (post_id);

create table board.qna_files
(
    file_id       bigint auto_increment
        primary key,
    file_name     varchar(255) not null,
    file_realname varchar(255) not null,
    size          int          null,
    extension     varchar(100) null,
    post_id       bigint       not null,
    constraint FK_qna_posts_TO_qna_files_1
        foreign key (post_id) references board.qna_posts (post_id)
);

create view board.v_community_not_deleted_post as
select `board`.`community_posts`.`post_id`       AS `post_id`,
       `board`.`community_posts`.`created_date`  AS `created_date`,
       `board`.`community_posts`.`modified_date` AS `modified_date`,
       `board`.`community_posts`.`title`         AS `title`,
       `board`.`community_posts`.`author_id`     AS `author_id`,
       `board`.`community_posts`.`content`       AS `content`,
       `board`.`community_posts`.`hits`          AS `hits`,
       `board`.`community_posts`.`category_id`   AS `category_id`,
       `board`.`community_posts`.`delete_flag`   AS `delete_flag`
from `board`.`community_posts`
where `board`.`community_posts`.`delete_flag` = 0;

create view board.v_gallery_not_deleted_post as
select `board`.`gallery_posts`.`post_id`       AS `post_id`,
       `board`.`gallery_posts`.`created_date`  AS `created_date`,
       `board`.`gallery_posts`.`modified_date` AS `modified_date`,
       `board`.`gallery_posts`.`title`         AS `title`,
       `board`.`gallery_posts`.`content`       AS `content`,
       `board`.`gallery_posts`.`hits`          AS `hits`,
       `board`.`gallery_posts`.`delete_flag`   AS `delete_flag`,
       `board`.`gallery_posts`.`author_id`     AS `author_id`,
       `board`.`gallery_posts`.`thumbnail_id`  AS `thumbnail_id`
from `board`.`gallery_posts`
where `board`.`gallery_posts`.`delete_flag` = 0;

create view board.v_inquiry_not_deleted_post as
select `board`.`inquiry_posts`.`post_id`       AS `post_id`,
       `board`.`inquiry_posts`.`created_date`  AS `created_date`,
       `board`.`inquiry_posts`.`modified_date` AS `modified_date`,
       `board`.`inquiry_posts`.`title`         AS `title`,
       `board`.`inquiry_posts`.`content`       AS `content`,
       `board`.`inquiry_posts`.`hits`          AS `hits`,
       `board`.`inquiry_posts`.`delete_flag`   AS `delete_flag`,
       `board`.`inquiry_posts`.`author_id`     AS `author_id`,
       `board`.`inquiry_posts`.`secret`        AS `secret`
from `board`.`inquiry_posts`
where `board`.`inquiry_posts`.`delete_flag` = 0;

create view board.v_notice_not_deleted_post as
select `board`.`notice_posts`.`post_id`       AS `post_id`,
       `board`.`notice_posts`.`created_date`  AS `created_date`,
       `board`.`notice_posts`.`modified_date` AS `modified_date`,
       `board`.`notice_posts`.`title`         AS `title`,
       `board`.`notice_posts`.`content`       AS `content`,
       `board`.`notice_posts`.`hits`          AS `hits`,
       `board`.`notice_posts`.`delete_flag`   AS `delete_flag`,
       `board`.`notice_posts`.`target`        AS `target`,
       `board`.`notice_posts`.`author_id`     AS `author_id`
from `board`.`notice_posts`
where `board`.`notice_posts`.`delete_flag` = 0;

create view board.v_qna_not_deleted_post as
select `board`.`qna_posts`.`post_id`            AS `post_id`,
       `board`.`qna_posts`.`created_date`       AS `created_date`,
       `board`.`qna_posts`.`modified_date`      AS `modified_date`,
       `board`.`qna_posts`.`title`              AS `title`,
       `board`.`qna_posts`.`content`            AS `content`,
       `board`.`qna_posts`.`hits`               AS `hits`,
       `board`.`qna_posts`.`adopted_comment_id` AS `adopted_comment_id`,
       `board`.`qna_posts`.`delete_flag`        AS `delete_flag`,
       `board`.`qna_posts`.`category_id`        AS `category_id`,
       `board`.`qna_posts`.`author_id`          AS `author_id`
from `board`.`qna_posts`
where `board`.`qna_posts`.`delete_flag` = 0;

