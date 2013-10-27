# CrawlerJob schema

# --- !Ups

CREATE TABLE crawlerjob (
  id bigint auto_increment primary key,
  label varchar(255),
  startUrls varchar(4000),
  extractPattern varchar(255),
  linkPattern varchar(255),
  storePattern varchar(255),
  userAgent varchar(255),
  downloadThreads integer,
  maxVisits integer,
  maxDownloads integer,
  maxRecursion integer,
  maxTime integer,
  downloadDelay integer,
  responseTimeout integer,
  crawlerTimeout integer
);

# --- !Downs

DROP TABLE crawlerjob;