insert into tag(id_s, id_n, name) values ('T', 1, 'Tag 1');
insert into tag(id_s, id_n, name)  values ('T', 2, 'Tag 2');

insert into owner(id_s, id_n, name) values ('O', 1, 'Owner 1');
insert into owner(id_s, id_n, name) values ('O', 2, 'Owner 2');
insert into owner(id_s, id_n, name) values ('O', 3, 'Owner 3');

insert into pet(id_s, id_n, name, owner_id_s, owner_id_n, parent_id_s, parent_id_n, tag_id_s, tag_id_n) values ('P', 1, 'Dog', 'O', 1, null, null, 'T', 1);
insert into pet(id_s, id_n, name, owner_id_s, owner_id_n, parent_id_s, parent_id_n, tag_id_s, tag_id_n) values ('P', 2, 'Cat', 'O', 1, 'P', 1, 'T', 1);
insert into pet(id_s, id_n, name, owner_id_s, owner_id_n, parent_id_s, parent_id_n, tag_id_s, tag_id_n) values ('P', 3, 'Potato', 'O', 1, 'P', 2, 'T', 2);

insert into pet_tags(pet_id_s, pet_id_n, tag_id_s, tag_id_n) values ('P', 1, 'T', 1);
insert into pet_tags(pet_id_s, pet_id_n, tag_id_s, tag_id_n) values ('P', 1, 'T', 2);
insert into pet_tags(pet_id_s, pet_id_n, tag_id_s, tag_id_n) values ('P', 2, 'T', 1);
insert into pet_tags(pet_id_s, pet_id_n, tag_id_s, tag_id_n) values ('P', 2, 'T', 2);
insert into pet_tags(pet_id_s, pet_id_n, tag_id_s, tag_id_n) values ('P', 3, 'T', 2);
