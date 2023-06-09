INSERT INTO role (id, updatedat, updatedby, createdat, createdby, "version", description, "name") VALUES(1, now(), null, now(), null, 1, 'ROLE_ADMIN', 'ROLE_ADMIN');
INSERT INTO role (id, updatedat, updatedby, createdat, createdby, "version", description, "name") VALUES(2, now(), null, now(), null, 1, 'ROLE_ALUNO', 'ROLE_ALUNO');

INSERT INTO public.userinternal (id, updatedat, updatedby, createdat, createdby, "version", apelido, email, enabled, "name", "password", semestre) VALUES(1,now(), '', now(), '', 1, 'Isaque', 'isaque.oliveira2@fatec.sp.gov.br', true, 'Isaque Chaves', '12345', 6);
INSERT INTO public.userinternal (id, updatedat, updatedby, createdat, createdby, "version", apelido, email, enabled, "name", "password", semestre) VALUES(2, now(), '', now(), '', 1, 'Admin', 'admin@admin', true, 'Admin', '12345', 6);

INSERT INTO public.userinternal_x_role (userinternal_id, role_id) VALUES(1, 1);
INSERT INTO public.userinternal_x_role (userinternal_id, role_id) VALUES(2, 1);

INSERT INTO public.disciplina
(updatedat, updatedby, createdat, createdby, "version", descricao, nome)
VALUES(Now(), '', Now(), '', 0, 'POO orientação objetos', 'POO');


INSERT INTO public.tag
(updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(Now() , '', Now(), '', 0, 'Java', 0);

INSERT INTO public.tag
(updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(Now(), '', Now(), '', 0, 'POO', 0);

INSERT INTO public.tag
(updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(Now(), '', Now(), '', 0, 'JS', 0);

INSERT INTO public.tag
(updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(Now(), '', Now(), '', 0, 'MPCT', 0);