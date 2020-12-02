insert into tags(str_value, int_value, name) values ('T', 1, 'Tag 1');
insert into tags(str_value, int_value, name) values ('T', 2, 'Tag 2');

insert into owners(str_value, int_value, name) values ('O', 1, 'Owner 1');
insert into owners(str_value, int_value, name) values ('O', 2, 'Owner 2');
insert into owners(str_value, int_value, name) values ('O', 3, 'Owner 3');

insert into pets(str_value, int_value, name, owner_s, owner_i, parent_s, parent_i, tag_s, tag_i) values ('P', 1, 'Dog', 'O', 1, null, null, 'T', 1);
insert into pets(str_value, int_value, name, owner_s, owner_i, parent_s, parent_i, tag_s, tag_i) values ('P', 2, 'Cat', 'O', 1, 'P', 1, 'T', 1);
insert into pets(str_value, int_value, name, owner_s, owner_i, parent_s, parent_i, tag_s, tag_i) values ('P', 3, 'Potato', 'O', 1, 'P', 2, 'T', 2);

insert into pet_tags(pet_s_id, pet_i_id, tag_s_id, tag_i_id) values ('P', 1, 'T', 1);
insert into pet_tags(pet_s_id, pet_i_id, tag_s_id, tag_i_id) values ('P', 1, 'T', 2);
insert into pet_tags(pet_s_id, pet_i_id, tag_s_id, tag_i_id) values ('P', 2, 'T', 1);
insert into pet_tags(pet_s_id, pet_i_id, tag_s_id, tag_i_id) values ('P', 2, 'T', 2);
insert into pet_tags(pet_s_id, pet_i_id, tag_s_id, tag_i_id) values ('P', 3, 'T', 2);
