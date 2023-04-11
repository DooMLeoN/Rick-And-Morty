--liquibase formatted sql
--changeset <postgres>:<add: add-external-id-colum-to-movie-character-table.sql>
ALTER TABLE public.movie_character ADD external_id bigint;

--rollback ALTER TABLE DROP COLUMN external_id;