--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.1
-- Dumped by pg_dump version 9.5.1

-- Started on 2018-03-29 16:54:51


SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

create database dudebase;

\connect dudebase;



CREATE SEQUENCE public.seq_id_base_audit
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 20
  CACHE 1;
ALTER TABLE public.seq_id_base_audit
  OWNER TO postgres;
  
  
--
-- TOC entry 188 (class 1259 OID 16619)
-- Name: base_audit; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE base_audit (
    id bigint DEFAULT nextval('seq_id_base_audit'::regclass) NOT NULL,
    entity_type character varying(25),
    create_date timestamp without time zone,
    edit_date timestamp without time zone,
    user_id bigint,
    guid uuid
);


--
-- TOC entry 189 (class 1259 OID 16622)
-- Name: car; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE car (
    id bigint NOT NULL,
    brand text,
    name text,
    color text,
    body character varying(25)
);


--
-- TOC entry 190 (class 1259 OID 16628)
-- Name: driver; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE driver (
    id bigint NOT NULL,
    fullname text,
    age integer,
    car_id bigint
);


--
-- TOC entry 2131 (class 0 OID 16619)
-- Dependencies: 188
-- Data for Name: base_audit; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (2, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:41', 10, '276e2d96-d0f5-4f5c-b697-72d2b0944890');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (1, 'Car', '2018-03-24 21:38:00', '2018-03-24 21:39:42', 10, '169e43d4-e5e3-442a-9c2e-7acf59b3f584');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (4, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'b660217e-8562-4a1a-aaf0-f38dc5f10d1b');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (3, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'b3c04d25-9d65-45d5-bc97-417907458821');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (6, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, '195acf12-e7ba-49da-913d-ce871072689d');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (5, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'e5dc3405-39b0-43b0-82d6-e65ce5e8adb7');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (8, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, '69c46f7a-85dd-4a4c-b8c2-f8973033be6f');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (7, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, '1a3ae230-26ba-4158-aaab-d2248c07b29e');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (10, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'e620a051-1276-49e1-a7bb-ac4890bec0ee');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (9, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'a1c29bb5-7858-41dc-ae12-593999eaf22c');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (12, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, '72fa100e-9176-4c22-8b06-9f5f9dc58fd6');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (11, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'a957607e-9fed-4b38-9249-f5ba5a9cdb35');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (14, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'de8d0404-205e-485a-8577-5a1e84afaf96');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (13, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, '2c13ba3d-d00a-4ea2-9151-2e47a3ab0a97');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (16, 'Driver', '2018-03-24 21:38:09', '2018-03-24 21:39:42', 10, 'f6ffa218-1ce1-4de9-98a8-5ef72c1f3027');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (15, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:43', 10, 'bfae1073-80b9-4152-9b68-1a1e7ad63ea8');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (18, 'Driver', '2018-03-24 21:38:10', '2018-03-24 21:39:43', 10, '324368f2-f40c-45df-8fd4-bf21088a7998');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (17, 'Car', '2018-03-24 21:38:09', '2018-03-24 21:39:43', 10, 'e91f8449-2dab-420a-9a3d-8f859e49af50');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (20, 'Driver', '2018-03-24 21:38:10', '2018-03-24 21:39:43', 10, 'fe987910-4820-42d0-9730-29002b2f78e8');
INSERT INTO base_audit (id, entity_type, create_date, edit_date, user_id, guid) VALUES (19, 'Car', '2018-03-24 21:38:10', '2018-03-24 21:39:43', 10, 'bebca27c-ba9b-4aa3-bd82-a2b49ba7833c');


--
-- TOC entry 2132 (class 0 OID 16622)
-- Dependencies: 189
-- Data for Name: car; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO car (id, brand, name, color, body) VALUES (1, 'Volvo', 'BM-810', 'green', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (3, 'Volvo', 'CX-50', 'green', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (5, 'Volvo', 'CX-50', 'white', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (7, 'Volvo', 'LXLXDDDDD', 'white', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (9, 'Volvo', 'LXLXDDDDD', 'white', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (11, 'Volvo', 'CX-90', 'green', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (13, 'Volvo', 'BM-810', 'black', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (15, 'Volvo', 'CX-50', 'blue', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (17, 'Volvo', 'CX-90', 'black', 'HATCHBACK');
INSERT INTO car (id, brand, name, color, body) VALUES (19, 'Volvo', 'CX-90', 'black', 'HATCHBACK');


--
-- TOC entry 2133 (class 0 OID 16628)
-- Dependencies: 190
-- Data for Name: driver; Type: TABLE DATA; Schema: public; Owner: -
--

INSERT INTO driver (id, fullname, age, car_id) VALUES (2, 'Tramp D.D.', 27, 1);
INSERT INTO driver (id, fullname, age, car_id) VALUES (4, 'Petrov A.A.', 28, 3);
INSERT INTO driver (id, fullname, age, car_id) VALUES (6, 'Ivanov V.V.', 29, 5);
INSERT INTO driver (id, fullname, age, car_id) VALUES (8, 'Ivanov V.V.', 30, 7);
INSERT INTO driver (id, fullname, age, car_id) VALUES (10, 'Sidorov S.S.', 31, 9);
INSERT INTO driver (id, fullname, age, car_id) VALUES (12, 'Tramp D.D.', 32, 11);
INSERT INTO driver (id, fullname, age, car_id) VALUES (14, 'Ivanov V.V.', 33, 13);
INSERT INTO driver (id, fullname, age, car_id) VALUES (16, 'Sidorov S.S.', 34, 15);
INSERT INTO driver (id, fullname, age, car_id) VALUES (18, 'Sidorov S.S.', 35, 17);
INSERT INTO driver (id, fullname, age, car_id) VALUES (20, 'Tramp D.D.', 36, 19);


--
-- TOC entry 2012 (class 2606 OID 16639)
-- Name: base_audit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY base_audit
    ADD CONSTRAINT base_audit_pkey PRIMARY KEY (id);


--
-- TOC entry 2014 (class 2606 OID 16635)
-- Name: car_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY car
    ADD CONSTRAINT car_pkey PRIMARY KEY (id);


--
-- TOC entry 2016 (class 2606 OID 16637)
-- Name: driver_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY driver
    ADD CONSTRAINT driver_pkey PRIMARY KEY (id);


-- Completed on 2018-03-29 16:54:51

--
-- PostgreSQL database dump complete
--

