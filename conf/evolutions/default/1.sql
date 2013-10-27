# CrawlerJob schema

# --- !Ups

CREATE TABLE crawlerjob (
    id bigint auto_increment primary key,
    label varchar(255),
    startUrls varchar(4000)
);

# --- !Downs

DROP TABLE crawlerjob;