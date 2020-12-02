insert into tags(id, name) values (1, 'T1');
insert into tags(id, name) values (2, 'T2');

insert into owners(id, name) values ('1', 'Owner 1');
insert into owners(id, name) values ('2', 'Owner 2');
insert into owners(id, name) values ('3', 'Owner 3');

insert into pets(id, name, owner_id, parent_id, tag_id) values (1, 'Dog', '1', null, 1);
insert into pets(id, name, owner_id, parent_id, tag_id) values (2, 'Cat', '1', 1, 1);
insert into pets(id, name, owner_id, parent_id, tag_id) values (3, 'Potato', '1', 2, 2);

insert into pet_tags(pet_id, tag_id) values (1, 1);
insert into pet_tags(pet_id, tag_id) values (1, 2);
insert into pet_tags(pet_id, tag_id) values (2, 1);
insert into pet_tags(pet_id, tag_id) values (2, 2);
insert into pet_tags(pet_id, tag_id) values (3, 2);
