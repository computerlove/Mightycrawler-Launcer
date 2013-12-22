# CrawlerJob schema

# --- !Ups

CREATE TABLE crawlerjob (
  id integer AUTO_INCREMENT primary key,
  label varchar(255),
  startUrls varchar(4000),
  includePattern varchar(255),
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
  crawl_id integer,
  url VARCHAR(4095),
  http_code INTEGER DEFAULT 0,
  content_type VARCHAR(255),
  page_type VARCHAR(255),
  response_time INTEGER DEFAULT 0,
  downloaded_at DATETIME,
  downloaded BIT
);

CREATE TABLE links (
  crawl_id integer,
  url_from VARCHAR(4095),
  url_to VARCHAR(4095)
);

CREATE TABLE crawlerjobrun (
  crawlerJobRunId integer AUTO_INCREMENT primary key,
  crawlerJobId integer,
  starttime datetime,
  stoptime datetime
);

# --- !Downs

DROP TABLE crawlerjob;
DROP TABLE crawlerjobrun;
DROP TABLE downloads;
DROP TABLE links;