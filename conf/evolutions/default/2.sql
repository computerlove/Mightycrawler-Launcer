# CrawlerJob schema

# --- !Ups
ALTER TABLE crawlerjob ADD COLUMN excludePattern varchar(255) default '';

# --- !Downs
ALTER TABLE crawlerjob DROP COLUMN excludePattern;