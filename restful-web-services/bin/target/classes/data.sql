insert into user values (1001, sysdate(), 'AB');
insert into user values (1002, sysdate(), 'Jill');
insert into user values (1003, sysdate(), 'Jack');

insert into post values (11001, 'My first post', 1001);
insert into post values (11002, 'My second post', 1001);
insert into post values (11003, 'My first post', 1002);
insert into post values (11004, 'My second post', 1002);
insert into post values (11005, 'My first post', 1003);
insert into post values (11006, 'My second post', 1003);