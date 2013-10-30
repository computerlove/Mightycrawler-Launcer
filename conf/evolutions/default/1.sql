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

CREATE TABLE downloads (
  crawl_id bigint,
  url VARCHAR(4095),
  http_code INTEGER DEFAULT 0,
  content_type VARCHAR(255),
  response_time INTEGER DEFAULT 0,
  downloaded_at DATETIME,
  downloaded BOOLEAN
);

CREATE TABLE links (
  crawl_id bigint,
  url_from VARCHAR(4095),
  url_to VARCHAR(4095)
);

# --- !Downs

DROP TABLE crawlerjob;
DROP TABLE downloads;
DROP TABLE links;