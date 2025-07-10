CREATE DATABASE IF NOT EXISTS newsseug;
USE newsseug;

-- 기존 테이블 제거
--DROP TABLE IF EXISTS subscribes;
--DROP TABLE IF EXISTS subscribes_seq;
--DROP TABLE IF EXISTS reports;
--DROP TABLE IF EXISTS reports_seq;
--DROP TABLE IF EXISTS likes;
--DROP TABLE IF EXISTS likes_seq;
--DROP TABLE IF EXISTS histories;
--DROP TABLE IF EXISTS histories_seq;
--DROP TABLE IF EXISTS hates;
--DROP TABLE IF EXISTS hates_seq;
--DROP TABLE IF EXISTS bookmarks;
--DROP TABLE IF EXISTS bookmarks_seq;
--DROP TABLE IF EXISTS birth_year_view_counts;
--DROP TABLE IF EXISTS birth_year_view_counts_seq;
--DROP TABLE IF EXISTS articles;
--DROP TABLE IF EXISTS articles_seq;
--DROP TABLE IF EXISTS folders;
--DROP TABLE IF EXISTS folders_seq;
--DROP TABLE IF EXISTS members;
--DROP TABLE IF EXISTS members_seq;
--DROP TABLE IF EXISTS press;
--DROP TABLE IF EXISTS press_seq;

create table if not exists newsseug.members
(
    member_id         bigint auto_increment
        primary key,
    activation_status enum ('ACTIVE', 'INACTIVE')        not null,
    created_at        timestamp                          not null,
    updated_at        timestamp                          not null,
    birth             date                               null,
    gender            enum ('FEMALE', 'MALE')            null,
    is_first          bit                                null,
    nickname          varchar(255)                       null,
    provider          enum ('GOOGLE', 'KAKAO')           not null,
    provider_id       varchar(255)                       not null,
    role              enum ('ROLE_ADMIN', 'ROLE_MEMBER') null,
    profile_image_url varchar(255)                       null,
    constraint UKilvrhdmws4av314ebhvn26im5
        unique (provider_id)
);

create table if not exists newsseug.folders
(
    folder_id         bigint auto_increment
        primary key,
    activation_status enum ('ACTIVE', 'INACTIVE') not null,
    created_at        timestamp                   not null,
    updated_at        timestamp                   not null,
    article_count     bigint                      null,
    thumbnail_url     varchar(255)                null,
    title             varchar(10)                 not null,
    member_id         bigint                      not null,
    constraint FKco4xraxvdqyci1h0bfgnvq1yf
        foreign key (member_id) references newsseug.members (member_id)
);

create table if not exists newsseug.press
(
    press_id        bigint auto_increment
        primary key,
    description     varchar(255) null,
    image_url       varchar(255) null,
    name            varchar(255) null,
    subscribe_count bigint       null
);

create table if not exists newsseug.articles
(
    article_id        bigint auto_increment
        primary key,
    activation_status enum ('ACTIVE', 'INACTIVE')                                                       not null,
    created_at        timestamp                                                                         not null,
    updated_at        timestamp                                                                         not null,
    category          enum ('ACCIDENT', 'ECONOMY', 'POLITICS', 'SCIENCE', 'SOCIETY', 'SPORTS', 'WORLD') not null,
    content_url       varchar(255)                                                                      null,
    conversion_status enum ('EXCEED_TOKEN', 'FILTERED', 'RUNNING', 'SUCCESS', 'UNKNOWN_FAIL')           not null,
    hate_count        bigint                                                                            not null,
    like_count        bigint                                                                            not null,
    source_created_at timestamp                                                                         null,
    source_url        varchar(255)                                                                      not null,
    thumbnail_url     varchar(255)                                                                      null,
    title             varchar(255)                                                                      not null,
    video_url         varchar(255)                                                                      null,
    view_count        bigint                                                                            not null,
    press_id          bigint                                                                            not null,
    constraint FKd04v02rcdg3lmlu45mw9vfomc
        foreign key (press_id) references newsseug.press (press_id)
);

--create index idx_category_press_created_at_desc
--    on newsseug.articles (category, activation_status, conversion_status, source_created_at desc);
--
--create index idx_created_at_desc
--    on newsseug.articles (activation_status, conversion_status, source_created_at desc);

create table if not exists newsseug.birth_year_view_counts
(
    birth_view_count_id bigint auto_increment
        primary key,
    birth_year          int    null,
    view_count          bigint not null,
    article_id          bigint not null,
    constraint FK4hy7ruduqtt8ugpj8hx5ajrpc
        foreign key (article_id) references newsseug.articles (article_id)
);

create table if not exists newsseug.bookmarks
(
    bookmark_id       bigint auto_increment
        primary key,
    activation_status enum ('ACTIVE', 'INACTIVE') not null,
    created_at        timestamp                   not null,
    updated_at        timestamp                   not null,
    article_id        bigint                      not null,
    folder_id         bigint                      not null,
    constraint uniqueBookmark
        unique (article_id, folder_id),
    constraint FKgw1od0yvy1n3r2p0r4cb7x57a
        foreign key (folder_id) references newsseug.folders (folder_id),
    constraint FKrgc71ng0qy59rn9y741gi2mjr
        foreign key (article_id) references newsseug.articles (article_id)
);

create table if not exists newsseug.hates
(
    hate_id    bigint auto_increment
        primary key,
    created_at timestamp not null,
    article_id bigint    not null,
    member_id  bigint    not null,
    constraint unique_member_article
        unique (member_id, article_id),
    constraint FKeu114uu249552fnxpjoesei59
        foreign key (member_id) references newsseug.members (member_id),
    constraint FKp1hq7yrqfyhean2q5gj53i4em
        foreign key (article_id) references newsseug.articles (article_id)
);

create table if not exists newsseug.histories
(
    history_id        bigint auto_increment
        primary key,
    activation_status enum ('ACTIVE', 'INACTIVE') not null,
    created_at        timestamp                   not null,
    updated_at        timestamp                   not null,
    play_time         int                         not null,
    article_id        bigint                      not null,
    member_id         bigint                      not null,
    constraint unique_member_article
        unique (member_id, article_id),
    constraint FK6dwu9pkt9gecpbk3r8u4oqk7n
        foreign key (article_id) references newsseug.articles (article_id),
    constraint FKr5eq32k17h6xd5u1ridpahnlg
        foreign key (member_id) references newsseug.members (member_id)
);

create table if not exists newsseug.likes
(
    like_id    bigint auto_increment
        primary key,
    created_at timestamp not null,
    article_id bigint    not null,
    member_id  bigint    not null,
    constraint unique_member_aticle
        unique (member_id, article_id),
    constraint FK166rh7nhmtcajf0xo1f1i3s8p
        foreign key (member_id) references newsseug.members (member_id),
    constraint FKic6sfk54mitq78b48367cfiet
        foreign key (article_id) references newsseug.articles (article_id)
);

create table if not exists newsseug.reports
(
    report_id  bigint auto_increment
        primary key,
    created_at timestamp                                                                                not null,
    type       enum ('DISLIKE', 'EXPLICIT_CONTENT', 'HATE_SPEECH_OR_SYMBOLS', 'MISINFORMATION', 'SPAM') not null,
    article_id bigint                                                                                   not null,
    constraint FKvd08qavn9nwy8fa6n5349tb7
        foreign key (article_id) references newsseug.articles (article_id)
);

create table if not exists newsseug.subscribes
(
    subscribe_id      bigint auto_increment
        primary key,
    activation_status enum ('ACTIVE', 'INACTIVE') not null,
    created_at        timestamp                   not null,
    updated_at        timestamp                   not null,
    member_id         bigint                      null,
    press_id          bigint                      null,
    constraint uniqueSubscribe
        unique (member_id, press_id),
    constraint FK1ll7f1tjs7lt3xiqfcbrfmdum
        foreign key (member_id) references newsseug.members (member_id),
    constraint FKj3n9k136ixactnmfdhflm2i4n
        foreign key (press_id) references newsseug.press (press_id)
);