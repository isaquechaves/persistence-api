API em Java para o trabalho de graduação (TG) da FATEC Sorocaba.

Este projeto faz parte de uma arquitetura de dois microsseriços pensados para alimentar um fórum voltado para os alunos da Fatec, os alunos criam posts que podem ser 
dúvidas, informações úteis, dicas de aprendizado etc, e recebem respostas e interações através de comentários.
Neste site os alunos poderiam adicionar imagens, links de vídeos e arquivos. As funcionalidades propostas foram quase todas implementadas, 
com a única exceção de um banco não relacional para melhorar o desempenho e a usabilidade.

Esta API utiliza como principais tecnologias: Java, Spring boot, Spring Data JPA, Hibernate, Flyway migrations, Springfox Swagger e Json webtoken.

É uma API com autenticação, aplicada em determinadas controllers, portanto no projeto decidimos deixar parte do conteúdo aberto para que usuários não logados 
pudessem ter um preview dos posts, apenas usuários logados podem executar ações de DML, por exemplo, salvar, editar e apagar posts.
