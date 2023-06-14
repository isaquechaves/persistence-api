INSERT INTO role (id, updatedat, updatedby, createdat, createdby, "version", description, "name") VALUES(1, now(), null, now(), null, 1, 'ROLE_ADMIN', 'ROLE_ADMIN');
INSERT INTO role (id, updatedat, updatedby, createdat, createdby, "version", description, "name") VALUES(2, now(), null, now(), null, 1, 'ROLE_ALUNO', 'ROLE_ALUNO');

INSERT INTO public.userinternal (id, updatedat, updatedby, createdat, createdby, "version", apelido, email, enabled, "name", "password", semestre) VALUES(1,now(), '', now(), '', 1, 'Isaque', 'isaque.oliveira2@fatec.sp.gov.br', true, 'Isaque Chaves', '$2a$10$lki6dSNY0Z.WfSgLobL57e.qA8rqDdYKg8CdqJ/rG.NqbK9KJr1ri', 6);
INSERT INTO public.userinternal (id, updatedat, updatedby, createdat, createdby, "version", apelido, email, enabled, "name", "password", semestre) VALUES(2, now(), '', now(), '', 1, 'Admin', 'admin@fatec.sp.gov.br', true, 'Admin', '$2a$10$lki6dSNY0Z.WfSgLobL57e.qA8rqDdYKg8CdqJ/rG.NqbK9KJr1ri', 6);

INSERT INTO public.userinternal_x_role (userinternal_id, role_id) VALUES(1, 1);
INSERT INTO public.userinternal_x_role (userinternal_id, role_id) VALUES(2, 1);

INSERT INTO public.disciplina
(id, updatedat, updatedby, createdat, createdby, "version", descricao, nome)
VALUES(1, Now(), '', Now(), '', 0, 'POO orientação objetos', 'POO');


INSERT INTO public.tag
(id, updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(1, Now() , '', Now(), '', 0, 'Java', 0);

INSERT INTO public.tag
(id, updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(2, Now(), '', Now(), '', 0, 'POO', 0);

INSERT INTO public.tag
(id, updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(3, Now(), '', Now(), '', 0, 'JS', 0);

INSERT INTO public.tag
(id, updatedat, updatedby, createdat, createdby, "version", nome, qtdeposts)
VALUES(4, Now(), '', Now(), '', 0, 'MPCT', 0);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (1, now(), '', now(), '', 0, now(), now(), 'Lorem ipsum dolor sit amet', 'Post 1', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (1, 2);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (2, now(), '', now(), '', 0, now(), now(), 'Consectetur adipiscing elit', 'Post 2', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (2, 2);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (2, 1);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (3, now(), '', now(), '', 0, now(), now(), 'Sed do eiusmod tempor incididunt', 'Post 11', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (3, 4);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (3, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (3, 2);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (4, now(), '', now(), '', 0, now(), now(), 'Lorem ipsum dolor sit amet', 'Post 1', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (4, 1);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (5, now(), '', now(), '', 0, now(), now(), 'Consectetur adipiscing elit', 'Post 2', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (5, 3);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (6, now(), '', now(), '', 0, now(), now(), 'Sed do eiusmod tempor incididunt', 'Post 3', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (6, 3);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (7, now(), '', now(), '', 0, now(), now(), 'Ut labore et dolore magna aliqua', 'Post 4', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (7, 4);

INSERT INTO public.post (id, updatedat, updatedby, createdat, createdby, "version", atualizadoem, criadoem, descricao, titulo, autor_id, disciplina_id)
VALUES (8, now(), '', now(), '', 0, now(), now(), 'Duis aute irure dolor in reprehenderit', 'Post 5', 1, 1);
INSERT INTO public.post_x_tag (id, tag_id) VALUES (8, 1);