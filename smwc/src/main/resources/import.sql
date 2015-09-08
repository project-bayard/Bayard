--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

SET search_path = public, pg_catalog;

--
-- Data for Name: committee; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO committee (id, created, deleted, lastmodified, name) VALUES ('4028bb834eb7d609014eb87a08d80005', '2015-07-22T21:14:42.258', false, '2015-07-22T21:14:42.258', 'Health Care is a Human Right');
INSERT INTO committee (id, created, deleted, lastmodified, name) VALUES ('4028bb834eb7d609014eb87a46d20006', '2015-07-22T21:14:58.129', false, '2015-07-22T21:14:58.129', 'Work With Dignity');
INSERT INTO committee (id, created, deleted, lastmodified, name) VALUES ('4028bb834eb7d609014eb87a8a1c0007', '2015-07-22T21:15:15.355', false, '2015-07-22T21:15:15.355', 'Political Education Committee');


--
-- Data for Name: donor_info; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--



--
-- Data for Name: member_info; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb883b4fb0013', '2015-07-22T21:25:16.127', false, '2015-07-22T21:25:16.127', true, true, true, 1);
INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb88586a30014', '2015-07-22T21:27:15.340', false, '2015-07-22T21:27:15.340', true, false, true, 3);
INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb88877730017', '2015-07-22T21:30:28.052', false, '2015-07-22T21:30:28.052', true, true, true, 1);
INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb8893be70018', '2015-07-22T21:31:18.354', false, '2015-07-22T21:31:18.354', false, false, false, 2);


--
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb8830a700012', '2015-07-22T21:24:32.495', false, '2015-07-22T21:30:07.138', NULL, 9, 'New York', '1978-08-08T04:00:00.000Z', false, false, 'example@blue.com', 'Black', 'James', 'Male', 'N/A', true, NULL, 'English/Spanish', 'Schuyler', false, 'unk', false, NULL, '8981239819', NULL, 'Black', 'N/A', 'NY', '989 Avenue of the Americas', '08012', NULL, NULL);
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '2015-07-22T21:20:03.501', false, '2015-07-22T21:25:51.918', '1', 0, 'Portland', '1988-06-13T04:00:00.000Z', false, false, 'example@usm.edu', 'N/A', 'Scott', 'Male', '$100K', false, NULL, 'English', 'Kimball', true, 'Russell', false, NULL, '1234561231', NULL, 'White', 'Heterosexual', 'ME', '123 Grant St', '04102', NULL, '4028bb834eb7d609014eb883b4fb0013');
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb8805415000f', '2015-07-22T21:21:34.739', false, '2015-07-22T21:31:03.573', NULL, 0, 'Falmouth', NULL, false, false, 'example@eyes.org', NULL, 'Kaleigh', NULL, NULL, true, NULL, 'Spanish', 'Duffy', true, 'Katherine', true, NULL, '8789897676', '1237678767', NULL, NULL, 'NY', '123 Boulevard Blvd.', '04030', NULL, '4028bb834eb7d609014eb88877730017');
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb873e2050000', '2015-07-22T21:07:59.083', false, '2015-07-22T21:27:55.723', '4', 0, 'Lowell', '1978-07-08T04:00:00.000Z', true, false, 'andrew.dorman.taylor@gmail.com', 'N/A', 'Andrew', 'Male', '$30K', true, NULL, 'English', 'Taylor', true, 'Dorman', false, NULL, '2073170660', '2073170660', 'N/A', 'Homosexual', 'ME', '172 Middle St. Apt. 405', '01852', NULL, '4028bb834eb7d609014eb88586a30014');
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb88158360010', '2015-07-22T21:22:41.332', false, '2015-07-22T21:28:39.469', '6', 0, 'New York', NULL, false, false, 'example@trump.org', NULL, 'Donald', NULL, NULL, false, NULL, 'Portuguese', 'Trump', false, NULL, false, NULL, '8981238912', NULL, NULL, NULL, 'NY', '666 Street St.', '89012', NULL, NULL);
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', '2015-07-22T21:20:32.138', false, '2015-07-22T21:32:01.220', NULL, 5, 'Portland', NULL, false, false, 'example@maine.edu', NULL, 'Bruce', NULL, NULL, true, NULL, 'English', 'MacLeod', true, NULL, true, NULL, NULL, NULL, NULL, NULL, 'ME', '123 Falmouth St', '04103', NULL, '4028bb834eb7d609014eb8893be70018');
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb87687190001', '2015-07-22T21:10:52.439', false, '2015-07-22T21:32:21.446', '504', 7, 'Boston', NULL, false, false, 'example@gmail.com', NULL, 'Sarah', NULL, NULL, false, NULL, 'English', 'Taylor', false, 'Nicole', false, NULL, '2023231222', '2023231222', NULL, NULL, 'ME', '123 Lane St', '03030', NULL, NULL);
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb8822d7d0011', '2015-07-22T21:23:35.932', false, '2015-07-22T21:32:52.493', '8', 7, 'Munich', NULL, false, false, 'none', NULL, 'Rainer', NULL, NULL, false, NULL, 'German', 'Rilke', false, 'Maria', true, NULL, 'none', 'none', NULL, NULL, 'ME', '123 Germany Ln.', 'N/A', NULL, NULL);


--
-- Data for Name: contact_committee; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87a08d80005');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87a8a1c0007');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87a08d80005');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87a8a1c0007');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', '4028bb834eb7d609014eb87a8a1c0007');


--
-- Data for Name: event; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO event (id, created, deleted, lastmodified, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87b61a80008', '2015-07-22T21:16:10.531', false, '2015-07-22T21:16:10.531', '2015-01-05', 'City Hall, Portland, ME', 'WWD Rally', 'Volunteers needed', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO event (id, created, deleted, lastmodified, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87c43390009', '2015-07-22T21:17:08.281', false, '2015-07-22T21:17:08.281', '2015-06-06', 'State house, Augusta, ME', 'WWD Rally', 'Rain-out date will be 06-10-2015', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO event (id, created, deleted, lastmodified, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87ccc57000a', '2015-07-22T21:17:43.383', false, '2015-07-22T21:17:43.383', '2016-12-11', 'Biddeford, ME', 'Canvassing in Biddeford', 'Canvassers needed', '4028bb834eb7d609014eb87a08d80005');
INSERT INTO event (id, created, deleted, lastmodified, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87d8f97000b', '2015-07-22T21:18:33.366', false, '2015-07-22T21:18:33.366', '2015-01-12', 'Portland, ME', 'Round table at USM', NULL, '4028bb834eb7d609014eb87a8a1c0007');
INSERT INTO event (id, created, deleted, lastmodified, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87e1c65000c', '2015-07-22T21:19:09.412', false, '2015-07-22T21:19:09.412', '2015-06-06', 'SMWC office', 'Committee meeting', 'Need RSVPs', '4028bb834eb7d609014eb87a08d80005');


--
-- Data for Name: contact_events; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87c43390009');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87b61a80008');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87d8f97000b');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87e1c65000c');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87d8f97000b');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87ccc57000a');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87c43390009');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87b61a80008');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87ccc57000a');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87d8f97000b');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87e1c65000c');
INSERT INTO contact_events (contact_id, event_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', '4028bb834eb7d609014eb87d8f97000b');


--
-- Data for Name: organization; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO organization (id, created, deleted, lastmodified, city, description, email, name, phonenumber, primarycontactname, state, streetaddress, zipcode) VALUES ('4028bb834eb7d609014eb8771a7b0002', '2015-07-22T21:11:30.164', false, '2015-07-22T21:11:30.164', 'New York', 'The NAACP', 'example@gmail.com', 'NAACP', '1234567899', 'John Doe', 'NY', '123 Washington Ave', '12345');
INSERT INTO organization (id, created, deleted, lastmodified, city, description, email, name, phonenumber, primarycontactname, state, streetaddress, zipcode) VALUES ('4028bb834eb7d609014eb87793370003', '2015-07-22T21:12:01.077', false, '2015-07-22T21:12:01.077', 'Portland', 'The SMWC', 'example@smwc.org', 'SMWC', '1234567899', 'Drew Christopher', 'ME', '123 Lane Ave', '04103');
INSERT INTO organization (id, created, deleted, lastmodified, city, description, email, name, phonenumber, primarycontactname, state, streetaddress, zipcode) VALUES ('4028bb834eb7d609014eb87958ed0004', '2015-07-22T21:13:57.228', false, '2015-07-22T21:13:57.228', 'Portland', 'The Dems of ME', 'example@mdp.org', 'Maine Democratic Party', '0987654321', 'Buster Keaton', 'ME', '456 Avenue Ave', '04103');


--
-- Data for Name: contact_organization; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87793370003');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87958ed0004');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb8771a7b0002');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87793370003');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87958ed0004');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', '4028bb834eb7d609014eb87958ed0004');


--
-- Data for Name: donation; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

--
-- EncounterType
--
INSERT INTO ENCOUNTER_TYPE (id, created, deleted, lastmodified,name) VALUES ('4028cb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 'Call');
INSERT INTO ENCOUNTER_TYPE (id, created, deleted, lastmodified,name) VALUES ('4028qb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 'Walk in');
INSERT INTO ENCOUNTER_TYPE (id, created, deleted, lastmodified,name) VALUES ('4028nb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 'Other');



--
-- Data for Name: encounter; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 0, '2015-08-08', 'Bad attitude. Do not call back', false, 'Call', '4028bb834eb7d609014eb88158360010', NULL, '4028bb834eb7d609014eb8805415000f');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb88825be0016', '2015-07-22T21:30:07.138', false, '2015-07-22T21:30:07.138', 9, '2014-01-01', 'Responded to canvassing on street in Manhattan. Seemed like a nice guy.', false, 'Other', '4028bb834eb7d609014eb8830a700012', NULL, '4028bb834eb7d609014eb873e2050000');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb889a4e00019', '2015-07-22T21:31:45.211', false, '2015-07-22T21:31:45.211', 5, '2001-01-01', 'Walk-in during annual new member orientation', false, 'Other', '4028bb834eb7d609014eb87f5f8b000e', NULL, '4028bb834eb7d609014eb873e2050000');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb88a326f001a', '2015-07-22T21:32:21.446', false, '2015-07-22T21:32:21.446', 7, '2011-07-07', 'Cold-call', false, 'Call', '4028bb834eb7d609014eb87687190001', NULL, '4028bb834eb7d609014eb873e2050000');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb88aabbf001b', '2015-07-22T21:32:52.493', false, '2015-07-22T21:32:52.493', 7, '2001-08-08', 'Met at academic conference', false, 'Other', '4028bb834eb7d609014eb8822d7d0011', NULL, '4028bb834eb7d609014eb8830a700012');


--
-- Data for Name: form; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--



--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: smwc_postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 1, true);


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: smwc_postgres
--

--INSERT INTO users (id, email, firstname, lastname, passwordhash, role) VALUES (1, 'superuser@email.com', 'first', 'last', '$2a$10$gX9J1R193HlxRUQJu5K7VemGzd8OXOF6q137yAM/0FO0Rmb9Z6Nf2', 'ROLE_SUPERUSER');


--
-- PostgreSQL database dump complete
--

