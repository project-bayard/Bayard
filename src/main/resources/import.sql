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
-- Data for Name: alinskygroup; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO alinskygroup (id, created, deleted, lastmodified, groupname) VALUES ('ff808181507bf0ea01507bf212c00000', '2015-10-18T13:14:33.528', false, '2015-10-18T13:14:33.528', 'Work With Dignity');
INSERT INTO alinskygroup (id, created, deleted, lastmodified, groupname) VALUES ('ff808181507bf0ea01507bf288100001', '2015-10-18T13:15:03.567', false, '2015-10-18T13:15:03.567', 'Health Care is a Human Right');


--
-- Data for Name: alinskygroup_aggregation; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf212c00000', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf288100001', '4028bb834eb7d609014eb87a08d80005');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf288100001', '4028bb834eb7d609014eb87e1c65000c');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf212c00000', '4028bb834eb7d609014eb87c43390009');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf212c00000', '4028bb834eb7d609014eb87b61a80008');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf212c00000', '4028bb834eb7d609014eb87ccc57000a');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf288100001', '4028bb834eb7d609014eb87ccc57000a');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf288100001', '4028bb834eb7d609014eb87793370003');
INSERT INTO alinskygroup_aggregation (alinskygroup_id, aggregation_id) VALUES ('ff808181507bf0ea01507bf212c00000', '4028bb834eb7d609014eb87958ed0004');


--
-- Data for Name: budgetitem; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO budgetitem (id, created, deleted, lastmodified, name) VALUES ('ff808181538fbe7e01538fbf51a80002', '2016-03-19T12:39:53.507', false, '2016-03-19T12:39:53.507', 'Item X');
INSERT INTO budgetitem (id, created, deleted, lastmodified, name) VALUES ('ff808181538fbe7e01538fbf62450003', '2016-03-19T12:39:57.764', false, '2016-03-19T12:39:57.764', 'Item Y');


--
-- Data for Name: committee; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO committee (id, created, deleted, lastmodified, aggregationtype, name) VALUES ('4028bb834eb7d609014eb87a8a1c0007', '2015-07-22T21:15:15.355', false, '2015-07-22T21:15:15.355', NULL, 'Political Education Committee');
INSERT INTO committee (id, created, deleted, lastmodified, aggregationtype, name) VALUES ('4028bb834eb7d609014eb87a08d80005', '2015-07-22T21:14:42.258', false, '2015-10-18T13:14:06.913', NULL, 'Health Care is a Human Right -- Portland');
INSERT INTO committee (id, created, deleted, lastmodified, aggregationtype, name) VALUES ('4028bb834eb7d609014eb87a46d20006', '2015-07-22T21:14:58.129', false, '2015-10-18T13:14:24.327', NULL, 'Work With Dignity  -- Lewiston');


--
-- Data for Name: donor_info; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO donor_info (id, created, deleted, lastmodified, currentsustainer) VALUES ('ff808181538fbe7e01538fc201070009', '2016-03-19T12:42:49.426', false, '2016-03-19T12:43:55.193', false);


--
-- Data for Name: member_info; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb883b4fb0013', '2015-07-22T21:25:16.127', false, '2015-07-22T21:25:16.127', true, true, true, 1);
INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb88586a30014', '2015-07-22T21:27:15.340', false, '2015-07-22T21:27:15.340', true, false, true, 3);
INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb88877730017', '2015-07-22T21:30:28.052', false, '2015-07-22T21:30:28.052', true, true, true, 1);
INSERT INTO member_info (id, created, deleted, lastmodified, attendedorientation, paiddues, signedagreement, status) VALUES ('4028bb834eb7d609014eb8893be70018', '2015-07-22T21:31:18.354', false, '2015-07-22T21:31:18.354', false, false, false, 2);


--
-- Data for Name: contact; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb8830a700012', '2015-07-22T21:24:32.495', false, '2015-07-22T21:30:07.138', NULL, 9, 'New York', '1978-08-08T04:00:00.000Z', false, false, 'example@blue.com', 'Black', 'James', 'Male', 'N/A', true, NULL, 'English/Spanish', 'Schuyler', false, 'unk', false, NULL, '8981239819', NULL, 'Black', 'N/A', 'NY', '989 Avenue of the Americas', '08012', NULL, NULL);
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb873e2050000', '2015-07-22T21:07:59.083', false, '2015-07-22T21:27:55.723', '4', 0, 'Lowell', '1978-07-08T04:00:00.000Z', true, false, 'andrew.dorman.taylor@gmail.com', 'N/A', 'Andrew', 'Male', '$30K', true, NULL, 'English', 'Taylor', true, 'Dorman', false, NULL, '2073170660', '2073170660', 'N/A', 'Homosexual', 'ME', '172 Middle St. Apt. 405', '01852', NULL, '4028bb834eb7d609014eb88586a30014');
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb88158360010', '2015-07-22T21:22:41.332', false, '2015-10-18T13:28:13.214', '6', 0, 'New York', '0NaN-NaN-NaN', false, false, 'example@trump.org', 'Hispanic/Latino', 'Donald', 'Male', '$100K +', false, NULL, 'Portuguese', 'Trump', false, NULL, false, NULL, '8981238912', NULL, 'White', 'N/A', 'NY', '666 Street St.', '89012', NULL, NULL);
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb8822d7d0011', '2015-07-22T21:23:35.932', false, '2015-10-18T13:28:24.432', '8', 7, 'Munich', NULL, false, false, 'none', NULL, 'Rainer', NULL, NULL, false, NULL, 'German', 'Rilke', false, 'Maria', true, NULL, 'none', 'none', NULL, NULL, 'ME', '123 Germany Ln.', 'N/A', NULL, NULL);
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', '2015-07-22T21:20:32.138', false, '2015-10-18T13:28:32.047', NULL, 5, 'Portland', NULL, false, false, 'example@maine.edu', NULL, 'Bruce', NULL, NULL, true, NULL, 'English', 'MacLeod', true, NULL, true, NULL, NULL, NULL, NULL, NULL, 'ME', '123 Falmouth St', '04103', NULL, '4028bb834eb7d609014eb8893be70018');
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb8805415000f', '2015-07-22T21:21:34.739', false, '2015-10-18T13:28:59.394', NULL, 0, 'Falmouth', '2015-10-15', true, false, 'example@eyes.org', 'Non Hispanic/Latino', 'Kaleigh', 'Female', '$100K +', true, NULL, 'Spanish', 'Duffy', true, 'Katherine', true, NULL, '8789897676', '1237678767', 'Black or African American', 'Homosexual', 'NY', '123 Boulevard Blvd.', '04030', NULL, '4028bb834eb7d609014eb88877730017');
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb87687190001', '2015-07-22T21:10:52.439', false, '2015-10-18T13:29:25.552', '504', 7, 'Boston', '2015-10-08', true, false, 'example@gmail.com', 'Non Hispanic/Latino', 'Sarah', 'N/A', '$30K - $40K', false, NULL, 'English', 'Taylor', false, 'Nicole', false, NULL, '2023231222', '2023231222', 'American Indian or Alaska Native', 'Asexual', 'ME', '123 Lane St', '03030', NULL, NULL);
INSERT INTO contact (id, created, deleted, lastmodified, aptnumber, assessment, city, dateofbirth, disabled, donor, email, ethnicity, firstname, gender, incomebracket, initiator, interests, language, lastname, member, middlename, needsfollowup, occupation, phonenumber1, phonenumber2, race, sexualorientation, state, streetaddress, zipcode, donorinfo_id, memberinfo_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '2015-07-22T21:20:03.501', false, '2016-03-19T12:43:55.193', '1', 0, 'Portland', '1988-06-13', true, true, 'example@usm.edu', 'Non Hispanic/Latino', 'Scott', 'Male', '$20K - $30K', false, NULL, 'English', 'Kimball', true, 'Russell', false, NULL, '1234561231', NULL, 'White', 'Heterosexual', 'ME', '123 Grant St', '04102', 'ff808181538fbe7e01538fc201070009', '4028bb834eb7d609014eb883b4fb0013');


--
-- Data for Name: contact_alinskygroup; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO contact_alinskygroup (contact_id, alinskygroup_id) VALUES ('4028bb834eb7d609014eb88158360010', 'ff808181507bf0ea01507bf288100001');
INSERT INTO contact_alinskygroup (contact_id, alinskygroup_id) VALUES ('4028bb834eb7d609014eb8822d7d0011', 'ff808181507bf0ea01507bf288100001');
INSERT INTO contact_alinskygroup (contact_id, alinskygroup_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', 'ff808181507bf0ea01507bf212c00000');


--
-- Data for Name: contact_committee; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87a08d80005');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87a8a1c0007');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87a08d80005');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87a8a1c0007');
INSERT INTO contact_committee (contact_id, committee_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', '4028bb834eb7d609014eb87a8a1c0007');


--
-- Data for Name: event; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO event (id, created, deleted, lastmodified, aggregationtype, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87b61a80008', '2015-07-22T21:16:10.531', false, '2015-07-22T21:16:10.531', NULL, '2015-01-05', 'City Hall, Portland, ME', 'WWD Rally', 'Volunteers needed', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO event (id, created, deleted, lastmodified, aggregationtype, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87c43390009', '2015-07-22T21:17:08.281', false, '2015-07-22T21:17:08.281', NULL, '2015-06-06', 'State house, Augusta, ME', 'WWD Rally', 'Rain-out date will be 06-10-2015', '4028bb834eb7d609014eb87a46d20006');
INSERT INTO event (id, created, deleted, lastmodified, aggregationtype, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87d8f97000b', '2015-07-22T21:18:33.366', false, '2015-07-22T21:18:33.366', NULL, '2015-01-12', 'Portland, ME', 'Round table at USM', NULL, '4028bb834eb7d609014eb87a8a1c0007');
INSERT INTO event (id, created, deleted, lastmodified, aggregationtype, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87e1c65000c', '2015-07-22T21:19:09.412', false, '2015-07-22T21:19:09.412', NULL, '2015-06-06', 'SMWC office', 'Committee meeting', 'Need RSVPs', '4028bb834eb7d609014eb87a08d80005');
INSERT INTO event (id, created, deleted, lastmodified, aggregationtype, dateheld, location, name, notes, committee_id) VALUES ('4028bb834eb7d609014eb87ccc57000a', '2015-07-22T21:17:43.383', false, '2016-03-21T14:22:45.097', NULL, '2016-12-11', 'Biddeford, ME', 'Canvassing in Biddeford', 'Canvassers needed', '4028bb834eb7d609014eb87a08d80005');


--
-- Data for Name: contact_events; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
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
-- Data for Name: organization; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO organization (id, created, deleted, lastmodified, aggregationtype, city, description, email, name, phonenumber, primarycontactname, state, streetaddress, zipcode) VALUES ('4028bb834eb7d609014eb8771a7b0002', '2015-07-22T21:11:30.164', false, '2015-07-22T21:11:30.164', NULL, 'New York', 'The NAACP', 'example@gmail.com', 'NAACP', '1234567899', 'John Doe', 'NY', '123 Washington Ave', '12345');
INSERT INTO organization (id, created, deleted, lastmodified, aggregationtype, city, description, email, name, phonenumber, primarycontactname, state, streetaddress, zipcode) VALUES ('4028bb834eb7d609014eb87793370003', '2015-07-22T21:12:01.077', false, '2015-07-22T21:12:01.077', NULL, 'Portland', 'The SMWC', 'example@smwc.org', 'SMWC', '1234567899', 'Drew Christopher', 'ME', '123 Lane Ave', '04103');
INSERT INTO organization (id, created, deleted, lastmodified, aggregationtype, city, description, email, name, phonenumber, primarycontactname, state, streetaddress, zipcode) VALUES ('4028bb834eb7d609014eb87958ed0004', '2015-07-22T21:13:57.228', false, '2016-03-21T14:20:40.851', NULL, 'Portland', 'The Dems of ME', 'example@mdp.org', 'Maine Democratic Party', '0987654321', 'Buster Keaton', 'ME', '456 Avenue Ave', '04103');


--
-- Data for Name: contact_organization; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb87eefae000d', '4028bb834eb7d609014eb87793370003');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb87958ed0004');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb873e2050000', '4028bb834eb7d609014eb8771a7b0002');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87793370003');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb8805415000f', '4028bb834eb7d609014eb87958ed0004');
INSERT INTO contact_organization (contact_id, org_id) VALUES ('4028bb834eb7d609014eb87f5f8b000e', '4028bb834eb7d609014eb87958ed0004');


--
-- Data for Name: demographiccategory; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO demographiccategory (id, created, deleted, lastmodified, name) VALUES ('ff808181507bf0ea01507bf4bcaa0002', '2015-10-18T13:17:28.100', false, '2015-10-18T13:17:28.100', 'race');
INSERT INTO demographiccategory (id, created, deleted, lastmodified, name) VALUES ('ff808181507bf0ea01507bf6d5d70008', '2015-10-18T13:19:45.623', false, '2015-10-18T13:19:45.623', 'ethnicity');
INSERT INTO demographiccategory (id, created, deleted, lastmodified, name) VALUES ('ff808181507bf0ea01507bf7ff74000b', '2015-10-18T13:21:01.811', false, '2015-10-18T13:21:01.811', 'gender');
INSERT INTO demographiccategory (id, created, deleted, lastmodified, name) VALUES ('ff808181507bf0ea01507bfa1e2f000f', '2015-10-18T13:23:20.751', false, '2015-10-18T13:23:20.751', 'sexualOrientation');
INSERT INTO demographiccategory (id, created, deleted, lastmodified, name) VALUES ('ff808181507bf0ea01507bfb7c080015', '2015-10-18T13:24:50.312', false, '2015-10-18T13:24:50.312', 'incomeBracket');


--
-- Data for Name: demographicoption; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf4bcb40003', '2015-10-18T13:17:28.095', false, '2015-10-18T13:17:28.095', 'White', 'ff808181507bf0ea01507bf4bcaa0002');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf534a60004', '2015-10-18T13:17:58.817', false, '2015-10-18T13:17:58.817', 'Black or African American', 'ff808181507bf0ea01507bf4bcaa0002');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf553600005', '2015-10-18T13:18:06.684', false, '2015-10-18T13:18:06.684', 'American Indian or Alaska Native', 'ff808181507bf0ea01507bf4bcaa0002');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf566cb0006', '2015-10-18T13:18:11.653', false, '2015-10-18T13:18:11.653', 'Asian', 'ff808181507bf0ea01507bf4bcaa0002');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf588a10007', '2015-10-18T13:18:20.316', false, '2015-10-18T13:18:20.316', 'Native Hawaiian or Other Pacific Islander', 'ff808181507bf0ea01507bf4bcaa0002');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf6d5dc0009', '2015-10-18T13:19:45.621', false, '2015-10-18T13:19:45.621', 'Hispanic/Latino', 'ff808181507bf0ea01507bf6d5d70008');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf7be42000a', '2015-10-18T13:20:45.119', false, '2015-10-18T13:20:45.119', 'Non Hispanic/Latino', 'ff808181507bf0ea01507bf6d5d70008');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf7ff79000c', '2015-10-18T13:21:01.809', false, '2015-10-18T13:21:01.809', 'Male', 'ff808181507bf0ea01507bf7ff74000b');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf809a7000d', '2015-10-18T13:21:04.421', false, '2015-10-18T13:21:04.421', 'Female', 'ff808181507bf0ea01507bf7ff74000b');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bf8332e000e', '2015-10-18T13:21:15.052', false, '2015-10-18T13:21:15.052', 'N/A', 'ff808181507bf0ea01507bf7ff74000b');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfa1e360010', '2015-10-18T13:23:20.749', false, '2015-10-18T13:23:20.749', 'Asexual', 'ff808181507bf0ea01507bfa1e2f000f');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfa3bfb0011', '2015-10-18T13:23:28.374', false, '2015-10-18T13:23:28.374', 'Bisexual', 'ff808181507bf0ea01507bfa1e2f000f');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfa52110012', '2015-10-18T13:23:34.029', false, '2015-10-18T13:23:34.029', 'Heterosexual', 'ff808181507bf0ea01507bfa1e2f000f');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfa64f00013', '2015-10-18T13:23:38.860', false, '2015-10-18T13:23:38.860', 'Homosexual', 'ff808181507bf0ea01507bfa1e2f000f');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfa85df0014', '2015-10-18T13:23:47.292', false, '2015-10-18T13:23:47.292', 'N/A', 'ff808181507bf0ea01507bfa1e2f000f');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfb7c0f0016', '2015-10-18T13:24:50.310', false, '2015-10-18T13:24:50.310', '$0 - $10K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfb984f0017', '2015-10-18T13:24:57.548', false, '2015-10-18T13:24:57.548', '$10K - $20K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfbb5510018', '2015-10-18T13:25:04.971', false, '2015-10-18T13:25:04.971', '$20K - $30K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfbe4300019', '2015-10-18T13:25:16.972', false, '2015-10-18T13:25:16.972', '$30K - $40K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfc0413001a', '2015-10-18T13:25:25.136', false, '2015-10-18T13:25:25.136', '$40K - $50K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfc3097001b', '2015-10-18T13:25:36.532', false, '2015-10-18T13:25:36.532', '$50K - $60K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfc484f001c', '2015-10-18T13:25:42.603', false, '2015-10-18T13:25:42.603', '$60K - $70K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfc61cb001d', '2015-10-18T13:25:49.127', false, '2015-10-18T13:25:49.127', '$70K - $80K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfc7c61001e', '2015-10-18T13:25:55.935', false, '2015-10-18T13:25:55.935', '$80K - $90K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfca74f001f', '2015-10-18T13:26:06.923', false, '2015-10-18T13:26:06.923', '$90K - $100K', 'ff808181507bf0ea01507bfb7c080015');
INSERT INTO demographicoption (id, created, deleted, lastmodified, name, category_id) VALUES ('ff808181507bf0ea01507bfcbb310020', '2015-10-18T13:26:12.012', false, '2015-10-18T13:26:12.012', '$100K +', 'ff808181507bf0ea01507bfb7c080015');


--
-- Data for Name: donation; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181538fbe7e01538fbfd53e0004', '2016-03-19T12:40:27.035', false, '2016-03-19T12:40:27.038', 100, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031078', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0020178', 'Credit Card', NULL, false, 'ff808181538fbe7e01538fbf51a80002', NULL, '4028bb834eb7d609014eb87958ed0004', NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181538fbe7e01538fc04f8e0005', '2016-03-19T12:40:58.400', false, '2016-03-19T12:40:58.401', 50, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031178', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0020c78', 'PayPal', NULL, false, 'ff808181538fbe7e01538fbf62450003', NULL, '4028bb834eb7d609014eb87958ed0004', NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181538fbe7e01538fc09f8d0006', '2016-03-19T12:41:18.899', false, '2016-03-19T12:41:18.900', 12, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031978', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0030a78', NULL, NULL, false, NULL, NULL, '4028bb834eb7d609014eb87958ed0004', NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181538fbe7e01538fc109730007', '2016-03-19T12:41:45.949', false, '2016-03-19T12:41:45.951', 100, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0030c78', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0030178', 'In-kind', NULL, false, NULL, '4028bb834eb7d609014eb87ccc57000a', NULL, NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181538fbe7e01538fc155ea0008', '2016-03-19T12:42:05.533', false, '2016-03-19T12:42:05.534', 500, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031878', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031778', 'Check', NULL, false, 'ff808181538fbe7e01538fbf62450003', '4028bb834eb7d609014eb87ccc57000a', NULL, NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181538fbe7e01538fc20107000a', '2016-03-19T12:42:49.425', false, '2016-03-19T12:42:49.426', 30, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031f78', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0030578', 'PayPal', 'Party funds', false, NULL, NULL, NULL, 'ff808181538fbe7e01538fc201070009');
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181538fbe7e01538fc26706000b', '2016-03-19T12:43:15.539', false, '2016-03-19T12:43:15.540', 100, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0021478', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0021378', 'Cash', NULL, false, 'ff808181538fbe7e01538fbf62450003', NULL, NULL, 'ff808181538fbe7e01538fc201070009');
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181539a550d01539a59de000000', '2016-03-21T14:04:54.014', false, '2016-03-21T14:04:54.017', 100, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031278', NULL, NULL, false, 'ff808181538fbe7e01538fbf62450003', NULL, '4028bb834eb7d609014eb87958ed0004', NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181539a550d01539a5a23060001', '2016-03-21T14:05:11.667', false, '2016-03-21T14:05:11.667', 90, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031e78', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0030178', 'PayPal', NULL, false, NULL, NULL, '4028bb834eb7d609014eb87958ed0004', NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181539a550d01539a5a5d460002', '2016-03-21T14:05:26.613', false, '2016-03-21T14:05:26.614', 1, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', 'Check', 'restricted', false, 'ff808181538fbe7e01538fbf51a80002', NULL, '4028bb834eb7d609014eb87958ed0004', NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181539a550d01539a6850470003', '2016-03-21T14:20:40.796', false, '2016-03-21T14:20:40.797', 90, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', 'PayPal', NULL, false, NULL, NULL, '4028bb834eb7d609014eb87958ed0004', NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181539a550d01539a69493c0004', '2016-03-21T14:21:44.539', false, '2016-03-21T14:21:44.540', 11111, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', 'Cash', NULL, false, NULL, '4028bb834eb7d609014eb87ccc57000a', NULL, NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181539a550d01539a69c48b0005', '2016-03-21T14:22:16.082', false, '2016-03-21T14:22:16.084', 54, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0031578', 'In-kind', NULL, false, 'ff808181538fbe7e01538fbf62450003', '4028bb834eb7d609014eb87ccc57000a', NULL, NULL);
INSERT INTO donation (id, created, deleted, lastmodified, amount, anonymous, dateofdeposit, dateofreceipt, method, restrictedtocategory, standalone, budget_item_id, event_id, organization_id, donor_info_id) VALUES ('ff808181539a550d01539a6a35960006', '2016-03-21T14:22:45.043', false, '2016-03-21T14:22:45.043', 1, false, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007dc031578', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007db031578', 'PayPal', NULL, false, NULL, '4028bb834eb7d609014eb87ccc57000a', NULL, NULL);


--
-- Data for Name: encounter; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 0, '2015-08-08', 'Bad attitude. Do not call back', false, 'Call', '4028bb834eb7d609014eb88158360010', NULL, '4028bb834eb7d609014eb8805415000f');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb88825be0016', '2015-07-22T21:30:07.138', false, '2015-07-22T21:30:07.138', 9, '2014-01-01', 'Responded to canvassing on street in Manhattan. Seemed like a nice guy.', false, 'Other', '4028bb834eb7d609014eb8830a700012', NULL, '4028bb834eb7d609014eb873e2050000');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb889a4e00019', '2015-07-22T21:31:45.211', false, '2015-07-22T21:31:45.211', 5, '2001-01-01', 'Walk-in during annual new member orientation', false, 'Other', '4028bb834eb7d609014eb87f5f8b000e', NULL, '4028bb834eb7d609014eb873e2050000');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb88a326f001a', '2015-07-22T21:32:21.446', false, '2015-07-22T21:32:21.446', 7, '2011-07-07', 'Cold-call', false, 'Call', '4028bb834eb7d609014eb87687190001', NULL, '4028bb834eb7d609014eb873e2050000');
INSERT INTO encounter (id, created, deleted, lastmodified, assessment, encounterdate, notes, requiresfollowup, type, contact_id, form_id, initiator_id) VALUES ('4028bb834eb7d609014eb88aabbf001b', '2015-07-22T21:32:52.493', false, '2015-07-22T21:32:52.493', 7, '2001-08-08', 'Met at academic conference', false, 'Other', '4028bb834eb7d609014eb8822d7d0011', NULL, '4028bb834eb7d609014eb8830a700012');


--
-- Data for Name: encounter_type; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO encounter_type (id, created, deleted, lastmodified, name) VALUES ('4028cb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 'Call');
INSERT INTO encounter_type (id, created, deleted, lastmodified, name) VALUES ('4028qb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 'Walk in');
INSERT INTO encounter_type (id, created, deleted, lastmodified, name) VALUES ('4028nb834eb7d609014eb886cf3a0015', '2015-07-22T21:28:39.466', false, '2015-07-22T21:28:39.466', 'Other');


--
-- Data for Name: form; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--



--
-- Data for Name: foundation; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO foundation (id, created, deleted, lastmodified, address, currentgrantor, name, phonenumber, primarycontactemail, primarycontactname, primarycontactphone, primarycontacttitle, secondarycontactemail, secondarycontactname, secondarycontactphone, secondarycontacttitle, website) VALUES ('ff808181538fbe7e01538fc3d23d000e', '2016-03-19T12:44:48.567', false, '2016-03-19T12:44:48.568', '123 Lane Ave. Portland, ME 04101', false, 'Test Foundation One', '2078989797', 'betty@foundation.com', 'Betty White', '8908889779', 'CEO', 'john@foundation.com', 'John Cusack', '9897788777', 'C3PO', 'foundation.com');
INSERT INTO foundation (id, created, deleted, lastmodified, address, currentgrantor, name, phonenumber, primarycontactemail, primarycontactname, primarycontactphone, primarycontacttitle, secondarycontactemail, secondarycontactname, secondarycontactphone, secondarycontacttitle, website) VALUES ('ff808181538fbe7e01538fc5afda000f', '2016-03-19T12:46:50.841', false, '2016-03-19T12:46:50.842', '898 6th Ave and 27th Street', false, 'Test Foundation Two', '8987798799', 'george@illuminati.org', 'George Harrison', '9999231231', 'Receptionist', 'john@illuminati.org', 'John Lennon', '8989821321', 'VP', 'illuminati.org');


--
-- Data for Name: foundation_grant; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO foundation_grant (id, created, deleted, lastmodified, amountappliedfor, amountreceived, applicationdeadline, description, endperiod, intentdeadline, name, reportdeadline, restriction, startperiod, foundation_id) VALUES ('ff808181538fbe7e01538fc717360011', '2016-03-19T12:48:22.838', false, '2016-03-19T12:48:22.838', 3500, 3500, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007de060178', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus suscipit massa id gravida suscipit.', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007df0c1f78', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007de010178', 'Grant for Prosperity', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007df0c1f78', NULL, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007df010178', 'ff808181538fbe7e01538fc3d23d000e');
INSERT INTO foundation_grant (id, created, deleted, lastmodified, amountappliedfor, amountreceived, applicationdeadline, description, endperiod, intentdeadline, name, reportdeadline, restriction, startperiod, foundation_id) VALUES ('ff808181538fbe7e01538fc859610012', '2016-03-19T12:49:45.312', false, '2016-03-19T12:49:45.312', 100, 100, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007db060178', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus suscipit massa id gravida suscipit. Mauris porttitor dolor nunc, at tincidunt tortor porttitor vel. Integer laoreet,', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007dc0c1f78', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007db010178', 'Grant for Unity', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007dc0c1f78', 'Activities that promote unity', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007dc010178', 'ff808181538fbe7e01538fc3d23d000e');


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: bayard_postgres
--

SELECT pg_catalog.setval('hibernate_sequence', 3, true);


--
-- Data for Name: interactionrecordtype; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO interactionrecordtype (id, created, deleted, lastmodified, name) VALUES ('ff808181538fbe7e01538fbf1c990000', '2016-03-19T12:39:39.905', false, '2016-03-19T12:39:39.905', 'Call');
INSERT INTO interactionrecordtype (id, created, deleted, lastmodified, name) VALUES ('ff808181538fbe7e01538fbf27a20001', '2016-03-19T12:39:42.753', false, '2016-03-19T12:39:42.753', 'Email');


--
-- Data for Name: interaction_record; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO interaction_record (id, created, deleted, lastmodified, dateofinteraction, notes, personcontacted, requiresfollowup, foundation_id, budget_item_id) VALUES ('ff808181538fbe7e01538fc8ba730013', '2016-03-19T12:50:10.160', false, '2016-03-19T12:50:10.162', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0030178', '212574', 'Betty White', true, 'ff808181538fbe7e01538fc3d23d000e', 'ff808181538fbe7e01538fbf27a20001');
INSERT INTO interaction_record (id, created, deleted, lastmodified, dateofinteraction, notes, personcontacted, requiresfollowup, foundation_id, budget_item_id) VALUES ('ff808181538fbe7e01538fc90c770014', '2016-03-19T12:50:31.156', false, '2016-03-19T12:50:31.159', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007db010178', '212575', 'John Cusack', false, 'ff808181538fbe7e01538fc3d23d000e', 'ff808181538fbe7e01538fbf1c990000');


--
-- Data for Name: sustainer_period; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO sustainer_period (id, created, deleted, lastmodified, canceldate, monthlyamount, periodstartdate, sentirsletter, donorinfo_id) VALUES ('ff808181538fbe7e01538fc2a9f8000c', '2016-03-19T12:43:32.674', false, '2016-03-19T12:43:32.674', NULL, 10, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007e0010178', false, 'ff808181538fbe7e01538fc201070009');
INSERT INTO sustainer_period (id, created, deleted, lastmodified, canceldate, monthlyamount, periodstartdate, sentirsletter, donorinfo_id) VALUES ('ff808181538fbe7e01538fc301e4000d', '2016-03-19T12:43:55.193', false, '2016-03-19T12:43:55.193', '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007df0c1f78', 15, '\xaced00057372000d6a6176612e74696d652e536572955d84ba1b2248b20c00007870770703000007df010178', false, 'ff808181538fbe7e01538fc201070009');


--
-- Data for Name: user_file_upload; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--



--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: bayard_postgres
--

INSERT INTO users (id, email, firstname, lastname, passwordhash, role) VALUES (2, 'superuser@email.com', 'firstName', 'lastName', '$2a$10$gAGKrqoGiN85BLGRGNjQXOQeODG3FVN.uq9EO/h9mx1hqgvqaO4wu', 'ROLE_SUPERUSER');

--
-- PostgreSQL database dump complete
--

