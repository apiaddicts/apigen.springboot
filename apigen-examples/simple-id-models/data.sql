insert into tags(name) values ('T1');
insert into tags(name) values ('T2');

insert into owners(id, name) values ('1', 'Owner 1');
insert into owners(id, name) values ('2', 'Owner 2');
insert into owners(id, name) values ('3', 'Owner 3');

insert into pets(name, owner_parent_tag_id) values ('Dog', '1', null, 1);
insert into pets(name, owner_parent_tag_id) values ('Cat', '1', 1, 1);
insert into pets(name, owner_parent_id, tag_id) values ('Potato', '1', 2, 2);

insert into pet_tags(pet_id, tag_id) values (1, 1);
insert into pet_tags(pet_id, tag_id) values (1, 2);
insert into pet_tags(pet_id, tag_id) values (2, 1);
insert into pet_tags(pet_id, tag_id) values (2, 2);
insert into pet_tags(pet_id, tag_id) values (3, 2);
