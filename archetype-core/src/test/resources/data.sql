insert into entity_dates(one_id, date, date_time) values (1, DATE '2010-06-06', TIMESTAMP '2010-01-01 10:00:00+01');
insert into entity_dates(one_id, date, date_time) values (2, DATE '2100-06-06', TIMESTAMP '2100-01-01 10:00:00+01');

insert into entity_nodes(id, name, parent_id, nearest_id) values (1, 'Center', null, null);
insert into entity_nodes(id, name, parent_id, nearest_id) values (2, 'North', 1, 1);
insert into entity_nodes(id, name, parent_id, nearest_id) values (3, 'North+', 2, 2);
insert into entity_nodes(id, name, parent_id, nearest_id) values (4, 'Est', 1, 1);
insert into entity_nodes(id, name, parent_id, nearest_id) values (5, 'West', 1, 1);

insert into entity_nodes_neighbors(source_id, target_id) values (1, 2);
insert into entity_nodes_neighbors(source_id, target_id) values (1, 4);
insert into entity_nodes_neighbors(source_id, target_id) values (1, 5);
insert into entity_nodes_neighbors(source_id, target_id) values (2, 3);

insert into big_entity(id, b_int, b_dec) values (1, 100, 0.33);
insert into big_entity(id, b_int, b_dec) values (2, 100, 0.32);
insert into big_entity(id, b_int, b_dec) values (3, 100, 0.34);
