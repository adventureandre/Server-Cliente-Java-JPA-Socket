# Projeto Cadastro Server-Cliente - RPG0018

## Descrição

Este projeto Java foi desenvolvido para atender aos requisitos da missão prática RPG0018 - Por que não paralelizar, no nível 5 do mundo 3. O objetivo principal é criar servidores e clientes baseados em Socket, utilizando Threads no lado do servidor e cliente, e acessando o banco de dados via JPA. O projeto é composto por duas partes principais: o servidor (application/ComandosHandler) e o cliente (application/ThreadClient).

## Estrutura do Projeto

### Parte 1 - Servidor 

Esta parte contém a implementação do servidor, que manipula comandos enviados pelos clientes, realiza operações no banco de dados e fornece as respostas necessárias. O arquivo `ComandosHandler.java` é responsável por essa lógica, utilizando Threads para lidar com vários clientes simultaneamente.

### Parte 2 - Cliente

Esta parte concentra-se na implementação do cliente, que interage com o servidor, enviando comandos e recebendo as respostas correspondentes. O arquivo `ThreadClient.java` representa a implementação do cliente, utilizando Threads para possibilitar a interação síncrona e assíncrona com o servidor.

## Execução do Aplicativo

Para executar o aplicativo, siga as instruções detalhadas no README de cada diretório correspondente às partes do projeto .

## Contribuição e Suporte

Para contribuir ou relatar problemas no repositório do projeto, acesse os diretórios. Para suporte adicional, entre em contato com o desenvolvedor [André Luíz F Souza](https://github.com/adventureandre).

## Licença

Este projeto é distribuído sob a licença MIT. Consulte o arquivo [License File](https://github.com/adventureandre/Lib/blob/main/LICENSE) para mais informações.

## Créditos

- [André Luíz F Souza](https://github.com/adventureandre) (Desenvolvedor)
- [ADVENTUREANDRE](https://www.linkedin.com/in/adventureandre) (LinkedIn)
- [Adventure.dev.br](https://adventure.dev.br) (Site)
